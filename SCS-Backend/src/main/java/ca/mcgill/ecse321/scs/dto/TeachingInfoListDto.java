package ca.mcgill.ecse321.scs.dto;

import java.util.ArrayList;
import java.util.List;

public class TeachingInfoListDto {
    private List<TeachingInfoResponseDto> teachingInfos;

    public TeachingInfoListDto() {
        this.teachingInfos = new ArrayList<>();
    }

    public TeachingInfoListDto(List<TeachingInfoResponseDto> teachingInfos) {
        this.teachingInfos = teachingInfos;
    }

    public List<TeachingInfoResponseDto> getTeachingInfos() {
        return teachingInfos;
    }

    public void setTeachingInfos(List<TeachingInfoResponseDto> teachingInfos) {
        this.teachingInfos = teachingInfos;
    }
}
