package com.example.orderinventory.controller;

import com.example.orderinventory.model.dto.OrderDto;
import com.example.orderinventory.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private OrderService orderService;

    @Test
    void createOrder_returns201AndLocation() throws Exception {
        OrderDto resp = new OrderDto(10L, "Alice", "PENDING", LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(orderService.createOrder(any(OrderDto.class))).thenReturn(resp);

        OrderDto req = new OrderDto();
        req.setCustomerName("Alice");

        mvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/orders/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.customerName").value("Alice"));
    }

    @Test
    void getOrder_returns200() throws Exception {
        OrderDto dto = new OrderDto(7L, "Bob", "PENDING", LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(orderService.getOrderById(7L)).thenReturn(dto);

        mvc.perform(get("/orders/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.customerName").value("Bob"));
    }

    @Test
    void updateOrder_returns200() throws Exception {
        OrderDto updated = new OrderDto(8L, "Carol", "PENDING", LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(orderService.updateOrder(eq(8L), any(OrderDto.class))).thenReturn(updated);

        OrderDto req = new OrderDto();
        req.setCustomerName("Carol");

        mvc.perform(put("/orders/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.customerName").value("Carol"));
    }

    @Test
    void deleteOrder_returnsNoContent() throws Exception {
        Mockito.doNothing().when(orderService).deleteOrder(9L);

        mvc.perform(delete("/orders/9"))
                .andExpect(status().isNoContent());
    }

    @Test
    void confirmAndCancel_returnOk() throws Exception {
        Mockito.doNothing().when(orderService).confirmOrder(11L);
        Mockito.doNothing().when(orderService).cancelOrder(12L);

        mvc.perform(post("/orders/11/confirm")).andExpect(status().isOk());
        mvc.perform(post("/orders/12/cancel")).andExpect(status().isOk());
    }
}