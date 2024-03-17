package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.ClassType;

public class ClassTypeResponseDto {
    private String className;
    private String description;
    private boolean isApproved;

    public ClassTypeResponseDto() {}

    public ClassTypeResponseDto(ClassType classType) {
        this.className = classType.getClassName();
        this.description = classType.getDescription();
        this.isApproved = classType.getIsApproved();
    }

    public ClassTypeResponseDto(String className, String description, boolean isApproved) {
        this.className = className;
        this.description = description;
        this.isApproved = isApproved;
    }

    public String getClassName() {
        return this.className;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getIsApproved() {
        return this.isApproved;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }
}
