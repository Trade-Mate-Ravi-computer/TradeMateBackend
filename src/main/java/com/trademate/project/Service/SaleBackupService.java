package com.trademate.project.Service;

import com.trademate.project.Model.SaleBackupModel;
import com.trademate.project.Model.SaleModel;
import com.trademate.project.Repository.SaleBackupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SaleBackupService {
    @Autowired
    private SaleBackupRepository saleBackupRepository;



    public ResponseEntity<SaleBackupModel> add(SaleModel saleModel){
        SaleBackupModel sale =new SaleBackupModel();
        sale.setCompany(saleModel.getCompany());
        sale.setCustomer(saleModel.getCustomer());
        sale.setDate(saleModel.getDate());
        sale.setEmail(saleModel.getEmail());
        sale.setItem(saleModel.getItem());
        sale.setCustomerName(saleModel.getCustomerName());
        sale.setCompanyName(saleModel.getCompanyName());
        sale.setGstInRupee(saleModel.getGstInRupee());
        sale.setQuantity(saleModel.getQuantity());
        sale.setRate(saleModel.getRate());
        sale.setProfit(saleModel.getProfit());
        sale.setRemaining(saleModel.getRemaining());
        sale.setReceivedAmmount(saleModel.getReceivedAmmount());
        sale.setProfit(saleModel.getProfit());
        sale.setTotalAmmount(saleModel.getTotalAmmount());
        return new ResponseEntity<SaleBackupModel>( saleBackupRepository.save(sale), HttpStatus.CREATED);
    }
}
