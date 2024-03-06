package ca.mcgill.ecse321.scs.service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.scs.dao.CustomHoursRepository;
import ca.mcgill.ecse321.scs.model.CustomHours;

import ca.mcgill.ecse321.scs.model.Schedule;

@Service
public class CustomHoursService {
    @Autowired
    CustomHoursRepository customHoursRepository;
    @Autowired
    ScheduleService scheduleService;

    @Transactional
    public CustomHours createCustomHours(String name, String description, LocalDate date, LocalTime openTime, LocalTime closeTime, int year) {
        CustomHours customHours = new CustomHours();
        customHours.setName(name);
        customHours.setDescription(description);
        customHours.setDate(Date.valueOf(date));
        customHours.setOpenTime(Time.valueOf(openTime));
        customHours.setCloseTime(Time.valueOf(closeTime));

        Schedule schedule = scheduleService.getSchedule(year);
        customHours.setSchedule(schedule);

        customHoursRepository.save(customHours);
        return customHours;
    }

    @Transactional
    public CustomHours updateCustomHours(String name, String description, LocalDate date, LocalTime openTime, LocalTime closeTime, int year) {
        CustomHours customHours = customHoursRepository.findCustomHoursByName(name);
        customHours.setDescription(description);
        customHours.setDate(Date.valueOf(date));
        customHours.setOpenTime(Time.valueOf(openTime));
        customHours.setCloseTime(Time.valueOf(closeTime));

        Schedule schedule = scheduleService.getSchedule(year);
        customHours.setSchedule(schedule);

        customHoursRepository.save(customHours);
        return customHours;
    }

    @Transactional
    public CustomHours getCustomHours(String name) {
        return customHoursRepository.findCustomHoursByName(name);
    }

    @Transactional
    public List<CustomHours> getCustomHoursByDate(LocalDate date) {
        List<CustomHours> customHours = ServiceUtils.toList(customHoursRepository.findAll());
        List<CustomHours> customHoursByDate = new ArrayList<CustomHours>();
        for (CustomHours ch : customHours) {
            if (ch.getDate().equals(Date.valueOf(date))) {
                customHoursByDate.add(ch);
            }
        }
        return customHoursByDate;
    }

    @Transactional
    public List<CustomHours> getAllCustomHours() {
        return ServiceUtils.toList(customHoursRepository.findAll());
    }

    @Transactional
    public void deleteCustomHours(String name) {
        CustomHours customHours = customHoursRepository.findCustomHoursByName(name);
        customHoursRepository.delete(customHours);
    }

    @Transactional
    public void deleteAllCustomHours() {
        customHoursRepository.deleteAll();
    }
}
