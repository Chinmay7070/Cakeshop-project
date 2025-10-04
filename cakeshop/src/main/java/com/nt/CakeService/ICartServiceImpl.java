package com.nt.CakeService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.model.Cake;
import com.nt.model.Cart;
import com.nt.repository.IcartRepository;
@Service
public class ICartServiceImpl implements ICartService {

	   @Autowired
	    private ICakeService cakeService;
	@Autowired
	private IcartRepository cartrepo;
	@Override
	public void addToCart(Cart cart) {
		
		cartrepo.save(cart);

	}

	@Override
	public List getAllCartItems() {
		return cartrepo.findAll();
	}
	 
	  public List getAllCartItemsWithDetails() {
	        List cartItems = cartrepo.findAll();
	        List cartDetails = new ArrayList();
	        for (int i = 0; i < cartItems.size(); i++) {
	            Cart cart = (Cart) cartItems.get(i);
	            Cake cake = cakeService.getCakeById(cart.getCakeId());
	            if (cake != null) {
	                CartDetail cartDetail = new CartDetail();
	                cartDetail.setCartId(cart.getId());
	                cartDetail.setCakeId(cart.getCakeId());
	                cartDetail.setQuantity(cart.getQuantity());
	                cartDetail.setCakeName(cake.getName());
	                cartDetail.setCakePrice(cake.getPrice());
	                cartDetail.setCakeImage_path(cake.getImage_path());
	                cartDetails.add(cartDetail);
	            }
	        }
	        return cartDetails;
	    }

	  @Override
	  public void clearCart() {
		// TODO Auto-generated method stub
		  cartrepo.deleteAll();
	  }
}
