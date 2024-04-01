package ca.mcgill.ecse321.scs.dto;

import java.util.ArrayList;
import java.util.List;

public class ClassRegistrationListDto {
    private List<ClassRegistrationResponseDto> classRegistrations;

    public ClassRegistrationListDto() {
        this.classRegistrations = new ArrayList<>();
    }

    public ClassRegistrationListDto(List<ClassRegistrationResponseDto> classRegistrations) {
        this.classRegistrations = classRegistrations;
    }

    public List<ClassRegistrationResponseDto> getClassRegistrations() {
        return classRegistrations;
    }

    public void setClassRegistrations(List<ClassRegistrationResponseDto> classRegistrations) {
        this.classRegistrations = classRegistrations;
    }
}
