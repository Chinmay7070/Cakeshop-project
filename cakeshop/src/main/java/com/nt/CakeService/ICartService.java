package com.nt.CakeService;

import java.util.List;

import com.nt.model.Cart;

public interface ICartService {

	public void addToCart(Cart cart);
	public List getAllCartItems();
	public List getAllCartItemsWithDetails();
	public void clearCart();
}
