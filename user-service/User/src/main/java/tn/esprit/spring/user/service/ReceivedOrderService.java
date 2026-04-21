package tn.esprit.spring.user.service;

import org.springframework.stereotype.Service;
import tn.esprit.spring.user.dto.OrderEventDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceivedOrderService {

    private final List<OrderEventDTO> receivedOrders = new ArrayList<>();

    public void addReceivedOrder(OrderEventDTO orderEventDTO) {
        receivedOrders.add(orderEventDTO);
        System.out.println("Order added to received list: " + orderEventDTO.getReference());
    }

    public List<OrderEventDTO> getReceivedOrders() {
        return receivedOrders;
    }
}