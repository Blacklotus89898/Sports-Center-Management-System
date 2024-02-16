package ca.mcgill.ecse321.scs.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.model.Owner;
import java.sql.Date;
import java.util.Optional;


@SpringBootTest
public class OwnerRepositoryTests {
    @Autowired
    private OwnerRepository ownerRepository;

    @AfterEach
	public void clearDatabase() {
		ownerRepository.deleteAll();
	}

    @Test
    public void testPersistAndLoadOwner() {
        // creation of the class type
        Date date = new Date(0);
        // String className = "Yoga"; 
        String name = "Koko"; 
        String email = "lol@.com";
        String password = "secret";

        Owner owner = new Owner();
        owner.setCreationDate(date);
        owner.setEmail(email);
        owner.setName(name);
        owner.setPassword(password);

        // save the class type
        Owner sentOwner = ownerRepository.save(owner);

        // read owner type from database
        Optional<Owner> result =  ownerRepository.findById(Integer.toString(sentOwner.getAccountId()));
        Owner resultOwner = result.get();

        // assert equals
        assertNotNull(resultOwner);
        assertEquals(date.toString(), resultOwner.getCreationDate().toString());
        assertEquals(email, resultOwner.getEmail());
        assertEquals(name, resultOwner.getName());
        assertEquals(password, resultOwner.getPassword());
    }
}
