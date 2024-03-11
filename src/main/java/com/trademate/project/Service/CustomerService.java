package com.trademate.project.Service;

import com.trademate.project.Model.CustomerModel;
import com.trademate.project.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CompanyService companyService;
    public ResponseEntity<CustomerModel> addCust(CustomerModel customer){
        customer.getCompany().setCompanyId((companyService.getByCompanyNameAndEmail(customer.getCompanyName(),customer.getEmail()).getCompanyId()));
        return new ResponseEntity<CustomerModel>(customerRepository.save(customer), HttpStatus.CREATED);
    }
    public CustomerModel getByNameAndCompanyName(String name,String companyName,String emial){
        return customerRepository.findByCustomerNameAndCompanyName(name,companyName,emial);
    }
}
