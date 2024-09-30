package com.SpringProfessional.dscommerce.services;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.SpringProfessional.dscommerce.ProductFactory;
import com.SpringProfessional.dscommerce.dto.ProductDTO;
import com.SpringProfessional.dscommerce.dto.ProductMinDTO;
import com.SpringProfessional.dscommerce.entities.Product;
import com.SpringProfessional.dscommerce.repositories.ProductRepository;
import com.SpringProfessional.dscommerce.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private Product product;
	private String productName;
	private Long existId, noExistId;
	
	private PageImpl<Product> page;

	
	@BeforeEach
	 void set() throws Exception {
		productName="Playstation 5";
		product=ProductFactory.createProduct(productName);
		existId=1L;
		noExistId=1000L;
		page=new PageImpl<>(List.of(product));
		
		Mockito.when(repository.findById(existId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(noExistId)).thenReturn(Optional.empty());
		Mockito.when(repository.searchByName(any(), (Pageable)any())).thenReturn(page);
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExist() {
		ProductDTO result=service.findById(existId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existId);
		Assertions.assertEquals(result.getName(), product.getName());
		
	}
	
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenDoesNotExistId() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.findById(noExistId);
		});
		
		
	}
	
	@Test
	public void findAllShouldReturnPageProductMinDTO() {
		
		Pageable pageable=PageRequest.of(0, 12);
		
		Page<ProductMinDTO> result=service.findAll(productName, pageable);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getSize(), 1);
		Assertions.assertEquals(result.iterator().next().getName(), productName);
		
		
	}
	
	
	
	

}
