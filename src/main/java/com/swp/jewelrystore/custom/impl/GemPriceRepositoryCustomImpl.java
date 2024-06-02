package com.swp.jewelrystore.custom.impl;

import com.swp.jewelrystore.custom.GemPriceRepositoryCustom;
import com.swp.jewelrystore.entity.GemEntity;
import com.swp.jewelrystore.entity.GemPriceEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

public class GemPriceRepositoryCustomImpl implements GemPriceRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public GemPriceEntity findLatestGemPrice(GemEntity gemEntity) {
        String sql = buildQueryFilter(gemEntity);
        Query query = entityManager.createNativeQuery(sql,GemPriceEntity.class);
        List<GemPriceEntity> gemPriceEntities = query.getResultList();
        return gemPriceEntities.get(0);
    }
    private String buildQueryFilter(GemEntity gemEntity) {
        String sql = "select gemprice.* from gemprice where origin = '" + gemEntity.getOrigin() +"' and color = '" + gemEntity.getColor()+ "' and clarity = '" + gemEntity.getClarity() + "' and carat_weight = " + gemEntity.getCaratWeight() + " and cut = '"+gemEntity.getCut()+"' and effect_date <= current_date() limit 1";
        return sql;
    }
}