package com.SpringProfessional.dscommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SpringProfessional.dscommerce.dto.CategoryDTO;
import com.SpringProfessional.dscommerce.dto.ProductMinDTO;
import com.SpringProfessional.dscommerce.entities.Category;
import com.SpringProfessional.dscommerce.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> result = repository.findAll();
		return result.stream().map(x-> new CategoryDTO(x)).collect(Collectors.toList());
	}

}
	
	
