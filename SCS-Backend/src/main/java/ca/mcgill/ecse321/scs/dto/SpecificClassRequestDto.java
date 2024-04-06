package ca.mcgill.ecse321.scs.dto;

import java.time.LocalTime;

import ca.mcgill.ecse321.scs.model.SpecificClass;

import java.time.LocalDate;

public class SpecificClassRequestDto {
    int classId;
    String classType;
    int year;
    String specificClassName;
    String description;
    LocalDate date;
    LocalTime startTime;
    int hourDuration;
    int maxCapacity;
    int currentCapacity;
    double registrationFee;
    byte[] image;

    public SpecificClassRequestDto() {
    }

    public SpecificClassRequestDto(int classId, String classType, int year, String specificClassName, String description, LocalDate date, LocalTime startTime, int hourDuration, int maxCapacity, int currentCapacity, double registrationFee, byte[] image) {
        this.classId = classId;
        this.classType = classType;
        this.year = year;
        this.specificClassName = specificClassName;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.hourDuration = hourDuration;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.registrationFee = registrationFee;
        this.image = image;
    }

    public SpecificClassRequestDto(SpecificClass specificClass) {
        this.classId = specificClass.getClassId();
        this.classType = specificClass.getClassType().getClassName();
        this.year = specificClass.getSchedule().getYear();
        this.specificClassName = specificClass.getSpecificClassName();
        this.description = specificClass.getDescription();
        this.date = specificClass.getDate().toLocalDate();
        this.startTime = specificClass.getStartTime().toLocalTime();
        this.hourDuration = specificClass.getHourDuration();
        this.maxCapacity = specificClass.getMaxCapacity();
        this.currentCapacity = specificClass.getCurrentCapacity();
        this.registrationFee = specificClass.getRegistrationFee();
        this.image = specificClass.getImage();
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSpecificClassName() {
        return specificClassName;
    }

    public void setSpecificClassName(String specificClassName) {
        this.specificClassName = specificClassName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public int getHourDuration() {
        return hourDuration;
    }

    public void setHourDuration(int hourDuration) {
        this.hourDuration = hourDuration;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public double getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(double registrationFee) {
        this.registrationFee = registrationFee;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
