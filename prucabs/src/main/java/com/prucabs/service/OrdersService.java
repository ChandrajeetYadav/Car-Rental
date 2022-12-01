package com.prucabs.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.prucabs.constant.CarsCharges;
import com.prucabs.constant.ORDER_BOOKING_STATUS;
import com.prucabs.constant.PAYMENT_MODE;
import com.prucabs.constant.TRANSACTION_STATUS;
import com.prucabs.dto.OrderDto;
import com.prucabs.dto.OrderPatchDto;
import com.prucabs.dto.OrderResponseDto;
import com.prucabs.entity.Cars;
import com.prucabs.entity.CarsOrders;
import com.prucabs.entity.TransactionDetail;
import com.prucabs.exception.BadRequestException;
import com.prucabs.exception.CarNotAvailableException;
import com.prucabs.exception.OrdersException;
import com.prucabs.repository.CarsOrdersRepository;
import com.prucabs.repository.CarsRepository;
import com.prucabs.repository.TransactionRepository;
import com.prucabs.utils.DateUtils;
import com.prucabs.utils.Utils;

@Service
public class OrdersService {
	
	@Autowired
	private CarsRepository carsRepository;
	
	@Autowired
	private CarsOrdersRepository carsOrdersRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;

	public OrderResponseDto createOrder(OrderDto orderDto) {
		validateOrderRequest(orderDto);
		if (isCarBooked(orderDto))
			throw new CarNotAvailableException("Car is not available for booking");
		
		CarsOrders carsOrders = placeOrder(orderDto);
		return buildOrderResponseDto(carsOrders);
	}
	
	public OrderResponseDto getOrderById(String orderId) {
		CarsOrders carsOrders = getOrderByIdFromDb(orderId);
		return buildOrderResponseDto(carsOrders);
	}
	
	public OrderResponseDto cancelOrder(String orderId) {
		CarsOrders carsOrders = getOrderByIdFromDb(orderId);
		if (carsOrders.getStatus().equals(ORDER_BOOKING_STATUS.CANCELLED.toString()) || carsOrders.getStatus().equals(ORDER_BOOKING_STATUS.COMPLETED.toString()))
			throw new OrdersException("Your order was already cancelled/completed");
		carsOrders.setStatus(ORDER_BOOKING_STATUS.CANCELLED.toString());
		carsOrdersRepository.save(carsOrders);
		createTransaction(carsOrders);
		OrderResponseDto orderResponseDto = new OrderResponseDto();
		orderResponseDto.setMessage("Your order was cancelled successfully");
		return orderResponseDto;
	}
	
	public OrderResponseDto updateOrder(String orderId, OrderPatchDto orderPatchDto) {
		CarsOrders carsOrders = validateUpdateRequest(orderId, orderPatchDto);
		carsOrdersRepository.save(carsOrders);
		OrderResponseDto orderResponseDto = new OrderResponseDto();
		orderResponseDto.setMessage("Your order was updated successfully");
		return orderResponseDto;
	}
	
	public OrderResponseDto completeOrder(String orderId) {
		CarsOrders carsOrders = getOrderByIdFromDb(orderId);
		if (carsOrders.getStatus().equals(ORDER_BOOKING_STATUS.CANCELLED.toString()) || carsOrders.getStatus().equals(ORDER_BOOKING_STATUS.COMPLETED.toString()))
			throw new OrdersException("Your order was already cancelled/completed");
		carsOrders.setStatus(ORDER_BOOKING_STATUS.COMPLETED.toString());
		carsOrdersRepository.save(carsOrders);
		TransactionDetail transactionDetail = createTransaction(carsOrders);
		OrderResponseDto orderResponseDto = new OrderResponseDto();
		orderResponseDto.setMessage("Your order was completed successfully and your final cost is: " + transactionDetail.getCurrency() + " " + transactionDetail.getAmount());
		return orderResponseDto;
	}
	
	private Cars validateOrderRequest(OrderDto orderDto) {
		Date curDate = new Date();
		long currentDate = curDate.getTime();
		
		Date startDate = DateUtils.getDate(orderDto.getStartDate());
		Date endDate = DateUtils.getDate(orderDto.getEndDate());
		if (startDate.getTime() < currentDate || endDate.getTime() <= startDate.getTime())
			throw new BadRequestException("Invalid start/end date.");
		long diffInDays = DateUtils.getTimeDifferenceInNumberOfDays(curDate, startDate);
		if (diffInDays > 30)
			throw new OrdersException("Car can be booked within next 30 days");
		diffInDays = DateUtils.getTimeDifferenceInNumberOfDays(startDate, endDate);
		if (diffInDays > 30)
			throw new OrdersException("Car booking is allowed only for 30 days.");
		Optional<Cars> cars = carsRepository.findById(orderDto.getCarId());
		if (cars.isEmpty())
			throw new BadRequestException("Invalid Car Id");
		return cars.get();
	}
	
