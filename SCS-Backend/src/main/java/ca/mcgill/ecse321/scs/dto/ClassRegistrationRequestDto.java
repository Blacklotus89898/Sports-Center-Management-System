package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.ClassRegistration;

public class ClassRegistrationRequestDto {    
    private int registrationId;
    private int accountId;
    private int classId;

    public ClassRegistrationRequestDto() {}

    public ClassRegistrationRequestDto(int registrationId, int accountId, int classId) {
        this.registrationId = registrationId;
        this.accountId = accountId;
        this.classId = classId;
    }

    public ClassRegistrationRequestDto(ClassRegistration classRegistration) {
        this.registrationId = classRegistration.getRegistrationId();
        this.accountId = classRegistration.getCustomer().getAccountId();
        this.classId = classRegistration.getSpecificClass().getClassId();
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
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
