package ca.mcgill.ecse321.scs.dto;

import java.util.ArrayList;
import java.util.List;

public class SpecificClassListDto {
    private List<SpecificClassResponseDto> specificClass;

    public SpecificClassListDto() {
        this.specificClass = new ArrayList<>();
    }

    public SpecificClassListDto(List<SpecificClassResponseDto> specificClass) {
        this.specificClass = specificClass;
    }

    public List<SpecificClassResponseDto> getSpecificClasses() {
        return specificClass;
    }

    public void setSpecificClasses(List<SpecificClassResponseDto> specificClass) {
        this.specificClass = specificClass;
    }
}
