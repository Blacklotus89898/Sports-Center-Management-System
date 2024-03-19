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

    // override to string
    @Override
    public String toString() {
        String classTypeList = "";
        for (ClassTypeResponseDto classType : classTypes) {
            classTypeList += classType.getClassName() + " " + classType.getDescription() + " " + classType.getIsApproved() + "\n";
        }
        return classTypeList;
    }
}
