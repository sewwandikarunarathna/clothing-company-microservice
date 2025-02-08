package com.example.order.controller;

import com.example.order.dto.OrderDTO;
import com.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/getorders")
    public List<OrderDTO> getOrders() {
        return orderService.getAllOrders();
    }

//    @GetMapping("/getorder/{orderId}/{orderName}")
//    public OrderDTO getOrder(@PathVariable Integer orderId, @PathVariable String orderName) {
//        return orderService.getOrderByIdAndName(orderId, orderName);
//    }

    @PostMapping("/addorder")
    public OrderDTO saveOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.saveOrder(orderDTO);
    }

    @PutMapping("/updateorder")
    public OrderDTO updateOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.updateOrder(orderDTO);
    }

    @DeleteMapping("/deleteorder/{orderId}") //use a path parameter
    public String deleteOrderById(@PathVariable Integer orderId) {
        return orderService.deleteOrderById(orderId);
    }
}
