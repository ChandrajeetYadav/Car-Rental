package com.prucabs.service;

import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prucabs.constant.ORDER_BOOKING_STATUS;
import com.prucabs.dto.OrderDto;
import com.prucabs.dto.OrderResponseDto;
import com.prucabs.entity.Cars;
import com.prucabs.entity.CarsOrders;
import com.prucabs.exception.BadRequestException;
import com.prucabs.exception.OrdersException;
import com.prucabs.repository.CarsOrdersRepository;
import com.prucabs.repository.CarsRepository;
import com.prucabs.repository.TransactionRepository;
import com.prucabs.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class OrdersServiceTest {
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Mock
	private CarsRepository carsRepository;

	@Mock
	private CarsOrdersRepository carsOrdersRepository;

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private OrdersService ordersService;

	@Test
	public void shouldThrowBadRequestWhenPastDateUsedInCreateOrder() {
		OrderDto orderDto = new OrderDto();
		orderDto.setStartDate("2022-11-06 14:00:00");
		orderDto.setEndDate("2022-12-06 14:00:00");

		Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
			ordersService.createOrder(orderDto);
		});

		Assertions.assertEquals("Invalid start/end date.", exception.getMessage());
	}

	@Test
	public void shouldThrowBadRequestWhenStartDateIsGreaterThanEndDateInCreateOrder() {
		String endDate = SIMPLE_DATE_FORMAT.format(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.getDate(endDate));
		cal.add(Calendar.DAY_OF_MONTH, 3);
		String startDate = SIMPLE_DATE_FORMAT.format(cal.getTime());

		OrderDto orderDto = new OrderDto();
		orderDto.setStartDate(startDate);
		orderDto.setEndDate(endDate);

		Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
			ordersService.createOrder(orderDto);
		});
		Assertions.assertEquals("Invalid start/end date.", exception.getMessage());
	}

	@Test
	public void shouldThrowOrdersExceptionWhenStartDateIsMoreThanThirtyDaysAheadFromCurrentDateInCreateOrder() {
		String curDate = SIMPLE_DATE_FORMAT.format(new Date());
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.getDate(curDate));
		cal.add(Calendar.DAY_OF_MONTH, 32);
		String startDate = SIMPLE_DATE_FORMAT.format(cal.getTime());
		
		cal.add(Calendar.HOUR, 5);
		String endDate =  SIMPLE_DATE_FORMAT.format(cal.getTime());

		OrderDto orderDto = new OrderDto();
		orderDto.setStartDate(startDate);
		orderDto.setEndDate(endDate);

		Exception exception = Assertions.assertThrows(OrdersException.class, () -> {
			ordersService.createOrder(orderDto);
		});

		Assertions.assertEquals("Car can be booked within next 30 days", exception.getMessage());
	}
	
	@Test
	public void shouldThrowOrdersExceptionWhenBookForMoreThanThirtyDaysInCreateOrder() {
		Date date = new Date();
		String curDate = SIMPLE_DATE_FORMAT.format(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.getDate(curDate));
		cal.add(Calendar.MINUTE, 1);
		String startDate = SIMPLE_DATE_FORMAT.format(cal.getTime());
		cal.add(Calendar.DAY_OF_MONTH, 31);
		String endDate = SIMPLE_DATE_FORMAT.format(cal.getTime());

		OrderDto orderDto = new OrderDto();
		orderDto.setStartDate(startDate);
		orderDto.setEndDate(endDate);
		
		System.out.println(startDate);
		System.out.println(endDate);

		Exception exception = Assertions.assertThrows(OrdersException.class, () -> {
			ordersService.createOrder(orderDto);
		});
		Assertions.assertEquals("Car booking is allowed only for 30 days.", exception.getMessage());
	}
	
	@Test
	public void shouldThrowBadRequestExceptionWhenStartAndEndDateAreSameInCreateOrder() {
		Date date = new Date();
		String curDate = SIMPLE_DATE_FORMAT.format(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.getDate(curDate));
		cal.add(Calendar.MINUTE, 1);
		String startDate = SIMPLE_DATE_FORMAT.format(cal.getTime());
		
		OrderDto orderDto = new OrderDto();
		orderDto.setStartDate(startDate);
		orderDto.setEndDate(startDate);
		
		Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
			ordersService.createOrder(orderDto);
		});
		Assertions.assertEquals("Invalid start/end date.", exception.getMessage());
	}
	
	@Test
	public void shouldThrowBadRequestExceptionForInvalidCarInCreateOrder() {
		Optional<Cars> cars = Optional.empty();
		when(carsRepository.findById(5L)).thenReturn(cars);
		
		Date date = new Date();
		String curDate = SIMPLE_DATE_FORMAT.format(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.getDate(curDate));
		cal.add(Calendar.MINUTE, 1);
		String startDate = SIMPLE_DATE_FORMAT.format(cal.getTime());
		cal.add(Calendar.HOUR, 3);
		String endDate = SIMPLE_DATE_FORMAT.format(cal.getTime());
		
		OrderDto orderDto = new OrderDto();
		orderDto.setCarId(5L);
		orderDto.setStartDate(startDate);
		orderDto.setEndDate(endDate);
		
		Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
			ordersService.createOrder(orderDto);
		});
		Assertions.assertEquals("Invalid Car Id", exception.getMessage());
	}
	
	@Test
	public void createOrderTest() {
		Date date = new Date();
		String curDate = SIMPLE_DATE_FORMAT.format(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtils.getDate(curDate));
		cal.add(Calendar.MINUTE, 1);
		String startDate = SIMPLE_DATE_FORMAT.format(cal.getTime());
		cal.add(Calendar.HOUR, 3);
		String endDate = SIMPLE_DATE_FORMAT.format(cal.getTime());
		
		OrderDto orderDto = new OrderDto();
		orderDto.setCarId(1L);
		orderDto.setStartDate(startDate);
		orderDto.setEndDate(endDate);
		
		Optional<Cars> cars = Optional.of(new Cars());
		when(carsRepository.findById(1L)).thenReturn(cars);
		
		when(carsOrdersRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArguments()[0]);
		OrderResponseDto actualResponse = ordersService.createOrder(orderDto);
		Assertions.assertEquals(ORDER_BOOKING_STATUS.CONFIRMED.toString(), actualResponse.getOrderStatus());
	}

	@Test
	public void shouldThrowBadRequestExceptionForEmptyOrderId() {
		Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
			ordersService.getOrderById("");
		});
		Assertions.assertEquals("Order id is missing or null!", exception.getMessage());
	}
	
	@Test
	public void shouldThrowBadRequestExceptionForNullOrderId() {
		Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
			ordersService.getOrderById(null);
		});
		Assertions.assertEquals("Order id is missing or null!", exception.getMessage());
	}
	
	@Test
	public void shouldThrowBadRequestExceptionForInvalidOrderId() {
		Optional<CarsOrders> carsOrders = Optional.empty();
		when(carsOrdersRepository.findById(Mockito.anyString())).thenReturn(carsOrders);
		Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
			ordersService.getOrderById("test");
		});
		Assertions.assertEquals("Invalid order id", exception.getMessage());
	}
	
	@Test
	public void getOrderByIdFromDbTest() {
		Date curDate = new Date();
		CarsOrders carsOrders = new CarsOrders();
		carsOrders.setOrderId("7fa77e21-9cf7-4dd4-9a08-a615f489a592");
		carsOrders.setStatus(ORDER_BOOKING_STATUS.CONFIRMED.toString());
		carsOrders.setStartDate(curDate);
		carsOrders.setEndDate(curDate);
		
		Optional<CarsOrders> optionalCarsOrders = Optional.of(carsOrders);
		when(carsOrdersRepository.findById(Mockito.anyString())).thenReturn(optionalCarsOrders);
		
		OrderResponseDto actualResponse = ordersService.getOrderById("test");
		Assertions.assertEquals("7fa77e21-9cf7-4dd4-9a08-a615f489a592", actualResponse.getOrderId());
		Assertions.assertEquals(201, actualResponse.getStatusCode());
		Assertions.assertEquals(ORDER_BOOKING_STATUS.CONFIRMED.toString(), actualResponse.getOrderStatus());
	}
	
	@Test
	public void shouldThrowBadRequestExceptionWhenCancelingEmptyOrderId() {
		Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
			ordersService.cancelOrder("");
		});
		Assertions.assertEquals("Order id is missing or null!", exception.getMessage());
	}
	
	@Test
	public void shouldThrowBadRequestExceptionWhenCancelingNullOrderId() {
		Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
			ordersService.cancelOrder(null);
		});
		Assertions.assertEquals("Order id is missing or null!", exception.getMessage());
	}
	
	@Test
	public void shouldThrowBadRequestExceptionWhenCancelingInvalidOrderId() {
		Optional<CarsOrders> carsOrders = Optional.empty();
		when(carsOrdersRepository.findById(Mockito.anyString())).thenReturn(carsOrders);
		Exception exception = Assertions.assertThrows(BadRequestException.class, () -> {
			ordersService.cancelOrder("test");
		});
		Assertions.assertEquals("Invalid order id", exception.getMessage());
	}
	
	@Test
	public void cancelCompletedOrderTest() {
		CarsOrders carsOrders = new CarsOrders();
		carsOrders.setStatus(ORDER_BOOKING_STATUS.COMPLETED.toString());
		Optional<CarsOrders> optionalCarsOrders = Optional.of(carsOrders);
		when(carsOrdersRepository.findById(Mockito.anyString())).thenReturn(optionalCarsOrders);
		
		Exception exception = Assertions.assertThrows(OrdersException.class, () -> {
			ordersService.cancelOrder("test");
		});
		Assertions.assertEquals("Your order was already cancelled/completed", exception.getMessage());
	}
	
	@Test
	public void cancelCanceledOrderTest() {
		CarsOrders carsOrders = new CarsOrders();
		carsOrders.setStatus(ORDER_BOOKING_STATUS.CANCELLED.toString());
		Optional<CarsOrders> optionalCarsOrders = Optional.of(carsOrders);
		when(carsOrdersRepository.findById(Mockito.anyString())).thenReturn(optionalCarsOrders);
		
		Exception exception = Assertions.assertThrows(OrdersException.class, () -> {
			ordersService.cancelOrder("test");
		});
		Assertions.assertEquals("Your order was already cancelled/completed", exception.getMessage());
	}
	
	@Test
	public void cancelOrderTest() {
		/*
		 * Date date = new Date(); String curDate = SIMPLE_DATE_FORMAT.format(date);
		 * Calendar cal = Calendar.getInstance();
		 * cal.setTime(DateUtils.getDate(curDate)); cal.add(Calendar.MINUTE, 1); Date
		 * startDate = DateUtils.getDate(SIMPLE_DATE_FORMAT.format(cal.getTime()));
		 * cal.add(Calendar.HOUR, 3); Date endDate =
		 * DateUtils.getDate(SIMPLE_DATE_FORMAT.format(cal.getTime()));
		 */
		
		CarsOrders carsOrders = new CarsOrders();
		carsOrders.setStatus(ORDER_BOOKING_STATUS.CONFIRMED.toString());
		Optional<CarsOrders> optionalCarsOrders = Optional.of(carsOrders);
		when(carsOrdersRepository.findById(Mockito.anyString())).thenReturn(optionalCarsOrders);
		
		when(carsOrdersRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArguments()[0]);
		
		OrderResponseDto actualResponseDto = ordersService.cancelOrder("test");
		
		Assertions.assertEquals("Your order was cancelled successfully", actualResponseDto.getMessage());
	}
}
