package ca.mcgill.ecse321.scs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.service.SCSService;

@RestController
public class SCSRestController {
    
    @Autowired
    private SCSService scsService;

    @GetMapping("customers")
    public List<Customer> getAllCustomers() {
        return scsService.findAllCustomers();
    }

    // @PostMapping("customers")
    // public List<Customer> getAllCustomers() {
    //     return scsService.findAllCustomers();
    // }

}
