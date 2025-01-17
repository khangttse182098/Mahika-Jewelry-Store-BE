package com.swp.jewelrystore.converter;

import com.swp.jewelrystore.constant.SystemConstant;
import com.swp.jewelrystore.entity.PurchaseOrderDetailEntity;
import com.swp.jewelrystore.entity.PurchaseOrderEntity;
import com.swp.jewelrystore.entity.SellOrderDetailEntity;
import com.swp.jewelrystore.entity.SellOrderEntity;
import com.swp.jewelrystore.model.response.DiamondCriteriaResponseDTO;
import com.swp.jewelrystore.model.response.InvoiceResponseDTO;
import com.swp.jewelrystore.model.response.MaterialResponseDTO;
import com.swp.jewelrystore.model.response.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConverter {

    private final ModelMapper modelMapper;
    private final ProductConverter productConverter;
    private final DiamondCriteriaConverter diamondCriteriaConverter;
    private final MaterialConverter materialConverter;
    private final DateTimeConverter dateTimeConverter;

    public InvoiceResponseDTO toInvoiceResponseDTO(SellOrderEntity sellOrderEntity) {
        InvoiceResponseDTO invoiceResponseDTO = modelMapper.map(sellOrderEntity, InvoiceResponseDTO.class);
        invoiceResponseDTO.setUserId(sellOrderEntity.getUser().getId());
        invoiceResponseDTO.setInvoiceCode(sellOrderEntity.getSellOrderCode());
        invoiceResponseDTO.setCreatedDate(dateTimeConverter.convertToDateTimeResponse(sellOrderEntity.getCreatedDate()));
        invoiceResponseDTO.setInvoiceType(SystemConstant.SELL);
        if(sellOrderEntity.getDiscount() != null){
            invoiceResponseDTO.setDiscountValue(sellOrderEntity.getDiscount().getValue());
            invoiceResponseDTO.setDiscountCode(sellOrderEntity.getDiscount().getCode());
        }
        if(sellOrderEntity.getCustomer() != null) {
            invoiceResponseDTO.setCustomerName(sellOrderEntity.getCustomer().getFullName());
            invoiceResponseDTO.setCustomerId(sellOrderEntity.getCustomer().getId());
        }else{
            invoiceResponseDTO.setCustomerName(SystemConstant.NO_CUSTOMER_YET);
        }
        invoiceResponseDTO.setStaffName(sellOrderEntity.getUser().getFullName());
        int totalPrice = 0;
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        for(SellOrderDetailEntity sellOrderDetailEntity : sellOrderEntity.getSellOrderDetailEntities()){
            totalPrice += sellOrderDetailEntity.getPrice();
            ProductResponseDTO productResponseDTO = productConverter.toProductResponseDTO(sellOrderDetailEntity.getProduct());
            productResponseDTO.setPrice(sellOrderDetailEntity.getPrice());
            productResponseDTOList.add(productResponseDTO);
        }
        invoiceResponseDTO.setProductResponseDTOList(productResponseDTOList);
        invoiceResponseDTO.setTotalPrice(totalPrice);
        return invoiceResponseDTO;
    }
    public InvoiceResponseDTO toInvoiceResponseDTO(PurchaseOrderEntity purchaseOrderEntity) {
        InvoiceResponseDTO invoiceResponseDTO = modelMapper.map(purchaseOrderEntity, InvoiceResponseDTO.class);
        invoiceResponseDTO.setUserId(purchaseOrderEntity.getUser().getId());
        invoiceResponseDTO.setInvoiceCode(purchaseOrderEntity.getPurchaseOrderCode());
        invoiceResponseDTO.setInvoiceType(SystemConstant.REPURCHASE);
        invoiceResponseDTO.setCreatedDate(dateTimeConverter.convertToDateTimeResponse(purchaseOrderEntity.getCreatedDate()));
        if(purchaseOrderEntity.getCustomer() != null) {
            invoiceResponseDTO.setCustomerName(purchaseOrderEntity.getCustomer().getFullName());
            invoiceResponseDTO.setCustomerId(purchaseOrderEntity.getCustomer().getId());
        }
        else{
            invoiceResponseDTO.setCustomerName(SystemConstant.NO_CUSTOMER_YET);
        }
        invoiceResponseDTO.setStaffName(purchaseOrderEntity.getUser().getFullName());
        int totalPrice = 0;
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        List<DiamondCriteriaResponseDTO> diamondCriteriaResponseDTOS = new ArrayList<>();
        List<MaterialResponseDTO> materialResponseDTOS = new ArrayList<>();
        for(PurchaseOrderDetailEntity purchaseOrderDetailEntity : purchaseOrderEntity.getPurchaseOrderDetailEntities()){
            totalPrice += purchaseOrderDetailEntity.getPrice();
            if(purchaseOrderDetailEntity.getProduct() != null){
                ProductResponseDTO productResponseDTO = productConverter.toProductResponseDTO(purchaseOrderDetailEntity.getProduct());
                productResponseDTO.setPrice(purchaseOrderDetailEntity.getPrice());
                productResponseDTO.setCreatedDate(dateTimeConverter.convertToDateTimeResponse(purchaseOrderDetailEntity.getProduct().getCreatedDate()));
                productResponseDTOList.add(productResponseDTO);
            }else if(purchaseOrderDetailEntity.getOrigin() != null){
                DiamondCriteriaResponseDTO diamondCriteriaResponseDTO = diamondCriteriaConverter.toDiamondCriteriaResponseDTO(purchaseOrderDetailEntity);
                diamondCriteriaResponseDTOS.add(diamondCriteriaResponseDTO);
            }else if(purchaseOrderDetailEntity.getMaterial() != null){
                MaterialResponseDTO materialResponseDTO = materialConverter.toMaterialResponseDTO(purchaseOrderDetailEntity);
                materialResponseDTO.setName(purchaseOrderDetailEntity.getMaterial().getName());
                materialResponseDTOS.add(materialResponseDTO);
            }
        }
        if(!productResponseDTOList.isEmpty()){
            invoiceResponseDTO.setProductResponseDTOList(productResponseDTOList);
        }
        if(!diamondCriteriaResponseDTOS.isEmpty()){
            invoiceResponseDTO.setDiamondCriteriaResponseDTOS(diamondCriteriaResponseDTOS);
        }
        if(!materialResponseDTOS.isEmpty()){
            invoiceResponseDTO.setMaterialResponseDTOList(materialResponseDTOS);
        }
        invoiceResponseDTO.setTotalPrice(totalPrice);
        return invoiceResponseDTO;
    }
}
