package ca.mcgill.ecse321.scs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ca.mcgill.ecse321.scs.model.Schedule;

public class ScheduleResponseDto {
    private int year;

    @JsonCreator
    public ScheduleResponseDto(@JsonProperty("year") int year) {
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
