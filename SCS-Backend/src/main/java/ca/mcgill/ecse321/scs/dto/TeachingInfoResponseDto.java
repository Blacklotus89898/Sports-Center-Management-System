package ca.mcgill.ecse321.scs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import ca.mcgill.ecse321.scs.model.TeachingInfo;

public class TeachingInfoResponseDto {
    private int teachingInfoId;
    private InstructorResponseDto instructor; //need full dto objects for response though
    private SpecificClassResponseDto specificClass; //does not exist yet lol


    @JsonCreator
    public TeachingInfoResponseDto(@JsonProperty("teachingInfoId") int teachingInfoId) {
        this.teachingInfoId = teachingInfoId;
    }

    public TeachingInfoResponseDto(TeachingInfo teachingInfo) {
        this.teachingInfoId = teachingInfo.getTeachingInfoId();
    }

    public int getTeachingInfoId() {
        return teachingInfoId;
    }

    public void setTeachingInfoId(int teachingInfoId) {
        this.teachingInfoId = teachingInfoId;
    }
}
