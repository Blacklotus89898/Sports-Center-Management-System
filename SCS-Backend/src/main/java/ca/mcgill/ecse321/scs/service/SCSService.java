package ca.mcgill.ecse321.scs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.model.Customer;

@Service
public class SCSService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> findAllCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    public void createCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
