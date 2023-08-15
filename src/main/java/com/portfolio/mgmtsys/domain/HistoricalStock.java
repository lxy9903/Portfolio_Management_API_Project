package com.portfolio.mgmtsys.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "historical_stock", indexes = {@Index(columnList = "ticker")})
@Data
public class HistoricalStock {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private Date date;

    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "open_price")
    private Double openPrice;

    @Column(name = "high_price")
    private Double highPrice;

    @Column(name = "low_price")
    private Double lowPrice;

    @Column(name = "close_price")
    private Double closePrice;

    @Column(name = "vol")
    private Double vol;
}
