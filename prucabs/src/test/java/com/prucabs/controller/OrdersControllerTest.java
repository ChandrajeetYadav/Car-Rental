package com.prucabs.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.prucabs.service.OrdersService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class OrdersControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private OrdersService ordersService;
	
	@Test
	public void createOrderTest() throws Exception {
		String requestBody = "{\r\n"
				+ "    \"carId\" : 3,\r\n"
				+ "    \"startDate\" : \"2022-12-06 14:00:00\",\r\n"
				+ "    \"endDate\" : \"2022-12-06 17:30:00\",\r\n"
				+ "    \"customerName\" : \"Chandu\",\r\n"
				+ "    \"customerEmail\" : \"chandu@gmail.com\",\r\n"
				+ "    \"customerPhoneNumber\" : \"9832198321\"\r\n"
				+ "}";
		mockMvc.perform(post("/orders").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	@Test
	public void createOrderWhenNullBodyTest() throws Exception {
		mockMvc.perform(post("/orders").content("").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void getOrderByIdTest() throws Exception {
		mockMvc.perform(get("/orders/orderId")).andExpect(status().isOk());
	}
	
	@Test
	public void cancelOrderTest() throws Exception {
		mockMvc.perform(patch("/orders/test/cancel")).andExpect(status().isOk());
	}
	
	@Test
	public void completeOrderTest() throws Exception {
		mockMvc.perform(patch("/orders/test/complete")).andExpect(status().isOk());
	}
	
	@Test
	public void updateOrderTest() throws Exception {
		String requestBody = "{\r\n"
				+ "    \"customerName\" : \"Yadav\",\r\n"
				+ "    \"customerPhoneNumber\": \"9832198325\"\r\n"
				+ "}";
		mockMvc.perform(patch("/orders/test").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
}
