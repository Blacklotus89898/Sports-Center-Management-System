package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.ClassRegistration;

public class ClassRegistrationResponseDto {
    private int registrationId;
    private CustomerDto customer;
    private SpecificClassResponseDto specificClass;
    private int classId;


    public ClassRegistrationResponseDto(int registrationId, CustomerDto customer, SpecificClassResponseDto specificClass) {
        this.registrationId = registrationId;
        this.customer = customer;
        this.specificClass = specificClass;
    }

    public ClassRegistrationResponseDto(ClassRegistration classRegistration) {
        this.registrationId = classRegistration.getRegistrationId();
        this.customer = new CustomerDto(classRegistration.getCustomer());
        this.specificClass = new SpecificClassResponseDto(classRegistration.getSpecificClass());
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(int registrationId) {
        this.registrationId = registrationId;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public SpecificClassResponseDto getSpecificClass() {
        return specificClass;
    }

    public void setSpecificClass(SpecificClassResponseDto specificClass) {
        this.specificClass = specificClass;
    }
}
