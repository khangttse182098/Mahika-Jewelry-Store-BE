package com.swp.jewelrystore.custom;

import com.swp.jewelrystore.entity.GemEntity;
import com.swp.jewelrystore.entity.GemPriceEntity;
import com.swp.jewelrystore.model.dto.DiamondCriteriaDTO;
import com.swp.jewelrystore.model.dto.DiamondDTO;

import java.util.List;


public interface GemPriceRepositoryCustom {
    GemPriceEntity findLatestGemPrice(GemEntity gemEntity);
    GemPriceEntity checkGemCaratInRange(DiamondCriteriaDTO diamondCriteriaDTO);
    List<GemPriceEntity> findAllGemPriceHistory(DiamondCriteriaDTO diamondCriteriaDTO);
    String autoGenerateCode();
    List<GemPriceEntity> checkGemExisted(DiamondDTO diamondDTO);
}
