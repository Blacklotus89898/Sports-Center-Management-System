package ca.mcgill.ecse321.scs.dto;

import java.sql.Date;
import java.sql.Time;

public class CustomHoursDto {
    private String name;
    private String description;
    private Date date;
    private Time openTime;
    private Time closeTime;


    public CustomHoursDto() {}

    public CustomHoursDto(String name, String description, Date date, Time openTime, Time closeTime) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public Time getOpenTime() {
        return openTime;
    }

    public Time getCloseTime() {
        return closeTime;
    }
}
