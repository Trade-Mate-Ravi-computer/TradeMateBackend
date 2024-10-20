package com.trademate.project.Service;

import com.trademate.project.Model.*;
import com.trademate.project.Repository.CompanyRepository;
import com.trademate.project.Repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class SaleService {
    private SaleRepository saleRepository;
    @Autowired
    private StockItemService stockItemService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private RecevivedMoneyService recevivedMoneyService;
    @Autowired
    private CompanyRepository companyRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }


    public SaleModel addSale(SaleModel saleModel){
        return saleRepository.save(saleModel);
    }
    public List<SaleModel> allSale(){
        return saleRepository.findAll();
    }
    public SaleModel getSaleById(long id){
        return saleRepository.findById(id).orElseThrow();
    }
//    public String updateSales(SaleModel newSale,long id){
//        saleRepository.findById(id).map(sale->{
//            ReceivedMoneyModel receivedMoneyModel = new ReceivedMoneyModel();
//receivedMoneyModel.setDate(LocalDate.now());
//receivedMoneyModel.setAmount(newSale.getReceivedAmmount());
//receivedMoneyModel.setCustomerName(sale.getCustomerName());
//recevivedMoneyService.addMoney(receivedMoneyModel);
//            sale.setReceivedAmmount(sale.getReceivedAmmount()+newSale.getReceivedAmmount());
//            sale.setRemaining(sale.getRemaining()-newSale.getReceivedAmmount());
//            return saleRepository.save(sale);
//        }).orElseThrow(()->new UsernameNotFoundException("User not Found "));
//        return "Updated";
//    }
    public String deleteSale(long id){
        saleRepository.deleteById(id);
        return  "Deleted";
    }
    public Object sumOfProfits(int month,int year,long companyId){
        CompanyModel company =new CompanyModel();
        company.setCompanyId(companyId);
        return saleRepository.sumOfRemainingByMonth(month,year,company);
    }
    public List<SaleModel> getByCustomerName(CustomerModel customerName){
        return saleRepository.findByCustomer(customerName);
    }
    public int getRemainingByCustomer(CustomerModel  customer){
//        return saleRepository.sumOfTotalRemainingCustomer(customer);
        return 10;
    }
    public List<SaleModel> salesWithRemainingBalance(){
        return saleRepository.salesWithRemainingBalance();
    }
    public List<SaleModel> grtById(long id){
//       SaleModel sale = saleRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("Not Found"));
//        return saleRepository.findByNameAndDate(sale.getCustomerName(),sale.getDate(),sale.getEmail());r
        return null;
    }
    public  int totalAmmountOfQuarter(QuarterMonthModel monthModel){
        CompanyModel company =new CompanyModel();
        company.setCompanyId(monthModel.getCompanyId());
        return saleRepository.getTotalAmountForQuarter(monthModel.getMonth1(),monthModel.getMonth2(),monthModel.getMonth3(),monthModel.getYear(),company);
    }
    public int totalAmmountOfMonth(MonthYearModel monthModel){
        CompanyModel company =new CompanyModel();
        company.setCompanyId(monthModel.getCompanyId());
        return saleRepository.sumOfTotalAmountOfMonth(monthModel.getMonth(),monthModel.getYear(),company);
    }
}
