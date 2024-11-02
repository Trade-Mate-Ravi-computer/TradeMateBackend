package com.trademate.project.Controller;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.PurchaseModel;
import com.trademate.project.Model.StockItemModel;
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
    @PostMapping("/add")
    public ResponseEntity<PurchaseModel> addPurchase(@RequestBody PurchaseModel purchase){
            return purchaseService.addPurchase(purchase);
    }
    @PostMapping("/getbycompany")
    public List<PurchaseModel> getByCompany(@RequestBody CompanyModel company){
        return purchaseRepository.findAllByCompany(company);
    }
    @PostMapping("/update")
    public String updateById(@RequestBody PurchaseModel purchase){
         PurchaseModel existingPurchase = purchaseService.getById(purchase.getId());
             existingPurchase.setPaidAmmount(existingPurchase.getPaidAmmount()+purchase.getPaidAmmount());
             existingPurchase.setRemaining(existingPurchase.getRemaining()-purchase.getPaidAmmount());
             purchaseRepository.save(existingPurchase);
             return "Updated";
    }
}
