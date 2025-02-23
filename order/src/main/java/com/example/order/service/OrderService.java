package com.example.order.service;

import com.example.inventory.dto.InventoryDTO;
import com.example.order.common.ErrorOrderResponse;
import com.example.order.common.OrderResponse;
import com.example.order.common.SuccessOrderResponse;
import com.example.order.dto.OrderDTO;
import com.example.order.model.Orders;
import com.example.order.repo.OrderRepo;
import com.example.product.dto.ProductDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Transactional
@Service
public class OrderService {
    private final WebClient inventoryWebClient; //when creating variable for web client, it requires to create a constructor (below)
    private final WebClient productWebClient;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ModelMapper modelMapper;

    public OrderService(WebClient inventoryWebClient, WebClient productWebClient, OrderRepo orderRepo, ModelMapper modelMapper) {
        this.inventoryWebClient = inventoryWebClient;
        this.productWebClient = productWebClient;
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
    }

    public List<OrderDTO> getAllOrders() {
        List<Orders> orderList = orderRepo.findAll();
        return modelMapper.map(orderList, new TypeToken<List<OrderDTO>>(){}.getType());
    }

    public OrderResponse saveOrder(OrderDTO orderDTO) {
        Integer itemId = orderDTO.getItemId();

        try {
            InventoryDTO inventoryResponse = inventoryWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/item/{itemId}").build(itemId)) //from where to fetch items // use uriBuilder to use itemId variable in this path
                    .retrieve() // to fetch to this order service
                    .bodyToMono(InventoryDTO.class) // to give data type  in which you're retrieving
                    .block(); // to publish the response giving by bodyToMono

//            System.out.println(inventoryResponse);
            assert inventoryResponse != null; //sometime invResp should be null, if not, continue to if block

            Integer productId = inventoryResponse.getProductId();

            ProductDTO productResponse = productWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/{productId}").build(productId))
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();

            assert productResponse != null;

            if (inventoryResponse.getQuantity() > 0) {
                if(productResponse.isForSale()){
                    orderRepo.save(modelMapper.map(orderDTO, Orders.class));
                    return new SuccessOrderResponse(orderDTO);
                }
                else {
                    return new ErrorOrderResponse("Product is not for sale, please try later");
                }
            }
            else {
                return new ErrorOrderResponse("Item is not available, please try later");
            }
        }
        catch (WebClientResponseException e){
            if(e.getStatusCode().is5xxServerError()){ //this is not the best way to handle 500 server errors since they can be occurred due to many reasons.
                return new ErrorOrderResponse("Item is not found");
            }
        }
        return null;
    }

    public OrderDTO updateOrder(OrderDTO orderDTO) {
        orderRepo.save(modelMapper.map(orderDTO, Orders.class));
        return orderDTO;
    }

    public String deleteOrderById(Integer orderId) {
        orderRepo.deleteById(orderId);
        return "Order with id " + orderId + " was deleted";
    }

//    public OrderDTO getOrderByIdAndName(Integer orderId, String orderName) {
//        Order order = orderRepo.getOrderByIdAndName(orderId, orderName);
//        return modelMapper.map(order, OrderDTO.class); //map the result order to DTO class for return
//    }
}
