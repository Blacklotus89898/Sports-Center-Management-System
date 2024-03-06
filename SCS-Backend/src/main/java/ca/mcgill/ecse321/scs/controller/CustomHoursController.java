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
import org.springframework.web.bind.annotation.PutMapping;
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

    /**
     * get all custom hours
     * 
     * @return list of all the custom hours
     */
    @GetMapping(value = { "/customHours", "/customHours/" })
    public CustomHoursListDto getCustomHours() {
        List<CustomHoursResponseDto> customHoursDtos = new ArrayList<>();
        for (CustomHours ch : customHoursService.getAllCustomHours()) {
            customHoursDtos.add(new CustomHoursResponseDto(ch));
        }

        return new CustomHoursListDto(customHoursDtos);
    }

    /**
     * get custom hours by name
     * 
     * @param name
     * @return the found custom hours
     */
    @GetMapping(value = { "/customHours/{name}", "/customHours/{name}/" })
    public CustomHoursResponseDto getCustomHour(@PathVariable String name) {
        return new CustomHoursResponseDto(customHoursService.getCustomHours(name));
    }

    /**
     * get custom hours by date
     * 
     * @param date
     * @return the found custom hour
     */
    @GetMapping(value = { "/customHours/date/{date}", "/customHours/date/{date}/" })
    public CustomHoursResponseDto getCustomHoursByDate(@PathVariable LocalDate date) {
        // there can only be 1 custom hours for a given date
        return new CustomHoursResponseDto(customHoursService.getCustomHoursByDate(date));
    }
    
    /**
     * create custom hours
     * 
     * @param customHoursDto
     * @return the created custom hours
     */
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

    /**
     * update custom hours
     * 
     * @param name
     * @param customHoursDto
     * @return the updated custom hours
     */
    @PutMapping(value = { "/customHours/{name}", "/customHours/{name}/" })
    public CustomHoursResponseDto updateCustomHours(@PathVariable String name, @RequestBody CustomHoursRequestDto customHoursDto) {
        String description = customHoursDto.getDescription();
        LocalDate date = customHoursDto.getDate();
        LocalTime openTime = customHoursDto.getOpenTime();
        LocalTime closeTime = customHoursDto.getCloseTime();
        int year = customHoursDto.getSchedule().getYear();

        CustomHours customHours = customHoursService.updateCustomHours(name, description, date, openTime, closeTime, year);
        return new CustomHoursResponseDto(customHours);
    }

    /**
     * delete custom hours by name
     * 
     * @param name
     */
    @DeleteMapping(value = { "/customHours/{name}", "/customHours/{name}/" })
    public void deleteCustomHours(@PathVariable String name) {
        customHoursService.deleteCustomHours(name);
    }

    /**
     * delete all custom hours
     */
    @DeleteMapping(value = { "/customHours", "/customHours/" })
    public void deleteAllCustomHours() {
        customHoursService.deleteAllCustomHours();
    }
}
