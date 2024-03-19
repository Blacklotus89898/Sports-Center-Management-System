package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.TeachingInfo;

public class TeachingInfoRequestDto {
    private int teachingInfoId;
    private int instructorId;
    private int specificClassId; //only ids needed for request dto

    public TeachingInfoRequestDto() {}

    public TeachingInfoRequestDto(TeachingInfo teachingInfo) {
        this.teachingInfoId = teachingInfo.getTeachingInfoId();
    }

    public TeachingInfoRequestDto(int teachingInfoId) {
        this.teachingInfoId = teachingInfoId;
    }

    public int getTeachingInfoId() {
        return teachingInfoId;
    }

    public void setTeachingInfoId(int teachingInfoId) {
        this.teachingInfoId = teachingInfoId;
    }
}
