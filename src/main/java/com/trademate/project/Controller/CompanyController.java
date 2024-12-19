package com.trademate.project.Controller;

import com.trademate.project.DTO.CompanyDto;
import com.trademate.project.DTO.MonthlySalesPurchasesDto;
import com.trademate.project.DTO.ProductSalePurchaseDto;
import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.UserModel;
import com.trademate.project.Repository.CompanyRepository;
import com.trademate.project.Service.CompanyService;
import com.trademate.project.Service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/company")
@CrossOrigin(value = {"http://localhost:3000", "https://trademate.ravicomputer.online/", "https://trade-mate-fr-shadcn.vercel.app/"})
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    ModelMapper modelMapper;


    @PostMapping("/add")
    public ResponseEntity<CompanyModel> addCompany(@RequestBody CompanyModel company) {
        company.setCompanyName(company.getCompanyName().trim());
        System.out.println(company.getCompanyName().trim());
        return companyService.adddCompany(company);
    }

    @GetMapping("/all/{userId}")
    public List<CompanyDto> getAllByUser(@PathVariable long userId) {
        UserModel userModel = new UserModel();
        userModel.setId(userId);
        List<CompanyModel> companyModels = companyService.getAllCompanyByUser(userModel);
        return companyModels.stream()
                .map(this::convertToDto) // Call a helper method to map the entity
                .toList(); // Collect to a list
    }

    private CompanyDto convertToDto(CompanyModel companyModel) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyId(companyModel.getCompanyId());
        companyDto.setCompanyName(companyModel.getCompanyName());
        companyDto.setCompanyAddress(companyModel.getCompanyAddress());
        companyDto.setGstIn(companyModel.getGstIn());
        companyDto.setGstType(companyModel.getGstType());
        companyDto.setState(companyModel.getState());
        companyDto.setDistrict(companyModel.getDistrict());
        companyDto.setLocality(companyModel.getLocality());
        companyDto.setCountry(companyModel.getCountry());
        companyDto.setPinCode(companyModel.getPinCode());
        companyDto.setMobile(companyModel.getMobile());
        companyDto.setEmail(companyModel.getEmail());
        companyDto.setImage(companyModel.getImage());
        return companyDto;
    }


    @GetMapping("/getCompanyBYId/{companyId}")
    public CompanyDto getCompamnyByCompanyId(@PathVariable long companyId) {
        CompanyModel companyModel = companyRepository.findByCompanyId(companyId);
        return modelMapper.map(companyModel, CompanyDto.class);
    }

    @PutMapping("/update")
    public CompanyModel updateCompany(@RequestBody CompanyModel company) {
        CompanyModel existingCompany = companyRepository.findByCompanyId(company.getCompanyId());
        if (company.getCompanyName().length() > 1) {
            existingCompany.setCompanyName(company.getCompanyName());
        }
        if (company.getCompanyAddress().length() > 1) {
            existingCompany.setCompanyAddress(company.getCompanyAddress());
        }
        if (company.getPinCode() > 0) {
            existingCompany.setPinCode(company.getPinCode());
        }
        if (company.getMobile() > 0) {
            existingCompany.setMobile(company.getMobile());
        }
        if (company.getGstIn().length() > 1) {
            existingCompany.setGstIn(company.getGstIn());
        }
        if (company.getGstType().length() > 1) {
            existingCompany.setGstType(company.getGstType());
        }
        if (company.getState().length() > 1) {
            existingCompany.setState(company.getState());
        }
        if (company.getCountry().length() > 1) {
            existingCompany.setCountry(company.getCountry());
        }
        if (company.getLocality().length() > 1) {
            existingCompany.setLocality(company.getLocality());
        }
        if (company.getDistrict().length() > 1) {
            existingCompany.setDistrict(company.getDistrict());
        }
        return companyRepository.save(existingCompany);
    }

    @PostMapping("/updateImage")
    public ResponseEntity<?> updateImageAndAccountDetials(@RequestBody CompanyModel companyModel) {
        CompanyModel companyModel1 = companyRepository.findByCompanyId(companyModel.getCompanyId());
        companyModel1.setAccountNumber(companyModel.getAccountNumber());
        companyModel1.setIfscCode(companyModel1.getIfscCode());
        companyModel1.setBankName(companyModel.getBankName());
        companyModel1.setImage(companyModel.getImage());
        return new ResponseEntity<>(companyRepository.save(companyModel1), HttpStatus.ACCEPTED);
    }


    // Weekly report
    @GetMapping("/report/weekly/{companyId}")
    public ResponseEntity<List<Object[]>> getWeeklyReport(@PathVariable long companyId) {
        System.out.println(companyId);
        List<Object[]> weeklyReport = companyRepository.findAllSalesAndPurchases(companyId);
        return ResponseEntity.ok(weeklyReport);
    }

    @GetMapping("/getCompanyProductReport/{companyId}")
    public List<ProductSalePurchaseDto> getProductReport(@PathVariable long companyId) {
        return companyRepository.findDistinctProductSalesAndPurchases(companyId);
    }

    @GetMapping("/getCompanySalePurchaseAmount/{companyId}")
    public List<MonthlySalesPurchasesDto> getOCmpanyMonthlySalesPurchaseReport(@PathVariable long companyId) {
        return companyRepository.findMonthlySalesAndPurchases(companyId);
    }

    // Sales Reports
    @GetMapping("/reports/sales/daily/{companyId}")
    public List<Map<String, Object>> getDailySalesReport(@PathVariable Long companyId) {
        return companyRepository.getDailySalesReport(companyId);
    }

    @GetMapping("/reports/sales-profit/daily/{companyId}")
    public List<Map<String, Object>> getDailySalesReportWithProfit(@PathVariable Long companyId) {
        return companyRepository.getDailySalesReportWithProfitAndRemaining(companyId);
    }


    @GetMapping("/reports/sales/monthly/{companyId}")
    public List<Map<String, Object>> getMonthlySalesReport(@PathVariable Long companyId) {
        return companyRepository.getMonthlySalesReport(companyId);
    }

    @GetMapping("/reports/sales/customer-wise/{companyId}")
    public List<Map<String, Object>> getCustomerWiseSalesReport(@PathVariable Long companyId) {
        return companyRepository.getCustomerWiseSalesReport(companyId);
    }

    // Purchase Reports
    @GetMapping("/reports/purchases/daily/{companyId}")
    public List<Map<String, Object>> getDailyPurchaseReport(@PathVariable Long companyId) {
        return companyRepository.getDailyPurchaseReport(companyId);
    }

    @GetMapping("/reports/purchases/monthly/{companyId}")
    public List<Map<String, Object>> getMonthlyPurchaseReport(@PathVariable Long companyId) {
        return companyRepository.getMonthlyPurchaseReport(companyId);
    }

    @GetMapping("/reports/purchases/vendor-wise/{companyId}")
    public List<Map<String, Object>> getVendorWisePurchaseReport(@PathVariable Long companyId) {
        return companyRepository.getSellerWisePurchaseReport(companyId);
    }

    // Inventory Reports
    @GetMapping("/reports/stock/current/{companyId}")
    public List<Object[]> getCurrentStockReport(@PathVariable Long companyId) {
        return companyRepository.getCurrentStockReport(companyId);
    }

    @GetMapping("/reports/stock/product-wise-sales/{companyId}")
    public List<Object[]> getProductWiseSalesReport(@PathVariable Long companyId) {
        return companyRepository.getProductWiseSalesReport(companyId);
    }

    // Financial Reports
    @GetMapping("/reports/financials/profit-loss/{companyId}")
    public Map<String, Object> getProfitAndLossReport(@PathVariable Long companyId) {
        return companyRepository.getProfitAndLossReport(companyId);
    }

    // Pending Payments Report
    @GetMapping("/reports/payments/pending-customers/{companyId}")
    public List<Object[]> getPendingPaymentsFromCustomers(@PathVariable Long companyId) {
        return companyRepository.getPendingPaymentsFromCustomers(companyId);
    }

    @GetMapping("/reports/payments/pending-vendors/{companyId}")
    public List<Map<String, Object>> getPendingPaymentsToVendors(@PathVariable Long companyId) {
        return companyRepository.getPendingPaymentsToVendors(companyId);
    }

    // Expense Reports
    @GetMapping("/reports/expenses/daily/{companyId}")
    public List<Map<String, Object>> getDailyExpenseReport(@PathVariable Long companyId) {
        return companyRepository.getDailyExpenseReport(companyId);
    }

    @GetMapping("/reports/expenses/monthly/{companyId}")
    public List<Map<String, Object>> getMonthlyExpenseReport(@PathVariable Long companyId) {
        return companyRepository.getMonthlyExpenseReport(companyId);
    }
}
