package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.TeachingInfo;

public class TeachingInfoRequestDto {    
    private int teachingInfoId;
    private int accountId;
    private int classId;

    public TeachingInfoRequestDto() {}

    public TeachingInfoRequestDto(int teachingInfoId, int accountId, int classId) {
        this.teachingInfoId = teachingInfoId;
        this.accountId = accountId;
        this.classId = classId;
    }

    public TeachingInfoRequestDto(TeachingInfo teachingInfo) {
        this.teachingInfoId = teachingInfo.getTeachingInfoId();
        this.accountId = teachingInfo.getInstructor().getAccountId();
        this.classId = teachingInfo.getSpecificClass().getClassId();
    }

    public int getTeachingInfoId() {
        return teachingInfoId;
    }

    public void setTeachingInfoId(int teachingInfoId) {
        this.teachingInfoId = teachingInfoId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
