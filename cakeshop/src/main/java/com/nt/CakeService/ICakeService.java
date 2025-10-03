package com.nt.CakeService;

import java.util.List;

import com.nt.model.Cake;

public interface ICakeService {

    public List<Cake> getAllCakes();
    Cake getCakeById(Long id);  
    public void saveCake(Cake cake);
   public void deleteCake(Long id);
}
