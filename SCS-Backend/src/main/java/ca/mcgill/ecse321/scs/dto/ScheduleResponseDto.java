package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.Schedule;

public class ScheduleResponseDto {
    private int year;

    public ScheduleResponseDto(int year) {
        this.year = year;
    }

    public ScheduleResponseDto(Schedule schedule) {
        this.year = schedule.getYear();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
