package com.trademate.project.Controller;

import com.trademate.project.DTO.CustomerDto;
import com.trademate.project.DTO.InvoiceDto;
import com.trademate.project.DTO.SaleDTO;
import com.trademate.project.Model.*;
import com.trademate.project.Repository.*;
import com.trademate.project.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@CrossOrigin(value = {"http://localhost:3000","https://trademate.ravicomputer.online/","https://trade-mate-fr-shadcn.vercel.app/"})
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
    private SaleBackupRepository saleBackupRepository;
@Autowired
private StockItemService stockItemService;
@Autowired
private ExpenseService expenseService;
@Autowired
private  StockItemRepository stockItemRepository;
@Autowired
private FeedbackRepository feedbackRepository;
@Autowired
private UserService userService;
@Autowired
private RecevivedMoneyService recevivedMoneyService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/addSale")
    public ResponseEntity<SaleModel> addSale(@RequestBody SaleModel saleModel){
        saleModel.setTotalAmmount(saleModel.getQuantity()*saleModel.getRate());
        saleModel.setRemaining((saleModel.getTotalAmmount()-saleModel.getReceivedAmmount()));
        StockItemModel stockItemModel=stockItemRepository.findByItemId(saleModel.getItem().getItemId());
        saleModel.setProfit((int) (saleModel.getTotalAmmount()-stockItemModel.getPurchasePrice()*saleModel.getQuantity()));
        saleModel.setGstInRupee((double) (saleModel.getTotalAmmount() * stockItemModel.getGstPercentage()) /100);
         return  new ResponseEntity<SaleModel>(saleService.addSale(saleModel), HttpStatus.CREATED);
    }

    @PostMapping("/addMultipleSale")
    public void addMultipleSale(@RequestBody List<SaleModel> saleModels){
        for(int i=0;i<saleModels.size();i++){
            SaleModel saleModel=saleModels.get(i);
            saleModel.setTotalAmmount(saleModel.getQuantity()*saleModel.getRate());
            saleModel.setRemaining((saleModel.getTotalAmmount()-saleModel.getReceivedAmmount()));
            StockItemModel stockItemModel=stockItemRepository.findByItemId(saleModel.getItem().getItemId());
            saleModel.setProfit((int) (saleModel.getTotalAmmount()-stockItemModel.getPurchasePrice()*saleModel.getQuantity()));
            saleModel.setGstInRupee((double) (saleModel.getTotalAmmount() * stockItemModel.getGstPercentage()) /100);
            stockItemModel.setQuantity(stockItemModel.getQuantity()-saleModel.getQuantity());
            stockItemRepository.save(stockItemModel);
        }
        saleRepository.saveAll(saleModels);
    }
    @PostMapping("/allsaledetails")
    public List<SaleDTO> getAllSale(@RequestBody CompanyModel company){
        return saleRepository.findAllSalesByCompanyId(company.getCompanyId());
    }

    @PostMapping("/allRemaining")
    public List<SaleModel> getAllRemaining(@RequestBody CompanyModel company){
        System.out.println("Remaing calling");
        return saleRepository.findAllByCompanyAndRemainingGreaterThanOrderByDateDesc(company,0);
    }


    @GetMapping("/{id}")
    public SaleModel getSaleById(@PathVariable long id){
        return saleService.getSaleById(id);
    }
    @PutMapping("/editsale")
    public String editSale(@RequestBody SaleModel saleModel){
        SaleModel saleModel1=saleRepository.findById(saleModel.getId()).get();
        saleModel1.setReceivedAmmount(saleModel.getReceivedAmmount()+saleModel1.getReceivedAmmount());
        saleModel1.setRemaining(saleModel1.getRemaining()-saleModel.getReceivedAmmount());
        saleRepository.save(saleModel1);
        return "Updated";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteSale(@PathVariable long id) {
      Optional<SaleModel> saleModel=saleRepository.findById(id);
      SaleBackupModel saleBackupModel=new SaleBackupModel();
      saleBackupModel.setCompany(saleModel.get().getCompany());
      saleBackupModel.setCustomer(saleModel.get().getCustomer());
      saleBackupModel.setItem(saleModel.get().getItem());
      saleBackupModel.setDate(saleModel.get().getDate());
      saleBackupModel.setReceivedAmmount(saleModel.get().getReceivedAmmount());
      saleBackupModel.setTotalAmmount(saleModel.get().getTotalAmmount());
      saleBackupRepository.save(saleBackupModel);
      saleRepository.deleteById(id);
      return "Deleted";
    }


    @PostMapping("/profit")
    public Object getProfit(@RequestBody DateModel intDate) {
       Date date = new Date(intDate.getYear(),intDate.getMonth(),intDate.getDay());
        return saleService.sumOfProfits(intDate.getMonth(),date.getYear(),intDate.getCompanyId());
    }
    @PostMapping("/bycname")
    public List<SaleModel> getByCustomerName(@RequestBody CustomerModel customerName){
        return saleService.getByCustomerName(customerName);
    }
    @PostMapping("/recust")
    public  int totalRemainingbyCustomer(@RequestBody CustomerModel customerName){
        return saleService.getRemainingByCustomer(customerName);
    }
    @GetMapping("remainsales")
    public List<SaleModel> salesWithRemaining(){
        return saleService.salesWithRemainingBalance();
    }
    @PostMapping("/totalsum")
    public Object sumOfTotal(@RequestBody DateModel intDate){
        CompanyModel company =new CompanyModel();
        company.setCompanyId(intDate.getCompanyId());
        return saleRepository.sumOfTotalRemaining(company);
    }
    @PostMapping("/byyear")
    public Object sumOfprofitByYear(@RequestBody DateModel intDate){
        Date date = new Date(intDate.getYear(),intDate.getMonth(),intDate.getDay());
        CompanyModel companyModel =new CompanyModel();
        companyModel.setCompanyId(intDate.getCompanyId());
        return saleRepository.sumOfRemainingByYear(date.getYear(),companyModel);
    }
    @PostMapping("/byid/{id}")
    public InvoiceDto findById(@PathVariable long id){
        SaleDTO saleModel =saleRepository.findSaleById(id);
        InvoiceDto invoiceDto=new InvoiceDto();
        CustomerDto customerModel =saleModel.getCustomer();
        invoiceDto.setCustomerModel(customerModel);
        List<SaleDTO> saleModels = saleService.getById(id);
        Double totalAmount = (double) 0;
        for(int i=0;i<saleModels.size();i++){
            saleModels.get(i).setCustomer(null);
            totalAmount+=saleModels.get(i).getTotalAmmount();
        }
          invoiceDto.setSales(saleModels);
          invoiceDto.setTotalAmount(totalAmount);
        return invoiceDto;
    }
    @PostMapping("/quart")
    public int totalAmountOfQuarter(@RequestBody QuarterMonthModel quarterMonthModel){
        return saleService.totalAmmountOfQuarter(quarterMonthModel);
    }
    @PostMapping("/monthsum")
    public  int totalAmmountOfMonth(@RequestBody MonthYearModel monthYearModel){
        return saleService.totalAmmountOfMonth(monthYearModel);
    }
//    @PostMapping("/date")
//    public LocalDate getMinDate(@RequestBody StockItemModel stock){
//        System.out.println(stock.getCompanyName()+":::::::::"+stock.getEmail()+"::::::"+saleRepository.findMinDate(stock.getCompanyName(),stock.getEmail()));
//        return saleRepository.findMinDate(stock.getCompanyName(),stock.getEmail());
//    }
    @PostMapping("/monthlyReport")
    public MonthlyReportModel monthlyReport(@RequestBody MonthYearModel monthYearModel){
        MonthlyReportModel monthlyReport= new MonthlyReportModel();
        List<CustomerSaleModel> topCustomersModel=new ArrayList<>();
        List<TopItemModel> topItemModelList =new ArrayList<>();
        CompanyModel company =new CompanyModel();
        company.setCompanyId(monthYearModel.getCompanyId());
        List<Object[]> customers = saleRepository.getTopFourCustomers(company,monthYearModel.getYear(),monthYearModel.getMonth());
        List<Object[]> items =saleRepository.getTopThreeItems(company,monthYearModel.getYear(),monthYearModel.getMonth());
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
        company.setCompanyId(monthYearModel.getCompanyId());
        Map<String,Long> sumOfRemaining= saleRepository.sumOfRemainingByMonths(monthYearModel.getMonth(),monthYearModel.getYear(),company);
        for (Map.Entry<String, Long> entry : sumOfRemaining.entrySet()) {
            if(entry.getKey().equals("sumOfTotalAmmount")){
                monthlyReport.setTotalRevenue(entry.getValue());
            }else if(entry.getKey().equals("sumOfRemaining")){
                monthlyReport.setTotalRemaining(entry.getValue());
            }else{
                monthlyReport.setTotalProfits(entry.getValue());
            }
                }
//        monthlyReport.setTotalExpenses(expenseService.getByMonth(monthYearModel));
        return monthlyReport;
    }
    @PostMapping("/dailyReport")
    public MonthlyReportModel dailyReport(@RequestBody DateModel dateModel){
      try{
          MonthlyReportModel monthlyReport = new MonthlyReportModel();
          CompanyModel company=new CompanyModel();
          company.setCompanyId(dateModel.getCompanyId());
          Map<String, Long> sumOfRemaining = saleRepository.sumOfRemainingByDay(dateModel.getMonth(),dateModel.getYear(),company,dateModel.getDay());
          for (Map.Entry<String, Long> entry : sumOfRemaining.entrySet()) {
              if(entry.getKey().equals("sumOfTotalAmmount")){
                  monthlyReport.setTotalRevenue(entry.getValue());
              }else if(entry.getKey().equals("sumOfRemaining")){
                  monthlyReport.setTotalRemaining(entry.getValue());
              }else{
                  monthlyReport.setTotalProfits(entry.getValue());
              }

          }
//          monthlyReport.setTotalExpenses(expenseService.getByDay(dateModel));
          return monthlyReport;
      }catch(Exception e){
          return null;
      }

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
