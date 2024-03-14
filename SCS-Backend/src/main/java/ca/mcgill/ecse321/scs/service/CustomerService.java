package ca.mcgill.ecse321.scs.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.exception.SCSException;
import ca.mcgill.ecse321.scs.model.Customer;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Transactional // everything interating with db --will be moved on top of each if other
               // non-transactional functions
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    // @Autowired
    // private InstructorRepository instructorRepository;
    // @Autowired
    // private OwnerRepository ownerRepository;
    // above commented are used once implementation are done

    public List<Customer> getAllCustomer() {
        return ServiceUtils.toList(customerRepository.findAll());
    }

    public Customer getCustomerById(Integer id) {
        Optional<Customer> customer = customerRepository.findById(id.toString());
        if (!customer.isPresent())
            throw new SCSException(HttpStatus.NOT_FOUND, ("Customer not found with id: " + id));
        return customer.get();
    }

    public Customer getCustomerByEmail(String customerEmail) {
        Customer customer = customerRepository.findCustomerByEmail(customerEmail);
        if (customer == null)
            throw new EntityNotFoundException(String.format("Customer not found with email: %s", customerEmail));
        return customer;
    }

    public Customer createCustomer(String name, String email, String password) {
        if ((customerRepository.findCustomerByEmail(email) != null)
        // || instructorRepository.findInstructorByEmail(email)!=null
        // || ownerRepository.findOwnerByEmail(email)!=null)
        ) { // the previous repo are not done yet
            throw new SCSException(HttpStatus.CONFLICT, "Email account already exists:" + email);
        }

        Customer newCustomer = new Customer();
        newCustomer.setName(name);
        newCustomer.setPassword(password);
        newCustomer.setEmail(email);
        newCustomer.setCreationDate(new Date(System.currentTimeMillis()));
        customerRepository.save(newCustomer);
        return newCustomer; // for test sake
    }

    public Customer updateCustomerById(Integer id, String name, String email, String password) {
        Optional<Customer> optionCustomer = customerRepository.findById(id.toString());
        if (!optionCustomer.isPresent())
            throw new SCSException(HttpStatus.NOT_FOUND, ("Customer not found with id: " + id));

        Customer currentCustomer = optionCustomer.get();
        currentCustomer.setEmail(email); // email should never change unless provided the option
        currentCustomer.setName(name);
        currentCustomer.setPassword(password);
        return customerRepository.save(currentCustomer);
    }

    // public Customer updateCustomerByEmail(Customer customer) {
    // // error checking in controller unless different implementation
    // Customer currentCustomer =
    // customerRepository.findCustomerByEmail(customer.getEmail());
    // // currentCustomer.setEmail(customer.getEmail()); --email should never change
    // unless provided the option
    // currentCustomer.setName(customer.getName());
    // currentCustomer.setPassword(customer.getPassword());
    // return customerRepository.save(currentCustomer);
    // }

    public void deleteCustomerById(Integer customerId) {
        customerRepository.delete(getCustomerById(customerId));
    }

    // public void deleteCustomerByEmail(String customerEmail) {
    // customerRepository.delete(getCustomerByEmail(customerEmail));
    // }

}
