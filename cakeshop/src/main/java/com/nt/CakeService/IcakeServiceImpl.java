package com.nt.CakeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.model.Cake;
import com.nt.repository.cakeRepository;

@Service
public class IcakeServiceImpl implements ICakeService {

	@Autowired
	private cakeRepository cakerepo;
	
	public List<Cake> getAllCakes(){
		return cakerepo.findAll();
	}

	@Override
	public Cake getCakeById(Long id) {
		return cakerepo.findById(id).orElse(null);
	}

	public void saveCake(Cake cake) {
		
		cakerepo.save(cake);
	}

	@Override
	public void deleteCake(Long id) {
		cakerepo.deleteById(id);
		
	}

}
