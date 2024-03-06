package com.trademate.project.Controller;

import com.trademate.project.Model.*;
import com.trademate.project.Repository.SaleBackupRepository;
import com.trademate.project.Repository.SaleRepository;
import com.trademate.project.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(value = {"http://localhost:3000","https://trade-mate-pearl.vercel.app/"})
@RequestMapping("/sales")
public class SaleController {
    private SaleService saleService;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SaleBackupService saleBackupService;
@Autowired
private StockItemService stockItemService;
@Autowired
private EmailService emailService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/addSale")
    public ResponseEntity<SaleModel> addSale(@RequestBody SaleModel saleModel){
        saleModel.getItem().setItemName(saleModel.getItemName());
        stockItemService.updateSaleQuantity(saleModel.getQuantity(),saleModel.getItemName());
        saleModel.getCompany().setCompanyId(companyService.getByCompanyNameAndEmail(saleModel.getCompanyName(),saleModel.getEmail()).getCompanyId());
        saleModel.getCustomer().setId(customerService.getByNameAndCompanyName(saleModel.getCustomerName(),saleModel.getCompanyName()).getId());
        String subject="Verify Your Email !";
        String message="You are successfully registered to TradeMate Click the link to verify your account "+"Verification Link"+"https://tradematebackend-production.up.railway.app/auth/setverify/"+saleModel.getEmail();
        emailService.sendEmail(saleModel.getEmail(),subject,message);
         return  new ResponseEntity<SaleModel>(saleService.addSale(saleModel), HttpStatus.CREATED);
    }
    @PostMapping("/allsaledetails")
    public List<SaleModel> getAllSale(@RequestBody DateModel date){
        return saleRepository.findAllByCompanyName(date.getCompanyName());
    }
    @GetMapping("/{id}")
    public SaleModel getSaleById(@PathVariable long id){
        return saleService.getSaleById(id);
    }
    @PutMapping("/editsale/{id}")
    public String editSale(@RequestBody SaleModel saleModel,@PathVariable long id){
        return saleService.updateSales(saleModel,id);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteSale(@PathVariable long id) {
        Optional<SaleModel> existingSaleOptional = saleRepository.findById(id);
        existingSaleOptional.ifPresentOrElse(existingSale -> {
           saleBackupService.add(existingSale);
            stockItemService.updateQuantity(existingSale.getQuantity(), existingSale.getItemName());
        }, () -> {
            throw new RuntimeException("Sale not found with id: " + id);
        });

        return saleService.deleteSale(id);
    }


    @PostMapping("/profit")
    public Object getProfit(@RequestBody DateModel intDate) {
       Date date = new Date(intDate.getYear(),intDate.getMonth(),intDate.getDay());
        return saleService.sumOfProfits(date.getMonth(),date.getYear(),intDate.getCompanyName(),intDate.getEmail());
    }
    @PostMapping("/bycname")
    public List<SaleModel> getByCustomerName(@RequestBody String customerName){
        return saleService.getByCustomerName(customerName);
    }
    @PostMapping("/recust")
    public  int totalRemainingbyCustomer(@RequestBody String customerName){
        return saleService.getRemainingByCustomer(customerName);
    }
    @GetMapping("remainsales")
    public List<SaleModel> salesWithRemaining(){
        return saleService.salesWithRemainingBalance();
    }
    @PostMapping("/totalsum")
    public Object sumOfTotal(@RequestBody DateModel intDate){
        return saleRepository.sumOfTotalRemaining(intDate.getCompanyName(),intDate.getEmail());
    }
    @PostMapping("/byyear")
    public Object sumOfprofitByYear(@RequestBody DateModel intDate){
        Date date = new Date(intDate.getYear(),intDate.getMonth(),intDate.getDay());
        return saleRepository.sumOfRemainingByYear(date.getYear(),intDate.getCompanyName(),intDate.getEmail());
    }
    @PostMapping("/byid/{id}")
    public List<SaleModel> findById(@PathVariable long id){
        return saleService.grtById(id);
    }
    @PostMapping("/quart")
    public int totalAmountOfQuarter(@RequestBody QuarterMonthModel quarterMonthModel){
        return saleService.totalAmmountOfQuarter(quarterMonthModel);
    }
    @PostMapping("/monthsum")
    public  int totalAmmountOfMonth(@RequestBody MonthYearModel monthYearModel){
        return saleService.totalAmmountOfMonth(monthYearModel);
    }
    @PostMapping("/date")
    public LocalDate getMinDate(@RequestBody StockItemModel stock){
        System.out.println(stock.getCompanyName()+":::::::::"+stock.getEmail()+"::::::"+saleRepository.findMinDate(stock.getCompanyName(),stock.getEmail()));
        return saleRepository.findMinDate(stock.getCompanyName(),stock.getEmail());
    }
}
