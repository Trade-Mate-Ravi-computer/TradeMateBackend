package com.trademate.project.Controller;

import com.trademate.project.Model.SellerModel;
import com.trademate.project.Repository.SellerRepository;
import com.trademate.project.Service.CompanyService;
import com.trademate.project.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = {"http://localhost:3000","https://ravicomputer.online/","http://13.60.161.70/"})
@RequestMapping("/seller")
public class SellerContorller {
    @Autowired
    private SellerService service;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private SellerRepository sellerRepository;

    @PostMapping("/add")
    public ResponseEntity<SellerModel> addSale(@RequestBody SellerModel seller){
        seller.getCompany().setCompanyId(companyService.getByCompanyNameAndEmail(seller.getCompanyName(),seller.getEmail()).getCompanyId());
        return service.addSale(seller);
    }
    @GetMapping("/byname/{name}")
    public SellerModel getByName(@PathVariable String name){
        return service.getByName(name);
    }
    @GetMapping("/all")
    public List<SellerModel> getAll() {
        return sellerRepository.findAll();
    }
}
