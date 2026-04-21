package tn.esprit.spring.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.spring.user.dto.OrderEventDTO;
import tn.esprit.spring.user.service.ReceivedOrderService;

import java.util.List;

@RestController
@RequestMapping("/api/users/received-orders")
public class ReceivedOrderController {

    private final ReceivedOrderService receivedOrderService;

    public ReceivedOrderController(ReceivedOrderService receivedOrderService) {
        this.receivedOrderService = receivedOrderService;
    }

    @GetMapping
    public List<OrderEventDTO> getReceivedOrders() {
        return receivedOrderService.getReceivedOrders();
    }
}