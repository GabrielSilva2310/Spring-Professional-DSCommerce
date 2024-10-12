package com.SpringProfessional.dscommerce;

import java.time.Instant;

import com.SpringProfessional.dscommerce.entities.Order;
import com.SpringProfessional.dscommerce.entities.OrderItem;
import com.SpringProfessional.dscommerce.entities.Payment;
import com.SpringProfessional.dscommerce.entities.Product;
import com.SpringProfessional.dscommerce.entities.User;
import com.SpringProfessional.dscommerce.enums.OrderStatus;

public class OrderFactory {
	
	public static Order createOrder(User client) {
		
		Order order= new Order(1L, Instant.now(), OrderStatus.DELIVERED, client, new Payment());
		
		Product product=ProductFactory.createProduct();
		OrderItem orderItem=new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		return order;
	
	}

}
