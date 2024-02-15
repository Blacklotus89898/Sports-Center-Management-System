package ca.mcgill.ecse321.scs.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.model.CustomHours;

@SpringBootTest
public class CustomHoursRepositoryTests {
    @Autowired
    private CustomHoursRepository customHoursRepository;

    @AfterEach
	public void clearDatabase() {
		customHoursRepository.deleteAll();
	}

    @Test
    public void testPersistAndLoadClassType() {
        // creation of the class type
        String name = "Christmas";
        String description = "A joyous holiday!";
        Date date = Date.valueOf("2023-12-25");
        Time openTime = Time.valueOf("00:00:00");
        Time closeTime = Time.valueOf("00:00:00");  // representation for closed
        
        CustomHours customHours = new CustomHours(name, description, date, openTime, closeTime);

        // save the class type
        customHoursRepository.save(customHours);

        // read class type from database
        customHours = customHoursRepository.findCustomHoursByName(name);

        // assert equals
        assertNotNull(customHours);
        assertEquals(name, customHours.getName());
        assertEquals(description, customHours.getDescription());
        assertEquals(date, customHours.getDate());
        assertEquals(openTime, customHours.getOpenTime());
        assertEquals(closeTime, customHours.getCloseTime());
    }
}
