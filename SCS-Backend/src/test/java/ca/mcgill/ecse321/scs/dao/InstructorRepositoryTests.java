package ca.mcgill.ecse321.scs.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.model.Instructor;
import java.sql.Date;
import java.util.Optional;

@SuppressWarnings("null")
@SpringBootTest
public class InstructorRepositoryTests {
    @Autowired
    private InstructorRepository instructorRepository;

    @AfterEach
	public void clearDatabase() {
		instructorRepository.deleteAll();
	}

    @Test
    public void testPersistAndLoadInstructor() {
        // creation of the class type
        Date date = new Date(0);
        // String className = "Yoga"; 
        String name = "Koko"; 
        String email = "lol@gym.com";
        String password = "secret";

        Instructor instructor = new Instructor();
        instructor.setCreationDate(date);
        instructor.setEmail(email);
        instructor.setName(name);
        instructor.setPassword(password);

        // save the class type
        Instructor sentInstructor = instructorRepository.save(instructor);

        // read instructor type from database
        Optional<Instructor> result =  instructorRepository.findById(Integer.toString(sentInstructor.getAccountId()));
        Instructor resultInstructor = result.get();

        // assert equals
        assertNotNull(resultInstructor);
        assertEquals(date.toString(), resultInstructor.getCreationDate().toString());
        assertEquals(email, resultInstructor.getEmail());
        assertEquals(name, resultInstructor.getName());
        assertEquals(password, resultInstructor.getPassword());
    }
}
