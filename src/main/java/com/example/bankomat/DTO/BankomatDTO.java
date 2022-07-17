package com.example.bankomat.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankomatDTO {
    private String privateNumber;
    private String bankomatType;
    private double maxSum;
    private double respCard;
    private double otherCard;
    private double alertSum;
    private String address;
}
