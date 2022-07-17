package com.example.bankomat.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Datas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double income;

    private double outcome;

    private String card;

    private double sum;

    @CreationTimestamp
    private Timestamp creationTime;

    @UpdateTimestamp
    private Timestamp updatedTime;
}
