package com.swp.jewelrystore.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String productName;
    private String productCode;
    private String productImage;
    private Double materialCost;
    private Double gemCost;
    private Double productionCost;
    private Double priceRate;
    private Date createdDate;
    private String materialName;
    private String gemName;
    private String categoryName;
    private Double price;
}