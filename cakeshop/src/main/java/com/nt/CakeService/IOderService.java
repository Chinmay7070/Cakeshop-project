package com.nt.CakeService;

import java.util.List;

import com.nt.model.Order;

public interface IOderService {
    public void saveOrder(Order order);
  
   public List<Order> getAllOrders();
}
