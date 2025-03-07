package com.trademate.project.Controller;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.SellerModel;
import com.trademate.project.Repository.CustomerRepository;
import com.trademate.project.Repository.SellerRepository;
import com.trademate.project.Service.CompanyService;
import com.trademate.project.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = {"http://localhost:3000","https://trademate.ravicomputer.online/","https://trade-mate-fr-shadcn.vercel.app/"})
@RequestMapping("/seller")
public class SellerContorller {
    @Autowired
    private SellerService service;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/add")
    public ResponseEntity<SellerModel> addSale(@RequestBody SellerModel seller){
        seller.setCompany(seller.getCompany());
        return service.addSale(seller);
    }
    @GetMapping("/byname/{id}")
    public SellerModel getByName(@PathVariable long  id){
        return service.getBySellerId(id);
    }
    @GetMapping("/all/{companyId}")
    public List<SellerModel> getAll(@PathVariable long companyId) {
        CompanyModel companyModel =new CompanyModel();
         companyModel.setCompanyId(companyId);
         List<SellerModel> sellers =sellerRepository.findByCompany(companyModel);
         for(SellerModel sellerModel :sellers){
             sellerModel.setCompany(null);
         }
         return sellers;
    }
}
