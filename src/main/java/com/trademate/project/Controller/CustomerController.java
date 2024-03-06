package com.trademate.project.Controller;

import com.trademate.project.Model.CustomerModel;
import com.trademate.project.Repository.CustomerRepository;
import com.trademate.project.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = {"http://localhost:3000","https://trade-mate-pearl.vercel.app/"})
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/add")
    public ResponseEntity<CustomerModel> addCust(@RequestBody CustomerModel customerModel){
        customerModel.getCompany().setCompanyId(customerService.getByNameAndCompanyName(customerModel.getCompanyName(),customerModel.getEmail()).getId());
        return customerService.addCust(customerModel);
    }
    @GetMapping("/byname/{name}")
    public CustomerModel getByName(@PathVariable String name){
        return customerRepository.findByCustomerName(name);
    }
    @GetMapping("/all")
    public List<CustomerModel> getAll(){
        return customerRepository.findAll();
    }
    @PostMapping("/bynamecompany")
    public CustomerModel getBynameAndCompany(@RequestBody CustomerModel customer){
//        System.out.println("customer name is"+customerService.getByNameAndCompanyName(customer.getCustomerName(),customer.getCompanyName()).getCustomerName());
        return customerService.getByNameAndCompanyName(customer.getCustomerName(),customer.getCompanyName());
    }
}
