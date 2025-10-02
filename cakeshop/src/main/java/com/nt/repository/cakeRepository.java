package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.model.Cake;

public interface cakeRepository extends JpaRepository<Cake, Long> {

}
