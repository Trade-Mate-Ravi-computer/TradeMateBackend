package com.trademate.project.Service;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public ResponseEntity<CompanyModel> adddCompany(CompanyModel company){
        return new ResponseEntity<CompanyModel>(companyRepository.save(company), HttpStatus.CREATED);
    }
    public List<CompanyModel> getAllCompany(){
        return companyRepository.findAll();
    }
    public  List<CompanyModel> getByUserId(long id){
        return companyRepository.findByUserId(id);
    }
    public CompanyModel getByName(String name){
        return companyRepository.findByCompanyName(name);
    }
    public String updateCompany(CompanyModel company){
        companyRepository.save(company);
        return "updated";
    }
    public CompanyModel getByCompanyId(long id){
        return companyRepository.findByCompanyId(id);
    }
public CompanyModel getByCompanyNameAndEmail(String cname,String email){
        return companyRepository.findByCompanyNameAndEmail(cname,email);
}
}
