package com.SpringProfessional.dscommerce;

import com.SpringProfessional.dscommerce.entities.Category;
import com.SpringProfessional.dscommerce.entities.Product;

public class ProductFactory {
	
	
	public static Product createProduct() {
		Category category=CategoryFactory.createCategory();
		Product product=new Product(1L, "PS5", "Video Game", 4000.00, "http//Sony/PS5");
		product.getCategories().add(category);
		return product;
		
	}
	

	public static Product createProduct(String name) {
		Product product=createProduct();
		product.setName(name);
		return product;
	}
	

}
