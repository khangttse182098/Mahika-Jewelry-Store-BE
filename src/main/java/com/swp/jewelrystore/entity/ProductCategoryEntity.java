package com.swp.jewelrystore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "productcategory")
public class ProductCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "sub_category_type")
    private String subCategoryType;

    @OneToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
    private List<ProductEntity> productEntities = new ArrayList<>();

}
