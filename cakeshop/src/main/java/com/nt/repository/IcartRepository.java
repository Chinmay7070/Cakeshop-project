package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.model.Cart;

public interface IcartRepository extends JpaRepository<Cart,Long> {

}
