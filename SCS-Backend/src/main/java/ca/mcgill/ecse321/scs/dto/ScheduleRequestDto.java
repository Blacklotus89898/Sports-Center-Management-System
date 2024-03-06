package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.Schedule;

public class ScheduleRequestDto {
    private int year;

    @SuppressWarnings("unused")
    private ScheduleRequestDto() {}

    public ScheduleRequestDto(Schedule schedule) {
        this.year = schedule.getYear();
    }

    public ScheduleRequestDto(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
