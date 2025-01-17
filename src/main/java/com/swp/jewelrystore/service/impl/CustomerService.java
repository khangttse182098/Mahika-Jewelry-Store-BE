package com.swp.jewelrystore.service.impl;


import com.swp.jewelrystore.constant.SystemConstant;
import com.swp.jewelrystore.converter.CustomerConverter;
import com.swp.jewelrystore.converter.DateTimeConverter;
import com.swp.jewelrystore.customexception.CustomerException;
import com.swp.jewelrystore.entity.CustomerEntity;
import com.swp.jewelrystore.entity.PurchaseOrderEntity;
import com.swp.jewelrystore.entity.SellOrderDetailEntity;
import com.swp.jewelrystore.entity.SellOrderEntity;
import com.swp.jewelrystore.model.dto.CustomerDTO;
import com.swp.jewelrystore.model.dto.CustomerInvoiceDTO;
import com.swp.jewelrystore.model.response.CustomerDetailDTO;
import com.swp.jewelrystore.model.response.CustomerResponseDTO;
import com.swp.jewelrystore.repository.CustomerRepository;
import com.swp.jewelrystore.repository.PurchaseOrderRepository;
import com.swp.jewelrystore.repository.SellOrderDetailRepository;
import com.swp.jewelrystore.repository.SellOrderRepository;
import com.swp.jewelrystore.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final SellOrderRepository sellOrderRepository;
    private final SellOrderDetailRepository sellOrderDetailRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final CustomerConverter customerConverter;
    private final DateTimeConverter dateTimeConverter;

    @Override
    public Long checkExisted(CustomerDTO customerDTO) {
        CustomerEntity customer = customerRepository.findByPhoneNumber(customerDTO.getPhoneNumber());
        if (customer != null){
            return customer.getId();
        }
        CustomerEntity newCustomer = modelMapper.map(customerDTO, CustomerEntity.class);
        customerRepository.save(newCustomer);
        return newCustomer.getId();
    }

    @Override
    public List<CustomerResponseDTO> getCustomerList(Map<String, String> phoneNumber) {
        List<CustomerEntity> listCustomer = customerRepository.findByPhoneNumberCustom(phoneNumber);
        return customerConverter.convertCustomerToCustomerResponseDTO(listCustomer);
    }

    @Override
    public CustomerDetailDTO getCustomerDetail(Long id) {
        // get customer information
        CustomerDetailDTO customer = modelMapper.map(customerRepository.findById(id).get(), CustomerDetailDTO.class);
        // invoice of customer
        List<SellOrderEntity> listPaidSellOrder = sellOrderRepository.
                findByCustomer_IdAndStatusNot(id, SystemConstant.UNPAID);
        List<CustomerInvoiceDTO> customerInvoice = new ArrayList<>();
        customer.setQuantityOrder(listPaidSellOrder.size());
        double expense = 0;
        // 1 Order - N product
        for (SellOrderEntity item : listPaidSellOrder) {
            CustomerInvoiceDTO paidInvoice = modelMapper.map(item, CustomerInvoiceDTO.class);
            paidInvoice.setCreatedDate(dateTimeConverter.convertToDateTimeResponse(item.getCreatedDate()));
            List<SellOrderDetailEntity> sellOrder = sellOrderDetailRepository.findBySellOrderId(item.getId());
            double price = sellOrder.stream()
                    .mapToDouble(SellOrderDetailEntity::getPrice)
                    .sum();
            paidInvoice.setPrice(price);
            expense += price;
            customerInvoice.add(paidInvoice);
        }
        customer.setExpense(expense);
        customer.setExpenseAverage(expense/2);
        customer.setListCustomerInvoice(customerInvoice);
        return customer;
    }

    @Override
    public String addOrUpdateCustomer(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = modelMapper.map(customerDTO, CustomerEntity.class);
        List<CustomerEntity> listCustomer = customerRepository.findAll();
        for (CustomerEntity customer : listCustomer) {
            if (customer.getPhoneNumber().equals(customerDTO.getPhoneNumber()) && customer.getId() != customerDTO.getId()) {
                return "Phone number is already in use";
            }
        }
        customerRepository.save(customerEntity);
        if(customerDTO.getId() == null) {
            return "Add customer successfully";
        } else {
            return "Update customer successfully";
        }
    }

    @Override
    public String deleteCustomer(Long id) {
        List<SellOrderEntity> listSellOrderOfCustomer = sellOrderRepository.findByCustomer_Id(id);
        List<PurchaseOrderEntity> listPurchaseOrderOfCustomer = purchaseOrderRepository.findByCustomerId(id);
        if (!listSellOrderOfCustomer.isEmpty() || !listPurchaseOrderOfCustomer.isEmpty()){
            throw new CustomerException("Khách hàng đã tồn tại trong hóa đơn, không thể xóa");
        }
        customerRepository.deleteById(id);
        return "Delete customer successfully";
    }


}
