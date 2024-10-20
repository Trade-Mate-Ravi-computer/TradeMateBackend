package com.trademate.project.Service;

import com.trademate.project.Model.CompanyModel;
import com.trademate.project.Model.UserModel;
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
    public List<CompanyModel> getAllCompanyByUser(UserModel userModel){
        return companyRepository.findByUser(userModel);
    }
}
