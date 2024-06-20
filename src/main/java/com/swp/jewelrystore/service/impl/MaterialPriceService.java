package com.swp.jewelrystore.service.impl;

import com.swp.jewelrystore.converter.DateTimeConverter;
import com.swp.jewelrystore.entity.MaterialEntity;
import com.swp.jewelrystore.entity.MaterialPriceEntity;
import com.swp.jewelrystore.model.dto.MaterialPriceDTO;
import com.swp.jewelrystore.repository.MaterialPriceRepository;
import com.swp.jewelrystore.repository.MaterialRepository;
import com.swp.jewelrystore.service.IMaterialPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaterialPriceService implements IMaterialPriceService {

    private final MaterialRepository materialRepository;
    private final MaterialPriceRepository materialPriceRepository;
    private final DateTimeConverter dateTimeConverter;

    @Override
    public void addOrUpdateMaterialPrice(MaterialPriceDTO materialPriceDTO) {
        MaterialEntity materialEntity = materialRepository.findMaterialEntityById(materialPriceDTO.getMaterialId());
        MaterialPriceEntity materialPriceEntity = new MaterialPriceEntity();
        if(materialEntity != null){
            materialPriceEntity.setMaterial(materialEntity);
        }
        materialPriceEntity.setBuyPrice(materialPriceDTO.getBuyPrice());
        materialPriceEntity.setSellPrice(materialPriceDTO.getSellPrice());
        materialPriceEntity.setEffectDate(dateTimeConverter.convertToDateTimeDTO(materialPriceDTO.getEffectDate()));
        materialPriceRepository.save(materialPriceEntity);
    }
}
