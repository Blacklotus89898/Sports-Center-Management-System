package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.scs.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, String> {

    void createCustomer(Customer customer);
    
}
