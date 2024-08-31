package com.trademate.project.Controller;

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
@CrossOrigin(value = {"http://localhost:3000","https://trade-mate-pearl.vercel.app/","http://ec2-16-171-11-228.eu-north-1.compute.amazonaws.com:8080","http://51.20.69.152/"})public class StockItemController {
    @Autowired
    private StockItemService service;
@Autowired
private StockItemRepository repository;
@Autowired
private CompanyRepository companyRepository;
        @PostMapping("/add")
    public ResponseEntity<StockItemModel> addStock(@RequestBody StockItemModel item){
            item.setItemName(item.getItemName().trim().replaceAll("/","-"));
            item.getCompany().setCompanyId(companyRepository.findByCompanyNameAndEmail(item.getCompanyName(),item.getEmail()).getCompanyId());
        return service.addStock(item);
    }
    @PostMapping("/all")
    public List<StockItemModel> getAllByCompanyName(@RequestBody DateModel date){
        return repository.fingByCompanyName(date.getCompanyName(),date.getEmail());
    }
    @PutMapping("/updateStock")
    public String updateStock(@RequestBody StockItemModel item){
            return service.updateItem(item.getPurchasePrice()!=0? item.getPurchasePrice() : 0,item.getItemName());
    }
}
