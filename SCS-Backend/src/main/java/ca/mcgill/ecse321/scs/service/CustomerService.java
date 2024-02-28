package ca.mcgill.ecse321.scs.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.model.Customer;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;

    // public List<Customer> findAllCustomers() {
    //     return (List<Customer>) customerRepository.findAll();
    // }

    // Create
    public Customer createCustomer(Customer customer) {
        // error checking 
        return customerRepository.save(customer);
    }
    
    // Read
    public Customer findCustomerById(String customerId) {
         Optional<Customer> customerOptional = customerRepository.findById(customerId);
         if (customerOptional.isPresent()) return customerOptional.get();
         throw new EntityNotFoundException(String.format("Customer not found with id: %s", customerId));
    }

    // Update --need to check if using dto
    public Customer updateCustomer(Customer customer) {
        // error checking 
        Customer currentCustomer = findCustomerById(Integer.toString(customer.getAccountId()));
        currentCustomer.setEmail(customer.getEmail());
        currentCustomer.setName(customer.getName());
        currentCustomer.setPassword(customer.getPassword());
        return customerRepository.save(currentCustomer);
    }

    // Delete
    public void deleteCustomerById(String customerId) {
        customerRepository.deleteById(customerId);
    }


}
