package com.example.bankomat.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Card{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@Size(min = 16, max = 16)
    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String expDate;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String cardType;

    @Column(nullable = false)
    private double balance;
}
