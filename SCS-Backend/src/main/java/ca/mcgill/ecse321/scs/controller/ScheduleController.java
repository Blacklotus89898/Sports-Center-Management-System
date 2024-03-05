package ca.mcgill.ecse321.scs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.scs.model.Schedule;
import ca.mcgill.ecse321.scs.service.ScheduleService;
import ca.mcgill.ecse321.scs.dto.ScheduleListDto;
import ca.mcgill.ecse321.scs.dto.ScheduleRequestDto;
import ca.mcgill.ecse321.scs.dto.ScheduleResponseDto;

@CrossOrigin(origins = "*")
@RestController
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    /**
     * get all schedules
     * 
     * @return list of all the schedules
     */
    @GetMapping(value = { "/schedules", "/schedules/" })
    public ScheduleListDto getSchedules() {
        List<ScheduleResponseDto> scheduleDtos = new ArrayList<>();
        for (Schedule s : scheduleService.getAllSchedules()) {
            scheduleDtos.add(new ScheduleResponseDto(s));
        }

        return new ScheduleListDto(scheduleDtos);
    }

    /**
     * get schedule by year
     * 
     * @param year
     * @return the found schedule
     */
    @GetMapping(value = { "/schedule/{year}", "/schedule/{year}/" })
    public ScheduleResponseDto getSchedule(@PathVariable int year) {
        return new ScheduleResponseDto(scheduleService.getSchedule(year));
    }

    /**
     * create a schedule
     * 
     * @param year
     * @return the created schedule, with the year
     */
    @PostMapping(value = { "/schedule", "/schedule/" })
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto schedule) {
        System.out.println("Creating schedule for year: " + schedule.getYear());
        Schedule newSchedule = scheduleService.createSchedule(schedule.getYear());
        return new ScheduleResponseDto(newSchedule);
    }
}
