package com.SpringProfessional.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SpringProfessional.dscommerce.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	
	
}
