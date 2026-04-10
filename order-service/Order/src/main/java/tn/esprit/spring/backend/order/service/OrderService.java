package tn.esprit.spring.backend.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.backend.client.ProductClient;
import tn.esprit.spring.backend.client.UserClient;
import tn.esprit.spring.backend.order.dto.CreateOrderRequest;
import tn.esprit.spring.backend.order.dto.OrderEventDTO;
import tn.esprit.spring.backend.order.dto.OrderResponse;
import tn.esprit.spring.backend.order.dto.ProductDto;
import tn.esprit.spring.backend.order.dto.ProductStockEventDTO;
import tn.esprit.spring.backend.order.dto.UserDto;
import tn.esprit.spring.backend.order.entity.Order;
import tn.esprit.spring.backend.order.entity.OrderStatus;
import tn.esprit.spring.backend.order.messaging.OrderProducer;
import tn.esprit.spring.backend.order.messaging.ProductStockProducer;
import tn.esprit.spring.backend.repository.OrderRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final OrderProducer orderProducer;
    private final ProductStockProducer productStockProducer;

    public OrderService(OrderRepository orderRepository,
                        UserClient userClient,
                        ProductClient productClient,
                        OrderProducer orderProducer,
                        ProductStockProducer productStockProducer) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.productClient = productClient;
        this.orderProducer = orderProducer;
        this.productStockProducer = productStockProducer;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        UserDto user = userClient.getUserById(request.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found with id: " + request.getUserId());
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("User account is disabled: " + request.getUserId());
        }

        ProductDto product = productClient.getProductById(request.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + request.getProductId());
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        if (product.getStock() == null || product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Not enough stock for product id: " + request.getProductId());
        }

        Double totalAmount = product.getPrice() * request.getQuantity();

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setTotalAmount(totalAmount);
        order.setReference(generateOrderReference());
        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);

        OrderEventDTO orderEventDTO = new OrderEventDTO(
                savedOrder.getId(),
                savedOrder.getReference(),
                savedOrder.getUserId(),
                savedOrder.getProductId(),
                savedOrder.getQuantity(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus().name()
        );

        orderProducer.sendOrder(orderEventDTO);

        ProductStockEventDTO stockEventDTO = new ProductStockEventDTO(
                savedOrder.getId(),
                savedOrder.getProductId(),
                savedOrder.getQuantity()
        );

        productStockProducer.sendStockUpdate(stockEventDTO);

        return mapToResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    public List<OrderResponse> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrder(Long id, CreateOrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        UserDto user = userClient.getUserById(request.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found with id: " + request.getUserId());
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("User account is disabled: " + request.getUserId());
        }

        ProductDto product = productClient.getProductById(request.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + request.getProductId());
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        if (product.getStock() == null || product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Not enough stock for product id: " + request.getProductId());
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only PENDING orders can be updated");
        }

        Double totalAmount = product.getPrice() * request.getQuantity();

        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    @Transactional
    public OrderResponse confirmOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only PENDING orders can be confirmed");
        }

        order.setStatus(OrderStatus.CONFIRMED);
        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    @Transactional
    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only PENDING orders can be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    private String generateOrderReference() {
        int year = LocalDate.now().getYear();

        Instant startOfYear = LocalDate.of(year, 1, 1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();

        Instant endOfYear = LocalDate.of(year, 12, 31)
                .atTime(23, 59, 59)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        long count = orderRepository.countByOrderDateBetween(startOfYear, endOfYear);
        return String.format("ORD-%d-%04d", year, count + 1);
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setReference(order.getReference());
        response.setOrderDate(order.getOrderDate());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setUserId(order.getUserId());
        response.setProductId(order.getProductId());
        response.setQuantity(order.getQuantity());
        return response;
    }
}