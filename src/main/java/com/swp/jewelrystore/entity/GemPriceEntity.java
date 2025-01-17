package com.swp.jewelrystore.entity;


import com.swp.jewelrystore.model.response.GemPriceResponseDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "gemprice")
public class GemPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gem_price_id", nullable = false)
    private Long id;

    @Column(name = "origin", nullable = false)
    private String origin;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "clarity", nullable = false)
    private String clarity;

    @Column(name = "carat_weight_from", nullable = false)
    private Double caratWeightFrom;

    @Column(name = "carat_weight_to", nullable = false)
    private Double caratWeightTo;

    @Column(name = "cut", nullable = false)
    private String cut;

    @Column(name = "buy_price", nullable = false)
    private Double buyPrice;

    @Column(name = "sell_price", nullable = false)
    private Double sellPrice;

    @Column(name = "effect_date", nullable = false)
    private Date effectDate;

}