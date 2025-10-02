package com.nt.CakeService;


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

}
