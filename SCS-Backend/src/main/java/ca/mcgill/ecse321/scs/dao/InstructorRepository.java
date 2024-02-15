package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.scs.model.Instructor;

public interface InstructorRepository extends CrudRepository<Instructor, String> {
    
}
