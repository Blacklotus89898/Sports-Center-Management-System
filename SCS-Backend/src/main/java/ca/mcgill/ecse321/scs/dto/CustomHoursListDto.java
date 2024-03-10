package ca.mcgill.ecse321.scs.dto;

import java.util.List;

public class CustomHoursListDto {
    private List<CustomHoursResponseDto> customHours;

    // default constructor required for deserialization
    public CustomHoursListDto() {}

    public CustomHoursListDto(List<CustomHoursResponseDto> customHours) {
        this.customHours = customHours;
    }

    public List<CustomHoursResponseDto> getCustomHours() {
        return customHours;
    }

    public void setCustomHours(List<CustomHoursResponseDto> customHours) {
        this.customHours = customHours;
    }
}
