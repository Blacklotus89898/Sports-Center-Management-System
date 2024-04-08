package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.SpecificClass;

public class SpecificClassResponseDto {
    int classId;
    ClassTypeResponseDto classType;
    ScheduleResponseDto schedule;
    String specificClassName;
    String description;
    String date;
    String startTime;
    float hourDuration;
    int maxCapacity;
    int currentCapacity;
    double registrationFee;
    byte[] image;

    public SpecificClassResponseDto() {
    }

    public SpecificClassResponseDto(int classId, ClassTypeResponseDto classType, ScheduleResponseDto schedule, String specificClassName, String description, String date, String startTime, float hourDuration, int maxCapacity, int currentCapacity, double registrationFee, byte[] image) {
        this.classId = classId;
        this.classType = classType;
        this.schedule = schedule;
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

    public SpecificClassResponseDto(SpecificClass specificClass) {
        this.classId = specificClass.getClassId();
        this.classType = new ClassTypeResponseDto(specificClass.getClassType());
        this.schedule = new ScheduleResponseDto(specificClass.getSchedule());
        this.specificClassName = specificClass.getSpecificClassName();
        this.description = specificClass.getDescription();
        this.date = specificClass.getDate().toString();
        this.startTime = specificClass.getStartTime().toString();
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

    public ClassTypeResponseDto getClassType() {
        return classType;
    }

    public void setClassType(ClassTypeResponseDto classType) {
        this.classType = classType;
    }

    public ScheduleResponseDto getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleResponseDto schedule) {
        this.schedule = schedule;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public float getHourDuration() {
        return hourDuration;
    }

    public void setHourDuration(float hourDuration) {
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
