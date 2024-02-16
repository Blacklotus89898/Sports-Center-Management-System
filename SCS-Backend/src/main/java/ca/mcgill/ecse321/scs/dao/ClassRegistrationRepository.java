package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.scs.model.ClassRegistration;

/*
 * DAO (a database link) for crud operations for ClassRegistration.
 */
public interface ClassRegistrationRepository extends CrudRepository<ClassRegistration, Integer>{

    public ClassRegistration findClassRegistrationByRegistrationId(Integer registrationId);
    
}