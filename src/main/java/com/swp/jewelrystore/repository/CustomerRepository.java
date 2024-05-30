package com.swp.jewelrystore.repository;


import com.swp.jewelrystore.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
     CustomerEntity findByPhoneNumber(String phoneNumber);
     CustomerEntity findByFullNameOrPhoneNumber(String fullName, String phoneNumber);
}
