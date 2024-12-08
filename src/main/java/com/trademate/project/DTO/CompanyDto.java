package com.trademate.project.DTO;
import lombok.Data;

@Data
public class CompanyDto {
    private long companyId;
    private String companyName;
    private String companyAddress;
    private String gstIn;
    private String gstType;
    private String state;
    private String district;
    private String locality;
    private String country;
    private int pinCode;
    private long mobile;
    private  String email;
    private String image;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
}
