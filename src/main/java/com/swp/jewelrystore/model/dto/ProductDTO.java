package com.swp.jewelrystore.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Valid
// FE gui ve ID cua dah
public class ProductDTO {

     @ApiModelProperty(example = "xem model", value = "sửa id thành \"\" nếu muốn thêm sp, sửa id thành \"3\" nếu muốn update sp có id = 3")
     private Long id;

     @NotBlank(message = "Product code is required")
     @ApiModelProperty(example = "NNKCVT14KTest")
     private String productCode;

     @ApiModelProperty(example = "Nhẫn Nam Kim cương Vàng trắng 14K Test")
     @NotBlank(message = "Product name is required")
     private String productName;

     @NotBlank(message = "Product category is required")
     @ApiModelProperty(example = "Trang sức")
     private String productCategoryName; // loại sản phẩm: kim cương, trang sức

     @ApiModelProperty(example = "8")
     private Long materialId;
     @NotNull(message = "Material weight is required")
     @ApiModelProperty(example = "5.3")
     private Double materialWeight;

     @NotNull(message = "Counter is required")
     @ApiModelProperty(example = "2")
     private Long counterId;

     @ApiModelProperty(example = "100")
     private Double materialCost;

     @ApiModelProperty(example = "80")
     private Double productionCost;

     @ApiModelProperty(example = "1.5")
     private Double priceRate;

     @ApiModelProperty(example = "0")
     private Double gemCost;

     @ApiModelProperty(example = "Nhẫn")
     private String subCategoryType; // danh muc: nhẫn, dây chuyen, ...

     @ApiModelProperty(example = "3")
     private Long gemId;

     @ApiModelProperty(example = "image21.jpg")
     private String productImage;

}
