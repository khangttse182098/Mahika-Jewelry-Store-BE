package com.swp.jewelrystore.custom.impl;

import com.swp.jewelrystore.custom.ProductRepositoryCustom;
import com.swp.jewelrystore.entity.MaterialPriceEntity;
import com.swp.jewelrystore.entity.ProductEntity;
import com.swp.jewelrystore.entity.ProductGemEntity;
import com.swp.jewelrystore.entity.ProductMaterialEntity;
import com.swp.jewelrystore.repository.GemPriceRepository;
import com.swp.jewelrystore.repository.MaterialPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    @Autowired
    private MaterialPriceRepository materialPriceRepository;

    @Autowired
    private GemPriceRepository gemPriceRepository;
    @Override
    public double calculateSellPrice(ProductEntity productEntity) {
        // Giá vốn sản phẩm = [giá vàng thời điểm * trọng lượng sản phẩm]
        // + tiền công + tiền đá
        double primePrice = 0;
        // tinh tien vang neu co
        if(!productEntity.getProductMaterialEntities().isEmpty()){
            List<ProductMaterialEntity> productMaterialEntities = productEntity.getProductMaterialEntities();
            for(ProductMaterialEntity productMaterialEntity : productMaterialEntities){
                primePrice += (productMaterialEntity.getWeight() * 0.267 * materialPriceRepository.findLatestMaterialPrice(productEntity).getSellPrice());
            }
        }
        // tinh tien kim cuong neu co
        if(!productEntity.getProductGemEntities().isEmpty()){
            List<ProductGemEntity> productGemEntities = productEntity.getProductGemEntities();
            for(ProductGemEntity productGemEntity : productGemEntities) {
                primePrice += gemPriceRepository.findLatestGemPrice(productGemEntity.getGem()).getSellPrice();
            }
        }
        // tinh tien nhan cong, da, vat lieu
        primePrice += productEntity.getMaterialCost() + productEntity.getGemCost() + productEntity.getProductionCost();
        // Giá bán = giá vốn sản phẩm * tỉ lệ áp giá,
        double sellPrice = primePrice * productEntity.getPriceRate();
        return sellPrice;
    }
}
