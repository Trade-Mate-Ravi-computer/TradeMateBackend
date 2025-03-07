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
@CrossOrigin(value = {"http://localhost:3000", "https://trademate.ravicomputer.online/", "https://trade-mate-fr-shadcn.vercel.app/"})
public class StockItemController {
    @Autowired
    private StockItemService service;
    @Autowired
    private StockItemRepository repository;
    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping("/add")
    public ResponseEntity<StockItemModel> addStock(@RequestBody StockItemModel item) {
        return service.addStock(item);
    }

    @GetMapping("/all/{companyId}")
    public List<StockItemModel> getAllByCompanyName(@PathVariable long companyId) {
        CompanyModel company = companyRepository.findByCompanyId(companyId);
        List<StockItemModel> stockItems =repository.findByCompany(company);
        for(int i=0;i<stockItems.size();i++){
            stockItems.get(i).setCompany(null);
        }
        return stockItems;
    }

    @PutMapping("/updateStock")
    public String updateStock(@RequestBody StockItemModel item) {
        return service.updateItem(item.getPurchasePrice() != 0 ? (int) item.getPurchasePrice() : 0, item.getItemId(), item.getItemName());
    }

    @GetMapping("/getTop5Items/{companyId}")
    public List<Object[]> getTop5Items(@PathVariable long companyId) {
        return repository.findTop5SoldItemsByCompany(companyId);
    }
}
