package ca.mcgill.ecse321.scs.controller;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.scs.model.CustomHours;
import ca.mcgill.ecse321.scs.service.CustomHoursService;

import ca.mcgill.ecse321.scs.dto.CustomHoursRequestDto;
import ca.mcgill.ecse321.scs.dto.CustomHoursResponseDto;
import ca.mcgill.ecse321.scs.dto.CustomHoursListDto;

@CrossOrigin(origins = "*")
@RestController
public class CustomHoursController {
    @Autowired
    private CustomHoursService customHoursService;

    @GetMapping(value = { "/customHours", "/customHours/" })
    public CustomHoursListDto getCustomHours() {
        List<CustomHoursResponseDto> customHoursDtos = new ArrayList<>();
        for (CustomHours ch : customHoursService.getAllCustomHours()) {
            customHoursDtos.add(new CustomHoursResponseDto(ch));
        }

        return new CustomHoursListDto(customHoursDtos);
    }

    @GetMapping(value = { "/customHours/{name}", "/customHours/{name}/" })
    public CustomHoursResponseDto getCustomHour(@PathVariable String name) {
        return new CustomHoursResponseDto(customHoursService.getCustomHours(name));
    }

    @GetMapping(value = { "/customHours/date/{date}", "/customHours/date/{date}/" })
    public CustomHoursListDto getCustomHoursByDate(LocalDate date) {
        List<CustomHoursResponseDto> customHoursDtos = new ArrayList<>();
        for (CustomHours ch : customHoursService.getCustomHoursByDate(date)) {
            customHoursDtos.add(new CustomHoursResponseDto(ch));
        }

        return new CustomHoursListDto(customHoursDtos);
    }
    
    @PostMapping(value = { "/customHours", "/customHours/" })
    public CustomHoursResponseDto createCustomHours(@RequestBody CustomHoursRequestDto customHoursDto) {
        String name = customHoursDto.getName();
        String description = customHoursDto.getDescription();
        LocalDate date = customHoursDto.getDate();
        LocalTime openTime = customHoursDto.getOpenTime();
        LocalTime closeTime = customHoursDto.getCloseTime();
        int year = customHoursDto.getSchedule().getYear();

        CustomHours customHours = customHoursService.createCustomHours(name, description, date, openTime, closeTime, year);
        return new CustomHoursResponseDto(customHours);
    }

    @PostMapping(value = { "/customHours/{name}", "/customHours/{name}/" })
    public CustomHoursResponseDto updateCustomHours(@PathVariable String name, @RequestBody CustomHoursRequestDto customHoursDto) {
        String description = customHoursDto.getDescription();
        LocalDate date = customHoursDto.getDate();
        LocalTime openTime = customHoursDto.getOpenTime();
        LocalTime closeTime = customHoursDto.getCloseTime();
        int year = customHoursDto.getSchedule().getYear();

        CustomHours customHours = customHoursService.updateCustomHours(name, description, date, openTime, closeTime, year);
        return new CustomHoursResponseDto(customHours);
    }

    @DeleteMapping(value = { "/customHours/{name}", "/customHours/{name}/" })
    public void deleteCustomHours(@PathVariable String name) {
        customHoursService.deleteCustomHours(name);
    }

    @DeleteMapping(value = { "/customHours", "/customHours/" })
    public void deleteAllCustomHours() {
        customHoursService.deleteAllCustomHours();
    }
}
