package com.trademate.project.Service;

import com.trademate.project.Model.MonthYearModel;
import com.trademate.project.Model.QuarterMonthModel;
import com.trademate.project.Model.SaleModel;
import com.trademate.project.Repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {
    private SaleRepository saleRepository;
    @Autowired
    private StockItemService stockItemService;
    @Autowired
    private CompanyService companyService;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }


    public SaleModel addSale(SaleModel saleModel){
        saleModel.setTotalAmmount(saleModel.getQuantity()*saleModel.getRate());
        saleModel.setRemaining(saleModel.getTotalAmmount()-saleModel.getReceivedAmmount());
       if(companyService.getByCompanyNameAndEmail(saleModel.getCompanyName(),saleModel.getEmail()).getGstType()=="Regular"){
           float gst= (saleModel.getTotalAmmount()-((float)saleModel.getTotalAmmount()*100)/(100+stockItemService.getByName(saleModel.getItemName()).getGstInPercent()));
           saleModel.setGstInRupee(gst);
       }else{
           saleModel.setGstInRupee((float)saleModel.getTotalAmmount()/100);
       }
        int pr = saleModel.getTotalAmmount()-saleModel.getQuantity()*(stockItemService.getByName(saleModel.getItemName()).getPurchasePrice());
        saleModel.setProfit(pr);
        return saleRepository.save(saleModel);
    }
    public List<SaleModel> allSale(){
        return saleRepository.findAll();
    }
    public SaleModel getSaleById(long id){
        return saleRepository.findById(id).orElseThrow();
    }
    public String updateSales(SaleModel newSale,long id){
        saleRepository.findById(id).map(sale->{
            sale.setReceivedAmmount(sale.getReceivedAmmount()+newSale.getReceivedAmmount());
            sale.setRemaining(sale.getRemaining()-newSale.getReceivedAmmount());
            return saleRepository.save(sale);
        }).orElseThrow(()->new UsernameNotFoundException("User not Found "));
        return "Updated";
    }
    public String deleteSale(long id){
        saleRepository.deleteById(id);
        return  "Deleted";
    }
    public Object sumOfProfits(int month,int year,String companyName,String email){
        return saleRepository.sumOfRemainingByMonth(month,year,companyName,email);
    }
    public List<SaleModel> getByCustomerName(String customerName){
        return saleRepository.findByCustomerName(customerName);
    }
    public int getRemainingByCustomer(String customerName){
        return saleRepository.sumOfTotalRemainingByCustomer(customerName);
    }
    public List<SaleModel> salesWithRemainingBalance(){
        return saleRepository.salesWithRemainingBalance();
    }
    public List<SaleModel> grtById(long id){
       SaleModel sale = saleRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("Not Found"));
        return saleRepository.findByNameAndDate(sale.getCustomerName(),sale.getDate(),sale.getEmail());
    }
    public  int totalAmmountOfQuarter(QuarterMonthModel monthModel){
        return saleRepository.sumOfTotalAmountOfQuarter(monthModel.getMonth1(),monthModel.getMonth2(),monthModel.getMonth3(),monthModel.getYear(),monthModel.getCompanyName(),monthModel.getEmail());
    }
    public int totalAmmountOfMonth(MonthYearModel monthModel){
        return saleRepository.sumOfTotalAmountOfMonth(monthModel.getMonth(),monthModel.getYear(),monthModel.getCompanyName(),monthModel.getEmail());
    }
}
