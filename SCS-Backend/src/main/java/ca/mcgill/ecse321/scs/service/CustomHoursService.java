package ca.mcgill.ecse321.scs.service;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.scs.dao.CustomHoursRepository;
import ca.mcgill.ecse321.scs.model.CustomHours;

@Service
public class CustomHoursService {
    @Autowired
    CustomHoursRepository customHoursRepository;

    @Transactional
    public CustomHours createCustomHours(Date date, Time openTime, Time closeTime) {
        CustomHours customHours = new CustomHours();
        customHours.setDate(date);
        customHours.setOpenTime(openTime);
        customHours.setCloseTime(closeTime);
        customHoursRepository.save(customHours);
        return customHours;
    }

    @Transactional
    public CustomHours updateCustomHours(String name, Date date, Time openTime, Time closeTime) {
        CustomHours customHours = customHoursRepository.findCustomHoursByName(name);
        customHours.setDate(date);
        customHours.setOpenTime(openTime);
        customHours.setCloseTime(closeTime);
        customHoursRepository.save(customHours);
        return customHours;
    }

    @Transactional
    public CustomHours getCustomHours(String name) {
        return customHoursRepository.findCustomHoursByName(name);
    }

    @Transactional
    public List<CustomHours> getCustomHoursByDate(Date date) {
        List<CustomHours> customHours = ServiceUtils.toList(customHoursRepository.findAll());
        List<CustomHours> customHoursByDate = new ArrayList<CustomHours>();
        for (CustomHours ch : customHours) {
            if (ch.getDate().equals(date)) {
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
