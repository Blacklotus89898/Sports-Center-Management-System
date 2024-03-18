package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.scs.model.Instructor;

public interface InstructorRepository extends CrudRepository<Instructor, String> {
    @Query("SELECT i FROM Instructor i WHERE i.email = :email AND i.password = :password")
    public Instructor findInstructorByEmailAndPassword(String email, String password);
}
