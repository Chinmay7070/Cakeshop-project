package com.nt.CakeService;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.model.Order;
import com.nt.repository.IOderRepository;

@Service
public class IOrderImpl implements IOderService
{

	 @Autowired
     private IOderRepository orepo;
	 
	@Override
	public void saveOrder(Order order) {
		
         orepo.save(order);
		
	}

	public List<Order> getAllOrders() {
		  return orepo.findAll();
	}
	
		
	

}
