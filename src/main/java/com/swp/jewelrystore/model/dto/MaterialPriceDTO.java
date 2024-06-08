package com.swp.jewelrystore.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class MaterialPriceDTO {
    @ApiModelProperty(example = "1")
    private Long materialId;
    @ApiModelProperty(example = "7799000")
    private Double buyPrice;
    @ApiModelProperty(example = "8080000")
    private Double sellPrice;
    @ApiModelProperty(example = "00:00:00 06/06/2024")
    private String effectDate;
}
