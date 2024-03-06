package ca.mcgill.ecse321.scs.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import ca.mcgill.ecse321.scs.model.CustomHours;

public class CustomHoursResponseDto {
    private String name;
    private String description;
    private LocalDate date;
    private LocalTime openTime;
    private LocalTime closeTime;

    private ScheduleRequestDto schedule;

    public CustomHoursResponseDto() {}

    public CustomHoursResponseDto(CustomHours customHours) {
        this.name = customHours.getName();
        this.description = customHours.getDescription();
        this.date = customHours.getDate().toLocalDate();
        this.openTime = customHours.getOpenTime().toLocalTime();
        this.closeTime = customHours.getCloseTime().toLocalTime();

        this.schedule = new ScheduleRequestDto(customHours.getSchedule());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public ScheduleRequestDto getSchedule() {
        return schedule;
    }
}
