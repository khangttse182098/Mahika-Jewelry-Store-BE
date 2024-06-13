package com.swp.jewelrystore.service.impl;


import com.swp.jewelrystore.converter.ProductConverter;
import com.swp.jewelrystore.entity.*;
import com.swp.jewelrystore.model.dto.ProductDTO;
import com.swp.jewelrystore.model.request.ProductSearchRequestDTO;
import com.swp.jewelrystore.model.response.ProductResponseDTO;
import com.swp.jewelrystore.repository.*;
import com.swp.jewelrystore.service.IProductService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import com.swp.jewelrystore.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Transactional
@Service
public class ProductService implements IProductService {


    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private ProductGemRepository productGemRepository;

    @Autowired
    private ProductMaterialRepository productMaterialRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductConverter productConverter;

    @Autowired
    private SellOrderDetailRepository sellOrderDetailRepository;

    @Autowired
    private SellOrderRepository sellOrderRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private GemRepository gemRepository;

    @Autowired
    private PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    @Override
    public List<ProductResponseDTO> getAllProduct(ProductSearchRequestDTO productSearchRequestDTO) {
        List<ProductEntity> productEntities = productRepository.getAllProduct(productSearchRequestDTO);
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        List<ProductResponseDTO> productResponseDTOs = new ArrayList<>();
        for (ProductEntity productEntity : productEntities) {
            productResponseDTO = productConverter.toProductResponseDTO(productEntity);
            productResponseDTOs.add(productResponseDTO);
        }
        return productResponseDTOs;
    }

    @Override
    public List<ProductResponseDTO> getProductBySellOrderCode(String sellOrderCode) {
        SellOrderEntity sellOrderEntity = sellOrderRepository.findBySellOrderCodeIs(sellOrderCode);
        List<SellOrderDetailEntity> sellOrderDetailEntities = sellOrderEntity.getSellOrderDetailEntities();
        List<ProductEntity> productEntities = new ArrayList<>();
        for (SellOrderDetailEntity sellOrderDetailEntity : sellOrderDetailEntities) {
            if(sellOrderDetailEntity.getProduct().getPurchaseOrderDetailEntities().isEmpty()){
                productEntities.add(sellOrderDetailEntity.getProduct());
            }
        }
        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        for (ProductEntity productEntity : productEntities) {
            ProductResponseDTO productResponseDTO = productConverter.toProductResponseDTO(productEntity);
            for (SellOrderDetailEntity sellOrderDetailEntity : sellOrderDetailEntities) {
                if (sellOrderDetailEntity.getProduct().getId() == productEntity.getId()) {
                    productResponseDTO.setPrice(Double.valueOf(sellOrderDetailEntity.getPrice()));
                }
            }
            productResponseDTOList.add(productResponseDTO);
        }
        return productResponseDTOList;
    }

    @Override
    public void addOrUpdateProduct(ProductDTO productDTO) {
        ProductEntity productEntity = modelMapper.map(productDTO, ProductEntity.class);
        // Product Category
        ProductCategoryEntity productCategoryEntity = new ProductCategoryEntity();
        if(productDTO.getProductCategoryName().equals("Trang sức")){
            productCategoryEntity = productCategoryRepository.findByCategoryNameAndSubCategoryType(productDTO.getProductCategoryName(), productDTO.getSubCategoryType());
            productEntity.setProductCategory(productCategoryEntity);
        }else{
            productCategoryEntity = productCategoryRepository.findByCategoryName(productDTO.getProductCategoryName());
            productEntity.setProductCategory(productCategoryEntity);
        }
        // Counter
        CounterEntity counterEntity = counterRepository.findCounterEntityById(productDTO.getCounterId());
        productEntity.setCounter(counterEntity);
        // Material
        MaterialEntity materialEntity = materialRepository.findMaterialEntityById(productDTO.getMaterialId());
        // Product Image
        productEntity.setProductImage(productDTO.getProductImage());
        // Save product
        productRepository.save(productEntity);
        // Product Material
        ProductMaterialEntity productMaterialEntity = new ProductMaterialEntity();
        productMaterialEntity.setMaterial(materialEntity);
        productMaterialEntity.setProduct(productEntity);
        // Gem
        GemEntity gemEntity = gemRepository.findGemEntityById(productDTO.getGemId());
        // ProductGem
        ProductGemEntity productGemEntity = new ProductGemEntity();
        productGemEntity.setGem(gemEntity);
        productGemEntity.setProduct(productEntity);
        productGemRepository.save(productGemEntity);
    }

    @Override
    public void deleteByIdsIn(List<Long> ids) {
        productMaterialRepository.deleteAllByProductIdIn(ids);
        productGemRepository.deleteAllByProductIdIn(ids);
        List<ProductEntity> productEntities = productRepository.findByIdIsIn(ids);
        purchaseOrderDetailRepository.deleteByProductIn(productEntities);
        sellOrderDetailRepository.deleteByProductIn(productEntities);
        productRepository.deleteByIdIn(ids);
    }

//    private void saveThumbnail(BuildingDTO buildingDTO, BuildingEntity buildingEntity) {
//        String path = "/building/" + buildingDTO.getImageName();
//        if (null != buildingDTO.getImageBase64()) {
//            if (null != buildingEntity.getImage()) {
//                if (!path.equals(buildingEntity.getImage())) {
//                    File file = new File("C://home/office" + buildingEntity.getImage());
//                    file.delete();
//                }
//            }
//            byte[] bytes = Base64.decodeBase64(buildingDTO.getImageBase64().getBytes());
//            uploadFileUtils.writeOrUpdate(path, bytes);
//            buildingEntity.setImage(path);
//        }
//    }

}
