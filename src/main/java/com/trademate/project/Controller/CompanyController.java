package com.trademate.project.Controller;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.UserModel;
import com.trademate.project.Repository.CompanyRepository;
import com.trademate.project.Service.CompanyService;
import com.trademate.project.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/company")
@CrossOrigin(value = {"http://localhost:3000","https://ravicomputer.online/","https://trade-mate-fr-shadcn.vercel.app/"})
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyRepository companyRepository;


    @PostMapping("/add")
    public ResponseEntity<CompanyModel> addCompany( @RequestBody CompanyModel company){
        company.setCompanyName(company.getCompanyName().trim());
        System.out.println(company.getCompanyName().trim());
        return companyService.adddCompany(company);
    }
    @GetMapping("/all/{userId}")
    public List<CompanyModel> getAllByUser(@PathVariable long userId){
        UserModel userModel =new UserModel();
        userModel.setId(userId);
        return companyService.getAllCompanyByUser(userModel);
    }

    @GetMapping("/getCompanyBYId/{companyId}")
    public CompanyModel getCompamnyByCompanyId(@PathVariable long companyId){
        return companyRepository.findByCompanyId(companyId);
    }
    @PutMapping("/update")
    public CompanyModel updateCompany(@RequestBody CompanyModel company){
        CompanyModel existingCompany = companyRepository.findByCompanyId(company.getCompanyId());
        if(company.getCompanyName().length()>1){
            existingCompany.setCompanyName(company.getCompanyName());
        }
        if(company.getCompanyAddress().length()>1){
            existingCompany.setCompanyAddress(company.getCompanyAddress());
        }
        if(company.getPinCode()>0){
            existingCompany.setPinCode(company.getPinCode());
        }
        if(company.getMobile()>0){
            existingCompany.setMobile(company.getMobile());
        }
        if(company.getGstIn().length()>1){
            existingCompany.setGstIn(company.getGstIn());
        }
        if(company.getGstType().length()>1){
            existingCompany.setGstType(company.getGstType());
        }
        if(company.getState().length()>1){
            existingCompany.setState(company.getState());
        }
        if(company.getCountry().length()>1){
            existingCompany.setCountry(company.getCountry());
        }
        if(company.getLocality().length()>1){
            existingCompany.setLocality(company.getLocality());
        }
        if(company.getDistrict().length()>1){
            existingCompany.setDistrict(company.getDistrict());
        }
        return companyRepository.save(existingCompany);
    }

}
