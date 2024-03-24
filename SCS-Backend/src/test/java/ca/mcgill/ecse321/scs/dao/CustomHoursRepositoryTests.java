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
import ca.mcgill.ecse321.scs.model.Schedule;

@SpringBootTest
public class CustomHoursRepositoryTests {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private CustomHoursRepository customHoursRepository;

    @AfterEach
	public void clearDatabase() {
		customHoursRepository.deleteAll();
        scheduleRepository.deleteAll();
	}

    @Test
    public void testPersistAndLoadCustomHours() {
        // creation of the schedule
        int year = 2024;

        Schedule schedule = new Schedule(year);
        
        // save the schedule
        scheduleRepository.save(schedule);


        // creation of the custom hours
        String name = "Christmas";
        String description = "A joyous holiday!";
        Date date = Date.valueOf("2024-12-25");
        Time openTime = Time.valueOf("00:00:00");
        Time closeTime = Time.valueOf("00:00:00");  // representation for closed
        
        CustomHours customHours = new CustomHours(name, description, date, openTime, closeTime, schedule);

        // save the custom hours
        customHoursRepository.save(customHours);

        // read custom hours from database
        customHours = customHoursRepository.findCustomHoursByName(name, year);

        // assert equals
        assertNotNull(customHours);
        assertEquals(name, customHours.getName());
        assertEquals(description, customHours.getDescription());
        assertEquals(date, customHours.getDate());
        assertEquals(openTime, customHours.getOpenTime());
        assertEquals(closeTime, customHours.getCloseTime());
        assertEquals(year, customHours.getSchedule().getYear());
    }
}
