package ca.mcgill.ecse321.scs.controller;

import ca.mcgill.ecse321.scs.dto.OpeningHoursDto;
import ca.mcgill.ecse321.scs.model.OpeningHours;
import ca.mcgill.ecse321.scs.service.OpeningHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;

@RestController
@RequestMapping("/openingHours")
public class OpeningHoursController {

    @Autowired
    private OpeningHoursService openingHoursService;

    /**
     * create opening hours
     * @param openingHoursDto
     * @return the created opening hours
     */
    @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
    public OpeningHoursDto createOpeningHours(@RequestBody OpeningHoursDto openingHoursDto) {
        OpeningHoursDto createdOpeningHours = new OpeningHoursDto(openingHoursService.createOpeningHours(
            openingHoursDto.getDayOfWeek().toString(),
            openingHoursDto.getOpenTime(),
            openingHoursDto.getCloseTime(),
            openingHoursDto.getYear()
        ));

        return createdOpeningHours;
    }

    /**
     * get opening hours by day
     * @param day
     * @return  the found opening hours
     */
    @GetMapping("/{day}")
    public OpeningHoursDto getOpeningHoursByDay(@PathVariable String day) {
        OpeningHours openingHours = openingHoursService.getOpeningHoursByDay(day);
        OpeningHoursDto openingHoursDto = new OpeningHoursDto(openingHours);
        return openingHoursDto;
    }

    /**
     * update opening hours
     * @param openingHoursDto
     * @return the updated opening hours
     */
    @PutMapping("/{day}")
    public OpeningHoursDto updateOpeningHours(@RequestBody OpeningHoursDto openingHoursDto, @PathVariable String day) {
        OpeningHoursDto updatedOpeningHours = new OpeningHoursDto(openingHoursService.updateOpeningHours(
                LocalTime.parse(openingHoursDto.getOpenTime().toString()),
                openingHoursDto.getCloseTime(),
                openingHoursDto.getYear(),
                day
        ));
        return updatedOpeningHours;
    }

    /**
     * delete opening hours by day
     * @param day
     */
    @DeleteMapping("/{day}")
    public void deleteOpeningHours(@PathVariable String day) {
        openingHoursService.deleteOpeningHours(day);
    }
    /**
     * delete all opening hours
     */
    @DeleteMapping
    public void deleteAllOpeningHours() {
        openingHoursService.deleteAllOpeningHours();
    }
}