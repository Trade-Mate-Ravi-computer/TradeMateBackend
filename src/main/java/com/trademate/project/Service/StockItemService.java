package com.trademate.project.Service;

import com.trademate.project.Model.StockItemModel;
import com.trademate.project.Repository.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockItemService {
    @Autowired
    private StockItemRepository stockItemRepository;
    @Autowired
    private CompanyService companyService;

    public ResponseEntity<StockItemModel> addStock(StockItemModel item){
        item.getCompany().setCompanyId(companyService.getByName(item.getCompanyName()).getCompanyId());
        return new ResponseEntity<StockItemModel>(stockItemRepository.save(item), HttpStatus.CREATED);
    }
    public List<StockItemModel> getAll(){
        return stockItemRepository.findAll();
    }
    public StockItemModel getByName(StockItemModel item){
        return stockItemRepository.findByItemName(item.getItemName());
    }
    public String updateItem(int price,String itemName ){
        StockItemModel existingItem = stockItemRepository.findByItemName(itemName);
        System.out.println(itemName);
        existingItem.setPurchasePrice(price);
        stockItemRepository.save(existingItem);
        return "Updated";
    }
}
