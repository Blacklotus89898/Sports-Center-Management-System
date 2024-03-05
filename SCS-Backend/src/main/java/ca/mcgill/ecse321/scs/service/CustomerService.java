package ca.mcgill.ecse321.scs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.scs.dao.CustomerRepository;
import ca.mcgill.ecse321.scs.dao.InstructorRepository;
import ca.mcgill.ecse321.scs.dao.OwnerRepository;
import ca.mcgill.ecse321.scs.model.Customer;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Transactional //everything interating with db --will be moved on top of each if other non-transactional functions
@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    

    // public Iterable<Customer> findAllCustomers() {
    //     return (ArrayList<Customer>) customerRepository.findAll();
    // }

    public Customer createCustomer(String name, String email, String password) {
        // error checking --input sanitation are done in controller
        if (password.length() > 100 ) throw new IllegalArgumentException(String.format("Invalid password: %s", password));
        else if ((customerRepository.findCustomerByEmail(email) != null) 
        // || instructorRepository.findInstructorByEmail(email)!=null
        // || ownerRepository.findOwnerByEmail(email)!=null
        ) {
            throw new EntityExistsException(String.format("Email account already exists: %s", email));
            // create the customer object here
        }
        // to be improved later
        Customer newCustomer = new Customer();
        newCustomer.setName(name);
        newCustomer.setPassword(password);
        newCustomer.setEmail(email);
        newCustomer.setCreationDate(null);
        customerRepository.save(newCustomer);
        return newCustomer; //for test sake
    }
    
    // Read --will be deleted
    // public Customer findCustomerById(String customerId) {
    //      Optional<Customer> customer = customerRepository.findById(customerId);
    //      if (customer.isPresent()) return customer.get();
    //      throw new EntityNotFoundException(String.format("Customer not found with id: %s", customerId));
    // }

    // will renamed to get
    public Customer getCustomerByEmail(String customerEmail) {
         Customer customer = customerRepository.findCustomerByEmail(customerEmail);
         if (customer == null) throw new EntityNotFoundException(String.format("Customer not found with email: %s", customerEmail));
         return customer;
    }

    // Update --need to check if using dto --> parsed inside the controller
    public Customer updateCustomerByEmail(Customer customer) {
        // error checking 
        Customer currentCustomer = getCustomerByEmail(customer.getEmail());
        // currentCustomer.setEmail(customer.getEmail());
        currentCustomer.setName(customer.getName());
        currentCustomer.setPassword(customer.getPassword());
        return customerRepository.save(currentCustomer);
    }

    // Delete
    public void deleteCustomerByEmail(String customerEmail) {
        customerRepository.delete(getCustomerByEmail(customerEmail));
    }


}
