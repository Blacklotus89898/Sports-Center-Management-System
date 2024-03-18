package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.scs.model.Owner;

public interface OwnerRepository extends CrudRepository<Owner, String> {
    public Owner findOwnerByEmail(String email);

    @Query("SELECT o FROM Owner o WHERE o.email = :email AND o.password = :password")
    public Owner findOwnerByEmailAndPassword(String email, String password);
}