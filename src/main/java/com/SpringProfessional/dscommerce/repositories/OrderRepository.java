package com.SpringProfessional.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SpringProfessional.dscommerce.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long >  {
	
	
}
