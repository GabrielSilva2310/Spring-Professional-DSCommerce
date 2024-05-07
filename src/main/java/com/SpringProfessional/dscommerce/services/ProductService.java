package com.SpringProfessional.dscommerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SpringProfessional.dscommerce.dto.ProductDTO;
import com.SpringProfessional.dscommerce.entities.Product;
import com.SpringProfessional.dscommerce.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	@Transactional(readOnly = true)
	public ProductDTO findById(Integer id) {
		Product product = repository.findById(id).get();
		return new ProductDTO(product);
	}

}
