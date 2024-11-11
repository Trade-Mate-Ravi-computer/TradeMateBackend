package com.trademate.project.Controller;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.PurchaseModel;
import com.trademate.project.Model.StockItemModel;
import com.trademate.project.Repository.CompanyRepository;
import com.trademate.project.Repository.PurchaseRepository;
import com.trademate.project.Service.CompanyService;
import com.trademate.project.Service.PurchaseService;
import com.trademate.project.Service.SellerService;
import com.trademate.project.Service.StockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
@CrossOrigin(value = {"http://localhost:3000","https://ravicomputer.online/","https://trade-mate-fr-shadcn.vercel.app/"})
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;
@Autowired
private CompanyService companyService;
@Autowired
private StockItemService stockItemService;
@Autowired
private PurchaseRepository purchaseRepository;
@Autowired
private SellerService sellerService;
@Autowired
private CompanyRepository companyRepository;
    @PostMapping("/add")
    public ResponseEntity<PurchaseModel> addPurchase(@RequestBody PurchaseModel purchase){
        purchase.setTotalAmount(purchase.getQuantity()*purchase.getPrice());
        purchase.setRemaining(purchase.getTotalAmount()-purchase.getPaidAmount());
        purchase.setGstInRupee((double) (purchase.getTotalAmount() * 18) /100);
            return purchaseService.addPurchase(purchase);
    }
    @GetMapping("/getbycompany/{companyId}")
    public List<PurchaseModel> getByCompany(@PathVariable long companyId){
        CompanyModel company =companyRepository.findByCompanyId(companyId);
        return purchaseRepository.findAllByCompany(company);
    }
    @PostMapping("/update")
    public String updateById(@RequestBody PurchaseModel purchase){
         PurchaseModel existingPurchase = purchaseService.getById(purchase.getId());
             existingPurchase.setPaidAmount(existingPurchase.getPaidAmount()+purchase.getPaidAmount());
             existingPurchase.setRemaining(existingPurchase.getRemaining()-purchase.getPaidAmount());
             purchaseRepository.save(existingPurchase);
             return "Updated";
    }
}
