package ca.mcgill.ecse321.scs.dto;

import java.util.ArrayList;
import java.util.List;

public class CustomerListDto {
    private List<CustomerDto> customers;

    public CustomerListDto() {
        this.customers = new ArrayList<>();
    }

    public CustomerListDto(List<CustomerDto> customers) {
        this.customers = customers;
    }

    public List<CustomerDto> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerDto> customers) {
        this.customers = customers;
    }
}
