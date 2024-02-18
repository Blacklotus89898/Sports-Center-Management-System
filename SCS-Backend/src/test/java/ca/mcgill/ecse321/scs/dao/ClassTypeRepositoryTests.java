package ca.mcgill.ecse321.scs.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.model.ClassType;

@SpringBootTest
public class ClassTypeRepositoryTests {
    @Autowired
    private ClassTypeRepository classTypeRepository;

    @AfterEach
	public void clearDatabase() {
		classTypeRepository.deleteAll();
	}

    @Test
    public void testPersistAndLoadClassType() {
        // creation of the class type
        String className = "Yoga";
        String description = "A class to help you relax your mind and body!";
        boolean isAppoved = true;
        ClassType classType = new ClassType(className, description, isAppoved);

        // save the class type
        classTypeRepository.save(classType);

        // read class type from database
        classType = classTypeRepository.findClassTypeByClassName(className);

        // assert equals
        assertNotNull(classType);
        assertEquals(className, classType.getClassName());
        assertEquals(description, classType.getDescription());
        assertEquals(isAppoved, classType.getIsApproved());
    }
}