package ca.mcgill.ecse321.scs.dto;

public class ScheduleRequestDto {
    private int year;

    @SuppressWarnings("unused")
    private ScheduleRequestDto() {}

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
