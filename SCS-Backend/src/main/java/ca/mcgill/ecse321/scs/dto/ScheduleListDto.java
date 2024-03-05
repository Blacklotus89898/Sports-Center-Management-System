package ca.mcgill.ecse321.scs.dto;

import java.util.List;

public class ScheduleListDto {
    private List<ScheduleResponseDto> schedules;

    public ScheduleListDto(List<ScheduleResponseDto> schedules) {
        this.schedules = schedules;
    }

    public List<ScheduleResponseDto> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ScheduleResponseDto> schedules) {
        this.schedules = schedules;
    }
}
