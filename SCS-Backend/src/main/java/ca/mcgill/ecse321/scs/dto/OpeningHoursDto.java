package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.OpeningHours.DayOfWeek;

public class OpeningHoursDto {
    private DayOfWeek dayOfWeek;
    private String openTime;
    private String closeTime;
    private int year;

    public OpeningHoursDto() {}

    public OpeningHoursDto(DayOfWeek dayOfWeek, String openTime, String closeTime, int year) {
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.year = year;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}