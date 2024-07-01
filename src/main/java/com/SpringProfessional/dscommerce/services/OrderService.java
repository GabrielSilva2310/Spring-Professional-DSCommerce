package com.SpringProfessional.dscommerce.services;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SpringProfessional.dscommerce.dto.OrderDTO;
import com.SpringProfessional.dscommerce.dto.OrderItemDTO;
import com.SpringProfessional.dscommerce.entities.Order;
import com.SpringProfessional.dscommerce.entities.OrderItem;
import com.SpringProfessional.dscommerce.entities.Product;
import com.SpringProfessional.dscommerce.entities.User;
import com.SpringProfessional.dscommerce.enums.OrderStatus;
import com.SpringProfessional.dscommerce.repositories.OrderItemRepository;
import com.SpringProfessional.dscommerce.repositories.OrderRepository;
import com.SpringProfessional.dscommerce.repositories.ProductRepository;
import com.SpringProfessional.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private UserService userService;

	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {
		Order order = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado!"));
		return new OrderDTO(order);
	}
	
	@Transactional
	public OrderDTO insert(OrderDTO dto) {
		
		Order order= new Order();
		order.setMoment(Instant.now());
		order.setStatus(OrderStatus.WAITING_PAYMENT);
		
		User user=userService.authenticated();
		order.setClient(user);
		
		for(OrderItemDTO itemDTO: dto.getItems()) {
			Product product=productRepository.getReferenceById(itemDTO.getProductId());
			OrderItem orderItem=new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
			order.getItems().add(orderItem);
		}
		
		repository.save(order);
		orderItemRepository.saveAll(order.getItems());
		
		return new OrderDTO(order);
		
		
	}
	

}
