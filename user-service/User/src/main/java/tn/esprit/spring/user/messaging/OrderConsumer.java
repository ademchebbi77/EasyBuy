package tn.esprit.spring.user.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tn.esprit.spring.config.RabbitMQConfig;
import tn.esprit.spring.user.dto.OrderEventDTO;
import tn.esprit.spring.user.service.ReceivedOrderService;

@Service
public class OrderConsumer {

    private final ReceivedOrderService receivedOrderService;

    public OrderConsumer(ReceivedOrderService receivedOrderService) {
        this.receivedOrderService = receivedOrderService;
    }

    @RabbitListener(
            queues = RabbitMQConfig.ORDER_QUEUE,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void receiveOrder(OrderEventDTO orderEventDTO) {
        System.out.println(
                "Order received from RabbitMQ: userId=" + orderEventDTO.getUserId()
                        + ", productId=" + orderEventDTO.getProductId()
                        + ", quantity=" + orderEventDTO.getQuantity()
                        + ", amount=" + orderEventDTO.getTotalAmount()
        );

        receivedOrderService.addReceivedOrder(orderEventDTO);
    }
}