package ca.mcgill.ecse321.scs.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.scs.service.CustomHoursService;

@CrossOrigin(origins = "*")
@RestController
public class CustomHoursController {
    @Autowired
    private CustomHoursService customHoursService;

}
