package com.swp.jewelrystore.api;


import com.swp.jewelrystore.entity.CustomerEntity;
import com.swp.jewelrystore.entity.SellOrderEntity;
import com.swp.jewelrystore.model.dto.CustomerDTO;
import com.swp.jewelrystore.model.response.CustomerResponseDTO;
import com.swp.jewelrystore.repository.CustomerRepository;
import com.swp.jewelrystore.repository.SellOrderRepository;
import com.swp.jewelrystore.model.response.CustomerDetailDTO;
import com.swp.jewelrystore.service.ICustomerService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/customer")
@CrossOrigin
@RequiredArgsConstructor
public class CustomerAPI {

    private final ICustomerService customerService;
    private final SellOrderRepository sellOrderRepository;
    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;

    @GetMapping("/list")
    public List<CustomerResponseDTO> customerList(@RequestParam Map<String, String> phoneNumber){
        return customerService.getCustomerList(phoneNumber);
    }

    @GetMapping("/list-{id}")
    public CustomerDetailDTO detailCustomerInformation(@PathVariable Long id){
        return customerService.getCustomerDetail(id);
    }

    @GetMapping
    public CustomerResponseDTO findCustomerBySellOrderCode(@RequestParam String sellOrderCode){
        SellOrderEntity sellOrderEntity = sellOrderRepository.findBySellOrderCodeIs(sellOrderCode);
        CustomerResponseDTO customerResponseDTO = modelMapper.map(sellOrderEntity.getCustomer(), CustomerResponseDTO.class);
        customerResponseDTO.setExpense(null);
        customerResponseDTO.setQuantityOrder(0);
        return customerResponseDTO;
    }
    @PostMapping
    public String addOrUpdateCustomer(@Valid @RequestBody CustomerDTO customerDTO){
        CustomerEntity customerEntity = modelMapper.map(customerDTO, CustomerEntity.class);
        customerRepository.save(customerEntity);
        if(customerDTO.getId() == null) return "Add customer successfully";
        return "Update customer successfully";
    }
}
