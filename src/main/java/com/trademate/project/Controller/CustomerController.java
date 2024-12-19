package com.trademate.project.Controller;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.CustomerModel;
import com.trademate.project.Repository.CustomerRepository;
import com.trademate.project.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(value = {"http://localhost:3000","https://trademate.ravicomputer.online/","https://trade-mate-fr-shadcn.vercel.app/"})
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/add")
    public ResponseEntity<CustomerModel> addCust(@RequestBody CustomerModel customerModel){
        customerModel.setAddDate(LocalDate.now());
        return customerService.addCustomer(customerModel);
    }

    @GetMapping("/customerById/{customerId}")
    public Optional<CustomerModel> getCustomerById(@PathVariable long customerId){
        return customerRepository.findById(customerId);
    }
    @GetMapping("/allCustomersByCompany/{companyId}")
    public List<CustomerModel> getAll(@PathVariable long companyId){
        CompanyModel companyModel = new CompanyModel();
        companyModel.setCompanyId(companyId);
        return customerRepository.findByCompany(companyModel);
    }

    @GetMapping("/getTop5customer/{companyId}")
    public List<Object[]> getTop5Customers(@PathVariable long companyId) {
        return customerRepository.findTop5CustomersByCompany(companyId);
    }

}
