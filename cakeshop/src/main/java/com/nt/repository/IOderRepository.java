package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.model.Order;

@Repository
public interface IOderRepository extends JpaRepository<Order,Long>{

	
	
}
