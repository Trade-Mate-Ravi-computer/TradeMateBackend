package com.trademate.project.Service;

import com.trademate.project.Model.SellerModel;
import com.trademate.project.Repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SellerService {
    @Autowired
    private SellerRepository sellerRepository;

    public ResponseEntity<SellerModel> addSale(SellerModel seller){
        return new ResponseEntity<SellerModel>(sellerRepository.save(seller), HttpStatus.CREATED);
    }
    public SellerModel getByName(String name){
        return sellerRepository.findBySellerName(name);
    }
    public SellerModel getByNameAndCompany(String name,String company){
        return sellerRepository.findBySellerNameAndCompanyName(name,company);
    }
}
