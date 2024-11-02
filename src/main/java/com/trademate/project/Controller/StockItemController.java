package com.trademate.project.Controller;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.DateModel;
import com.trademate.project.Model.StockItemModel;
import com.trademate.project.Repository.CompanyRepository;
import com.trademate.project.Repository.StockItemRepository;
import com.trademate.project.Service.StockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@CrossOrigin(value = {"http://localhost:3000","https://ravicomputer.online/","https://trade-mate-fr-shadcn.vercel.app/"})
public class StockItemController {
    @Autowired
    private StockItemService service;
@Autowired
private StockItemRepository repository;
@Autowired
private CompanyRepository companyRepository;
        @PostMapping("/add")
    public ResponseEntity<StockItemModel> addStock(@RequestBody StockItemModel item){
        return service.addStock(item);
    }
    @GetMapping("/all/{companyId}")
    public List<StockItemModel> getAllByCompanyName(@PathVariable long companyId){
            CompanyModel company=companyRepository.findByCompanyId(companyId);
        return repository.findByCompany(company);
    }
    @PutMapping("/updateStock")
    public String updateStock(@RequestBody StockItemModel item){
            return service.updateItem(item.getPurchasePrice()!=0? item.getPurchasePrice() : 0,item.getItemName());
    }
}
