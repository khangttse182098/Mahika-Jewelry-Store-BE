package com.swp.jewelrystore.service.impl;

import com.swp.jewelrystore.converter.DateTimeConverter;
import com.swp.jewelrystore.entity.MaterialEntity;
import com.swp.jewelrystore.entity.MaterialPriceEntity;
import com.swp.jewelrystore.model.response.GoldResponseDTO;
import com.swp.jewelrystore.repository.MaterialPriceRepository;
import com.swp.jewelrystore.repository.MaterialRepository;
import com.swp.jewelrystore.service.IGoldPriceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoldPriceService implements IGoldPriceService {

    private final MaterialRepository materialRepository;
    private final MaterialPriceRepository materialPriceRepository;
    private final ModelMapper modelMapper;
    private final DateTimeConverter dateTimeConverter;

    @Override
    public List<GoldResponseDTO> goldPriceList() {
        List<MaterialEntity> listMaterial = materialRepository.findAll();
        List<GoldResponseDTO> result = new ArrayList<>();
        for (MaterialEntity item : listMaterial) {
            MaterialPriceEntity materialPrice = materialPriceRepository.findLatestGoldPrice(item);
            GoldResponseDTO goldResponseDTO = modelMapper.map(materialPrice, GoldResponseDTO.class);
            goldResponseDTO.setId(item.getId());
            goldResponseDTO.setGoldName(item.getName());
            goldResponseDTO.setEffectDate(dateTimeConverter.convertToDateTimeResponse(materialPrice.getEffectDate()));
            result.add(goldResponseDTO);
        }
        return result;
    }
}
