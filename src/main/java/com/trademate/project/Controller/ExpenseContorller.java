package com.trademate.project.Controller;

import com.trademate.project.Model.ExpenseModel;
import com.trademate.project.Repository.ExpenseRepository;
import com.trademate.project.Service.CompanyService;
import com.trademate.project.Service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = {"http://localhost:3000","https://trademate.ravicomputer.online/","https://trade-mate-fr-shadcn.vercel.app/"})
@RequestMapping("/expense")
public class ExpenseContorller {

    @Autowired
    ExpenseRepository repository;

@Autowired
private CompanyService companyService;
    @PostMapping("/add")
    public void add(@RequestBody ExpenseModel expense){
       repository.save(expense);
    }
    @GetMapping("/all")
    public List<ExpenseModel> getAll(){
        List<ExpenseModel> expenseModels =repository.findAll();
        for (ExpenseModel expenseModel:expenseModels){
            expenseModel.setCompany(null);
        }
        return expenseModels;
    }
}
