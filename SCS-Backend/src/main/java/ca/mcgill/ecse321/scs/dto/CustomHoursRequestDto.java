package ca.mcgill.ecse321.scs.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class CustomHoursRequestDto {
    private String name;
    private String description;
    private LocalDate date;
    private LocalTime openTime;
    private LocalTime closeTime;
    private ScheduleRequestDto schedule;

    public CustomHoursRequestDto() {}

    public CustomHoursRequestDto(String name, String description, LocalDate date, LocalTime openTime, LocalTime closeTime, int year) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.schedule = new ScheduleRequestDto(year);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    public void setSchedule(ScheduleRequestDto schedule) {
        this.schedule = schedule;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public LocalTime getOpenTime() {
        return this.openTime;
    }

    public LocalTime getCloseTime() {
        return this.closeTime;
    }

    public ScheduleRequestDto getSchedule() {
        return this.schedule;
    }
}
