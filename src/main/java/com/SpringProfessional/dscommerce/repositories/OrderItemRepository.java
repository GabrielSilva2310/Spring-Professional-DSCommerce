package com.SpringProfessional.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SpringProfessional.dscommerce.entities.OrderItem;
import com.SpringProfessional.dscommerce.entities.OrderItemPK;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK >  {
	
	
}
