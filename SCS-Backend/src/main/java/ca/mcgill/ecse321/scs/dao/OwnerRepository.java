package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.scs.model.Owner;

public interface OwnerRepository extends CrudRepository<Owner, String> {
    
}
