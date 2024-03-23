package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.TeachingInfo;

public class TeachingInfoResponseDto {
    private int teachingInfoId;
    private InstructorResponseDto instructor;
    private SpecificClassResponseDto specificClass;

    public TeachingInfoResponseDto(int teachingInfoId, InstructorResponseDto instructor, SpecificClassResponseDto specificClass) {
        this.teachingInfoId = teachingInfoId;
        this.instructor = instructor;
        this.specificClass = specificClass;
    }

    public TeachingInfoResponseDto(TeachingInfo teachingInfo) {
        this.teachingInfoId = teachingInfo.getTeachingInfoId();
        this.instructor = new InstructorResponseDto(teachingInfo.getInstructor());
        this.specificClass = new SpecificClassResponseDto(teachingInfo.getSpecificClass());
    }

    public int getTeachingInfoId() {
        return teachingInfoId;
    }

    public void setTeachingInfoId(int teachingInfoId) {
        this.teachingInfoId = teachingInfoId;
    }

    public InstructorResponseDto getInstructor() {
        return instructor;
    }

    public void setInstructor(InstructorResponseDto instructor) {
        this.instructor = instructor;
    }

    public SpecificClassResponseDto getSpecificClass() {
        return specificClass;
    }

    public void setSpecificClass(SpecificClassResponseDto specificClass) {
        this.specificClass = specificClass;
    }
}