	private CarsOrders placeOrder(OrderDto orderDto) {
		CarsOrders carsOrders = new CarsOrders();
		carsOrders.setOrderId(UUID.randomUUID().toString());
		carsOrders.setCarId(orderDto.getCarId());
		carsOrders.setStartDate(DateUtils.getDate(orderDto.getStartDate()));
		carsOrders.setEndDate(DateUtils.getDate(orderDto.getEndDate()));
		carsOrders.setStatus(ORDER_BOOKING_STATUS.CONFIRMED.toString());
		carsOrders.setCustomerName(orderDto.getCustomerName());
		carsOrders.setCustomerEmail(orderDto.getCustomerEmail());
		carsOrders.setCustomerPhoneNumber(orderDto.getCustomerPhoneNumber());
		return carsOrdersRepository.save(carsOrders);
	}
	
	private CarsOrders getOrderByIdFromDb(String orderId) {
		if (Utils.isEmpty(orderId))
			throw new BadRequestException("Order id is missing or null!");
		Optional<CarsOrders> carsOrders = carsOrdersRepository.findById(orderId);
		if (!carsOrders.isPresent())
			throw new BadRequestException("Invalid order id");
		return carsOrders.get();
	}
	
	private boolean isCarBooked(OrderDto orderDto) {
		Date startDate = DateUtils.getDate(orderDto.getStartDate());
		Date endDate = DateUtils.getDate(orderDto.getEndDate());
		String status = ORDER_BOOKING_STATUS.CONFIRMED.toString();
		int bookingCount = carsOrdersRepository.getExistingCarBookings(orderDto.getCarId(), status, startDate, endDate);
		return bookingCount > 0;
	}
	
	
	private OrderResponseDto buildOrderResponseDto(CarsOrders carsOrders) {
		OrderResponseDto orderResponseDto = new OrderResponseDto();
		orderResponseDto.setOrderId(carsOrders.getOrderId());
		orderResponseDto.setOrderStatus(carsOrders.getStatus().toString());
		orderResponseDto.setStatusCode(HttpStatus.CREATED.value());
		orderResponseDto.setStartDate(DateUtils.getDateString(carsOrders.getStartDate()));
		orderResponseDto.setEndDate(DateUtils.getDateString(carsOrders.getEndDate()));
		orderResponseDto.setCustomerName(carsOrders.getCustomerName());
		orderResponseDto.setCustomerEmail(carsOrders.getCustomerEmail());
		orderResponseDto.setCustomerPhoneNumber(carsOrders.getCustomerPhoneNumber());
		orderResponseDto.setCustomerId(carsOrders.getCustomerId());
		return orderResponseDto;
	}
	
	private CarsOrders validateUpdateRequest(String orderId, OrderPatchDto orderPatchDto) {
		CarsOrders carsOrders = getOrderByIdFromDb(orderId);
		if (carsOrders.getStatus().equals(ORDER_BOOKING_STATUS.CANCELLED.toString()) || carsOrders.getStatus().equals(ORDER_BOOKING_STATUS.COMPLETED.toString()))
			throw new OrdersException("Your order was completed/cancelled");
		boolean isChanged = false;
		if (!Utils.isEmpty(orderPatchDto.getCustomerName())) {
			carsOrders.setCustomerName(orderPatchDto.getCustomerName());
			isChanged = true;
		}
		if (!Utils.isEmpty(orderPatchDto.getCustomerEmail())) {
			carsOrders.setCustomerEmail(orderPatchDto.getCustomerEmail());
			isChanged = true;
		}
		if (!Utils.isEmpty(orderPatchDto.getCustomerPhoneNumber())) {
			carsOrders.setCustomerPhoneNumber(orderPatchDto.getCustomerPhoneNumber());
			isChanged = true;
		}
		if (!isChanged)
			throw new BadRequestException("Details to be updated are missing");
		return carsOrders;
	}
	
	private TransactionDetail createTransaction(CarsOrders carsOrders) {
		TransactionDetail transactionDetail = new TransactionDetail();
		transactionDetail.setTransactionId(UUID.randomUUID().toString());
		transactionDetail.setOrderId(carsOrders.getOrderId());
		transactionDetail.setCurrency("INR");
		
		transactionDetail.setTransactionDate(new Date());
		if (carsOrders.getStatus().equals(ORDER_BOOKING_STATUS.COMPLETED.toString())) {
			transactionDetail.setAmount(getOrderPrice(carsOrders));
			transactionDetail.setStatus(TRANSACTION_STATUS.COMPLETED.toString());
		} else if (carsOrders.getStatus().equals(ORDER_BOOKING_STATUS.CANCELLED.toString())) {
			transactionDetail.setAmount(0.0);
			transactionDetail.setStatus(TRANSACTION_STATUS.CANCELLED.toString());
		}
		transactionDetail.setPaymentMode(PAYMENT_MODE.CASH.toString());
		transactionRepository.save(transactionDetail);
		return transactionDetail;
	}
	
	private double getOrderPrice(CarsOrders carsOrders) {
		long diffInMinutes = DateUtils.getTimeDifferenceInNumberOfMinutes(carsOrders.getStartDate(), carsOrders.getEndDate());
		Cars cars = carsRepository.findById(carsOrders.getCarId()).get();
		String model = cars.getManufacturer() + "_" + cars.getModel();
		return CarsCharges.getPrice(model) * (diffInMinutes / 60.0);
	}
}
