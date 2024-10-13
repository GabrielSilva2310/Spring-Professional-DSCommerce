package com.SpringProfessional.dscommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.SpringProfessional.dscommerce.OrderFactory;
import com.SpringProfessional.dscommerce.ProductFactory;
import com.SpringProfessional.dscommerce.UserFactory;
import com.SpringProfessional.dscommerce.dto.OrderDTO;
import com.SpringProfessional.dscommerce.entities.Order;
import com.SpringProfessional.dscommerce.entities.OrderItem;
import com.SpringProfessional.dscommerce.entities.Product;
import com.SpringProfessional.dscommerce.entities.User;
import com.SpringProfessional.dscommerce.repositories.OrderItemRepository;
import com.SpringProfessional.dscommerce.repositories.OrderRepository;
import com.SpringProfessional.dscommerce.repositories.ProductRepository;
import com.SpringProfessional.dscommerce.services.exceptions.ForbbidenException;
import com.SpringProfessional.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {
	
	@InjectMocks
	private OrderService service;
	
	@Mock 
	private OrderRepository repository;
	
	@Mock
	private AuthService authService;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private OrderItemRepository orderItemRepository;
	
	@Mock
	private UserService userService;
	
	private Long existId, noExistId;
	private Long existProductId, noExistProductId; 
	private Order order;
	private OrderDTO orderDTO;
	private Product product;
	
	private User admin,client;

	
	@BeforeEach
	void set() throws Exception {
		existId=1L;
		noExistId=2L;
		
		existProductId=1L;
		noExistProductId=2L;
		
		admin=UserFactory.createCustomAdminUser(1L, "Jef");
		client=UserFactory.createCustomClientUser(2L, "Bob");
		
		order=OrderFactory.createOrder(client);
		
		orderDTO=new OrderDTO(order);
		
		product=ProductFactory.createProduct();
		
		Mockito.when(repository.findById(existId)).thenReturn(Optional.of(order));
		Mockito.when(repository.findById(noExistId)).thenReturn(Optional.empty());
		
		Mockito.when(productRepository.getReferenceById(existProductId)).thenReturn(product);
		Mockito.when(productRepository.getReferenceById(noExistProductId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(repository.save(any())).thenReturn(order);
		
		Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));
		
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
	
	@Test
	public void insertShouldReturnOrderDTOWhenAdminLogged() {
		Mockito.when(userService.authenticated()).thenReturn(admin);
		
		OrderDTO result=service.insert(orderDTO);
		
		Assertions.assertNotNull(result);
	
	}
	
	@Test
	public void insertShouldReturnOrderDTOWhenClientLogged() {
		Mockito.when(userService.authenticated()).thenReturn(client);
		
		OrderDTO result=service.insert(orderDTO);
		
		Assertions.assertNotNull(result);
	
	}
	
	@Test
	public void insertShouldThrowUsernameNotFoundExceptionWhenUserNotLogged() {
		Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();
		
		order.setClient(new User());
		orderDTO=new OrderDTO(order);
		
		Assertions.assertThrows(UsernameNotFoundException.class, ()->{
			@SuppressWarnings("unused")
			OrderDTO result=service.insert(orderDTO);
		});
		
	}
	
	@Test
	public void insertShouldThrowEntityNotFoundExceptionWhenDoesNotExistOrderProductId() {
		Mockito.when(userService.authenticated()).thenReturn(client);
		
		product.setId(noExistProductId);
		OrderItem orderItem=new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		
		orderDTO=new OrderDTO(order);
		
		Assertions.assertThrows(EntityNotFoundException.class, ()->{
			@SuppressWarnings("unused")
			OrderDTO result=service.insert(orderDTO);
		});
		
		
	}
	
}
