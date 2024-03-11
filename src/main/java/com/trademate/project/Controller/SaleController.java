package com.trademate.project.Controller;

import com.trademate.project.Model.*;
import com.trademate.project.Repository.ExpenseRepository;
import com.trademate.project.Repository.FeedbackRepository;
import com.trademate.project.Repository.SaleBackupRepository;
import com.trademate.project.Repository.SaleRepository;
import com.trademate.project.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

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
private ExpenseService expenseService;
@Autowired
private FeedbackRepository feedbackRepository;
@Autowired
private UserService userService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/addSale")
    public ResponseEntity<SaleModel> addSale(@RequestBody SaleModel saleModel){
        saleModel.getItem().setItemName(saleModel.getItemName());
        stockItemService.updateSaleQuantity(saleModel.getQuantity(),saleModel.getItemName());
        saleModel.getCompany().setCompanyId(companyService.getByCompanyNameAndEmail(saleModel.getCompanyName(),saleModel.getEmail()).getCompanyId());
        saleModel.getCustomer().setId(customerService.getByNameAndCompanyName(saleModel.getCustomerName(),saleModel.getCompanyName()).getId());
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
    @PostMapping("/monthlyReport")
    public MonthlyReportModel monthlyReport(@RequestBody MonthYearModel monthYearModel){
        MonthlyReportModel monthlyReport= new MonthlyReportModel();
        List<CustomerSaleModel> topCustomersModel=new ArrayList<>();
        List<TopItemModel> topItemModelList =new ArrayList<>();
        List<Object[]> customers = saleRepository.getTopFourCustomers(monthYearModel.getCompanyName(),monthYearModel.getEmail(),monthYearModel.getYear(),monthYearModel.getMonth());
        List<Object[]> items =saleRepository.getTopThreeItems(monthYearModel.getCompanyName(),monthYearModel.getEmail(),monthYearModel.getYear(),monthYearModel.getMonth());
        for (Object[] row : customers) {
            CustomerSaleModel customerSaleModel=new CustomerSaleModel();
            String customerName = (String) row[0];
            Long totalSale = (Long) row[1];
            customerSaleModel.setCustomerName(customerName);
            customerSaleModel.setTotalSale(totalSale);
            topCustomersModel.add(customerSaleModel);
        }
        for(Object[] row :items){
            TopItemModel topItemModel =new TopItemModel();
            String itemName = (String) row[0];
            Long totalQuantity = (Long) row[1];
            topItemModel.setItemName(itemName);
            topItemModel.setQuantity(totalQuantity);
            topItemModelList.add(topItemModel);
        }
        TopCustomersModel topCustomersModel1 =new TopCustomersModel();
        topCustomersModel1.setCustomerSaleModels(topCustomersModel);
        TopItemsModel topItemsModel=new TopItemsModel();
        topItemsModel.setTopItemModelList(topItemModelList);
        monthlyReport.setTopItemsModel(topItemsModel);
        monthlyReport.setTopCustomersModel(topCustomersModel1);
        Map<String,Long> sumOfRemaining= saleRepository.sumOfRemainingByMonths(monthYearModel.getMonth(),monthYearModel.getYear(),monthYearModel.getCompanyName(),monthYearModel.getEmail());
        for (Map.Entry<String, Long> entry : sumOfRemaining.entrySet()) {
            if(entry.getKey().equals("sumOfTotalAmmount")){
                monthlyReport.setTotalRevenue(entry.getValue());
            }else if(entry.getKey().equals("sumOfRemaining")){
                monthlyReport.setTotalRemaining(entry.getValue());
            }else{
                monthlyReport.setTotalProfits(entry.getValue());
            }
                }
        monthlyReport.setTotalExpenses(expenseService.getByMonth(monthYearModel));
        return monthlyReport;
    }
    @PostMapping("/dailyReport")
    public MonthlyReportModel dailyReport(@RequestBody DateModel dateModel){
        System.out.println(dateModel.getDay());
        MonthlyReportModel monthlyReport = new MonthlyReportModel();
        Map<String, Long> sumOfRemaining = saleRepository.sumOfRemainingByDay(dateModel.getMonth(),dateModel.getYear(),dateModel.getCompanyName(),dateModel.getEmail(),dateModel.getDay());
        for (Map.Entry<String, Long> entry : sumOfRemaining.entrySet()) {
            if(entry.getKey().equals("sumOfTotalAmmount")){
                monthlyReport.setTotalRevenue(entry.getValue());
            }else if(entry.getKey().equals("sumOfRemaining")){
                monthlyReport.setTotalRemaining(entry.getValue());
            }else{
                monthlyReport.setTotalProfits(entry.getValue());
            }

        }
        monthlyReport.setTotalExpenses(expenseService.getByDay(dateModel));
        return monthlyReport;
    }
    @PostMapping("/feedback")
    public String add(@RequestBody FeedbackModel feedbackModel){
        if(feedbackRepository.findByName(userService.getByEmail(feedbackModel.getName()).getName())==null){
            feedbackModel.setName(userService.getByEmail(feedbackModel.getName()).getName());
            feedbackRepository.save(feedbackModel);
            return "Thank For Your Valuable feedback";
        }else{
            return "You have already submitted your Review";
        }


    }
}
