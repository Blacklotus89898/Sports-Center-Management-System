package ca.mcgill.ecse321.scs.controller;

import ca.mcgill.ecse321.scs.dto.CustomerDto;
import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomer().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomerById(@PathVariable Integer id) {
        return convertToDto(customerService.getCustomerById(id));
    }

    @PostMapping
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto) {
        return convertToDto(customerService.createCustomer(customerDto.getName(), customerDto.getEmail(),
                customerDto.getPassword()));
    }

    @PutMapping("/{id}")
    public CustomerDto updateCustomerById(@PathVariable Integer id, @RequestBody CustomerDto customerDto) {
        return convertToDto(customerService.updateCustomerById(id, customerDto.getName(), customerDto.getEmail(),
                customerDto.getPassword()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Integer id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

    private CustomerDto convertToDto(Customer customer) {
        return new CustomerDto(customer.getAccountId(), customer.getName(), customer.getEmail(), customer.getPassword(),
                customer.getCreationDate());
    }
}
