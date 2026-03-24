package com.trademate.project.Controller;

import com.trademate.project.Repository.PurchaseRepository;
import com.trademate.project.Repository.SaleRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gst")
@CrossOrigin(value = {"http://localhost:3000","https://tradematefr.mauryaravi599.workers.dev","https://trademate.ravicomputer.online/","https://trade-mate-fr-shadcn.vercel.app/"})
public class GstController {

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    SaleRepository saleRepository;


    @GetMapping("/get-gst-report")
    public ResponseEntity<List<Map<String,Object>>> getGstReports(@RequestParam (required = true) Long companyId){
        List<Map<String,Object>> gstReports = new ArrayList<>();
        gstReports.add(Map.of("inputGst",purchaseRepository.getDailyPurchaseWithInputGst(companyId)));
        gstReports.add(Map.of("outpurGst",saleRepository.getDailyOutputGst(companyId)));
        return ResponseEntity.ok(gstReports);
    }
}
