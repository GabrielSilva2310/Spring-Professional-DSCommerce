package com.SpringProfessional.dscommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.SpringProfessional.dscommerce.OrderFactory;
import com.SpringProfessional.dscommerce.UserFactory;
import com.SpringProfessional.dscommerce.dto.OrderDTO;
import com.SpringProfessional.dscommerce.entities.Order;
import com.SpringProfessional.dscommerce.entities.User;
import com.SpringProfessional.dscommerce.repositories.OrderRepository;
import com.SpringProfessional.dscommerce.services.exceptions.ForbbidenException;
import com.SpringProfessional.dscommerce.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {
	
	@InjectMocks
	private OrderService service;
	
	@Mock 
	private OrderRepository repository;
	
	@Mock
	private AuthService authService;
	
	private Long existId, noExistId;
	
	private Order order;
	private OrderDTO orderDTO;
	
	private User admin,client;

	
	@BeforeEach
	void set() throws Exception {
		existId=1L;
		noExistId=2L;
		
		admin=UserFactory.createCustomAdminUser(1L, "Jef");
		client=UserFactory.createCustomClientUser(2L, "Bob");
		
		order=OrderFactory.createOrder(client);
		
		orderDTO=new OrderDTO(order);
		
		Mockito.when(repository.findById(existId)).thenReturn(Optional.of(order));
		Mockito.when(repository.findById(noExistId)).thenReturn(Optional.empty());

	}
	
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistAndAdminLogged() {
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
		
		OrderDTO result=service.findById(existId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existId);
		
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenIdExistAndSelfClientLogged() {
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());
		
		OrderDTO result=service.findById(existId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existId);
		
	}
	
	@Test
	public void findByIdShouldThrowForbbidenExceptionWhenIdExistAndOtherClientLogged() {
		Mockito.doThrow(ForbbidenException.class).when(authService).validateSelfOrAdmin(any());
		
		Assertions.assertThrows(ForbbidenException.class, ()->{
			OrderDTO result=service.findById(existId);
		});
		
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenDoesNotExistId() {
		
		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

		
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.findById(noExistId);
		});
		
	}
	
}
