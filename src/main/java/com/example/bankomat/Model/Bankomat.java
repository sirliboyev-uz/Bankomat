package com.example.bankomat.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Bankomat{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String privateNumber;

    @Column(nullable = false)
    private String bankomatType;

    @Column(nullable = false)
    private double maxSum;

    @Column(nullable = false)
    private double respCard;

    @Column(nullable = false)
    private double otherCard;

    @Column(nullable = false)
    private double alertSum;

    @Column(nullable = false)
    private String address;
}
