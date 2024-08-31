package com.trademate.project.Controller;

import com.trademate.project.Model.ExpenseModel;
import com.trademate.project.Service.CompanyService;
import com.trademate.project.Service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = {"http://localhost:3000","https://trade-mate-pearl.vercel.app/","http://ec2-16-171-11-228.eu-north-1.compute.amazonaws.com:8080","http://51.20.69.152/"})@RequestMapping("/expense")
public class ExpenseContorller {
    @Autowired
    private ExpenseService service;
@Autowired
private CompanyService companyService;
    @PostMapping("/add")
    public ResponseEntity<ExpenseModel> add(@RequestBody ExpenseModel expense){
        expense.getCompany().setCompanyId(companyService.getByCompanyNameAndEmail(expense.getCompanyName(),expense.getEmail()).getCompanyId());
        return service.add(expense);
    }
    @GetMapping("/all")
    public List<ExpenseModel> getAll(){
        return service.getAll();
    }
}
