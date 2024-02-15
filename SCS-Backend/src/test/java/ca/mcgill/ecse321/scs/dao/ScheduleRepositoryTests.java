package ca.mcgill.ecse321.scs.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.scs.model.Schedule;

@SpringBootTest
public class ScheduleRepositoryTests {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @AfterEach
    public void clearDatabase() {
        scheduleRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadSchedule() {
        // creation of the schedule
        int year = 2024;

        Schedule schedule = new Schedule(year);
        
        // save the schedule
        scheduleRepository.save(schedule);

        // read schedule from database
        schedule = scheduleRepository.findScheduleByYear(year);

        // assert equals
        assertNotNull(schedule);
        assertEquals(year, schedule.getYear());
    }
}
