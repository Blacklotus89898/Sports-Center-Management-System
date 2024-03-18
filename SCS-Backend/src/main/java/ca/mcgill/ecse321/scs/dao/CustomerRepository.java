package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.scs.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    public Customer findCustomerByAccountId(int accountId);

    public Customer findCustomerByEmail(String email);

    public Customer deleteCustomerByEmail(String email);

    @Query("SELECT e FROM Customer e WHERE e.email = :email AND e.password = :password")
    public Customer findCustomerByEmailAndPassword(String email, String password);
}
