package com.prucabs.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prucabs.dto.OrderDto;
import com.prucabs.dto.OrderPatchDto;
import com.prucabs.dto.OrderResponseDto;
import com.prucabs.service.OrdersService;

@RestController
@RequestMapping("/orders")
public class OrdersController {
	
	@Autowired
	private OrdersService ordersService;
	
	@PostMapping
	public OrderResponseDto createOrder(@RequestBody @Valid OrderDto orderRequestDto) {
		OrderResponseDto orderResponseDto = ordersService.createOrder(orderRequestDto);
		return orderResponseDto;
	}
	
	@GetMapping("/{orderId}")
	public OrderResponseDto getOrderById(@PathVariable String orderId) {
		return ordersService.getOrderById(orderId);
	}
	
	@PatchMapping("/{orderId}/cancel")
	public OrderResponseDto cancelOrder(@PathVariable String orderId) {
		return ordersService.cancelOrder(orderId);
	}
	
	@PatchMapping("/{orderId}")
	public OrderResponseDto updateOrder(@PathVariable String orderId, @RequestBody @Valid OrderPatchDto orderPatchDto) {
		return ordersService.updateOrder(orderId, orderPatchDto);
	}
	
	@PatchMapping("/{orderId}/complete")
	public OrderResponseDto completeOrder(@PathVariable String orderId) {
		return ordersService.completeOrder(orderId);
	}
}
