package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.OpeningHours;
import ca.mcgill.ecse321.scs.model.OpeningHours.DayOfWeek;
import java.time.LocalTime;

public class OpeningHoursDto {
    private DayOfWeek dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private int year;

    public OpeningHoursDto() {}

    // public OpeningHoursDto(DayOfWeek dayOfWeek, String openTime, String closeTime, int year) {
    //     this.dayOfWeek = dayOfWeek;
    //     this.openTime = openTime;
    //     this.closeTime = closeTime;
    //     this.year = year;
    // }

    public OpeningHoursDto(OpeningHours openingHours){
        this.dayOfWeek = openingHours.getDayOfWeek();
        this.openTime = openingHours.getOpenTime().toLocalTime();
        this.closeTime = openingHours.getCloseTime().toLocalTime();
        this.year = openingHours.getSchedule().getYear();
    }
    
    public OpeningHoursDto(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime, int year) {
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

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}