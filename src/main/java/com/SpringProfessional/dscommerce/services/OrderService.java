package com.SpringProfessional.dscommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SpringProfessional.dscommerce.dto.OrderDTO;
import com.SpringProfessional.dscommerce.entities.Order;
import com.SpringProfessional.dscommerce.repositories.OrderRepository;
import com.SpringProfessional.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;

	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {
		Order order = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado!"));
		return new OrderDTO(order);
	}


}
