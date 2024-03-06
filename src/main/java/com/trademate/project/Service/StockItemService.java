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
        return new ResponseEntity<StockItemModel>(stockItemRepository.save(item), HttpStatus.CREATED);
    }
    public List<StockItemModel> getAll(){
        return stockItemRepository.findAll();
    }
    public StockItemModel getByName(String item){
        return stockItemRepository.findByItemName(item);
    }
    public String updateItem(int price,String itemName ){
        StockItemModel existingItem = stockItemRepository.findByItemName(itemName);
        existingItem.setPurchasePrice(price);
        stockItemRepository.save(existingItem);
        return "Updated";
    }
    public void updateQuantity(int quantity, String itemName) {
        StockItemModel existingItem = stockItemRepository.findByItemName(itemName);
        int newQuantity = existingItem.getQuantity() + quantity;
        existingItem.setQuantity(newQuantity); // Add quantity to existing quantity
        System.out.println("Hello"+newQuantity);
        stockItemRepository.save(existingItem);
    }
    public void updateSaleQuantity(int quantity, String itemName) {
        StockItemModel existingItem = stockItemRepository.findByItemName(itemName);
        int newQuantity = existingItem.getQuantity()-quantity;
        existingItem.setQuantity(newQuantity); // Add quantity to existing quantity
        stockItemRepository.save(existingItem);
    }

}
