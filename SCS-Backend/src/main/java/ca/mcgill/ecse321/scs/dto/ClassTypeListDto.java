package ca.mcgill.ecse321.scs.dto;

import java.util.ArrayList;
import java.util.List;

public class ClassTypeListDto {
    private List<ClassTypeResponseDto> classTypes;

    public ClassTypeListDto() {
        this.classTypes = new ArrayList<>();
    }

    public ClassTypeListDto(List<ClassTypeResponseDto> classTypes) {
        this.classTypes = classTypes;
    }

    public List<ClassTypeResponseDto> getClassTypes() {
        return classTypes;
    }

    public void setClassTypes(List<ClassTypeResponseDto> classTypes) {
        this.classTypes = classTypes;
    }
}
