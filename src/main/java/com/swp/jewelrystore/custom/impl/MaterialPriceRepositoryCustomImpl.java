package com.swp.jewelrystore.custom.impl;

import com.swp.jewelrystore.custom.MaterialPriceRepositoryCustom;
import com.swp.jewelrystore.entity.MaterialPriceEntity;
import com.swp.jewelrystore.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Repository
public class MaterialPriceRepositoryCustomImpl implements MaterialPriceRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public MaterialPriceEntity findLatestMaterialPrice(ProductEntity productEntity) {
        String sql = buildQueryFilter(productEntity.getId().toString());
        Query query = entityManager.createNativeQuery(sql,MaterialPriceEntity.class);
        List<MaterialPriceEntity> materialPriceEntities = query.getResultList();
        return materialPriceEntities.get(0);
    }
    private String buildQueryFilter(String materialId) {
        String sql = "select materialprice.* from materialprice where material_id = " + materialId+ " and effect_date <= current_date() limit 1";
        return sql;
    }
}