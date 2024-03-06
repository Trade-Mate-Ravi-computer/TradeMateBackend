package com.trademate.project.Controller;

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
@CrossOrigin(value = {"http://localhost:3000","https://trade-mate-pearl.vercel.app/"})
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
            stockItemService.updateItem(purchase.getPrice(),purchase.getItemName());
            stockItemService.updateQuantity(purchase.getQuantity(),purchase.getItemName());
            purchase.setTotalAmmount(purchase.getPrice()*purchase.getQuantity());
            purchase.setGstInRupee((float)purchase.getTotalAmmount()*stockItemService.getByName(purchase.getItemName()).getGstInPercent()/(100+stockItemService.getByName(purchase.getItemName()).getGstInPercent()));
            purchase.setRemaining((purchase.getPrice()*purchase.getQuantity())-purchase.getPaidAmmount());
            purchase.getCompany().setCompanyId(companyService.getByCompanyNameAndEmail(purchase.getCompanyName(),purchase.getEmail()).getCompanyId());
            purchase.getItem().setItemName(purchase.getItemName());
            purchase.getSeller().setId(sellerService.getByNameAndCompany(purchase.getSellerName(),purchase.getCompanyName()).getId());
            return purchaseService.addPurchase(purchase);
    }
    @PostMapping("/getbycompany")
    public List<PurchaseModel> getByCompany(@RequestBody PurchaseModel purchase){
        return purchaseService.getByCompanyName(purchase.getCompanyName());
    }
    @PostMapping("/update")
    public String updateById(@RequestBody PurchaseModel purchase){
        System.out.println(purchase.getPaidAmmount());
        System.out.println(purchase.getId());
PurchaseModel existingPurchase = purchaseService.getById(purchase.getId());
existingPurchase.setPaidAmmount(existingPurchase.getPaidAmmount()+purchase.getPaidAmmount());
existingPurchase.setRemaining(existingPurchase.getRemaining()-purchase.getPaidAmmount());
purchaseRepository.save(existingPurchase);
return "Updated";
    }
}
