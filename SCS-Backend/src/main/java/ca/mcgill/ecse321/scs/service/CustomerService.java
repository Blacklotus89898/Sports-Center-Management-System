package ca.mcgill.ecse321.scs.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.model.Customer;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Transactional //everything interating with db --will be moved on top of each if other non-transactional functions
@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;

    // public List<Customer> findAllCustomers() {
    //     return (List<Customer>) customerRepository.findAll();
    // }

    // Create
    public Customer createCustomer(String name, String email, String password) {
        // error checking 
        // create the customer object here
        Customer newCustomer = new Customer();
        newCustomer.setName(name);
        newCustomer.setPassword(password);
        newCustomer.setEmail(email);
        newCustomer.setCreationDate(null);
        customerRepository.save(newCustomer);
        return newCustomer;
    }
    
    // Read --will be deleted
    public Customer findCustomerById(String customerId) {
         Optional<Customer> customer = customerRepository.findById(customerId);
         if (customer.isPresent()) return customer.get();
         throw new EntityNotFoundException(String.format("Customer not found with id: %s", customerId));
    }

    // Read
    public Customer findCustomerByEmail(String customerEmail) {
         Customer customer = customerRepository.findCustomerByEmail(customerEmail);
         if (customer != null) return customer;
         throw new EntityNotFoundException(String.format("Customer not found with email: %s", customerEmail));
    }

    // Update --need to check if using dto
    public Customer updateCustomerByEmail(Customer customer) {
        // error checking 
        Customer currentCustomer = findCustomerByEmail(customer.getEmail());
        currentCustomer.setEmail(customer.getEmail());
        currentCustomer.setName(customer.getName());
        currentCustomer.setPassword(customer.getPassword());
        return customerRepository.save(currentCustomer);
    }

    // Delete
    public void deleteCustomerByEmail(String customerEmail) {
        customerRepository.deleteById(customerEmail);
    }


}
