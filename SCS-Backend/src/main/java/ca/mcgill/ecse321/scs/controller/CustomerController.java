package ca.mcgill.ecse321.scs.controller;

import ca.mcgill.ecse321.scs.dto.CustomerDto;
import ca.mcgill.ecse321.scs.dto.CustomerListDto;
import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import ca.mcgill.ecse321.scs.dto.ErrorDto;
import org.springframework.http.HttpStatus;

@RestController
@Tag(name = "Customers", description = "Endpoints for managing customers.")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /*  
     * @return all customers
     */
    @GetMapping(value = { "/customers", "/customers/" })
    @Operation(summary = "Get all customers", description = "Retrieves all customers")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of customers")
    @ApiResponse(responseCode = "404", description = "No customers found",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.OK)
    public CustomerListDto getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomer();
        List<CustomerDto> customerListDto = new ArrayList<>();
        for (Customer customer : customers) {
            customerListDto.add(new CustomerDto(customer.getAccountId(), customer.getName(), customer.getEmail(), customer.getPassword()));
        }
        return new CustomerListDto(customerListDto);
    }

    /*
     * @param id
     * @return the found customer
     */
    @GetMapping(value = { "/customers/{id}", "/customers/{id}/" })
    @Operation(summary = "Get customer by id", description = "Retrieves the customer with the specified id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of customer")
    @ApiResponse(responseCode = "404", description = "Customer not found with the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getCustomerById(@PathVariable int id) {
        return convertToDto(customerService.getCustomerById(id));
    }

    /*
     * @param email
     * @return the created customer
     */
    @PostMapping(value = { "/customers", "/customers/" })
    @Operation(summary = "Create customer", description = "Creates new customer")
    @ApiResponse(responseCode = "201", description = "Successful creation of customer")
    @ApiResponse(responseCode = "409", description = "Email account already exists",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto) {
        return convertToDto(customerService.createCustomer(customerDto.getName(), customerDto.getEmail(),
        customerDto.getPassword()));
    }

    /*
     * @param id
     * @param customerDto
     * @return the updated customer
     */
    @PutMapping(value = { "/customers/{id}", "/customers/{id}/" })
    @Operation(summary = "Update customer by id", description = "Updates the customer with the specified id")
    @ApiResponse(responseCode = "200", description = "Successful update of customer")
    @ApiResponse(responseCode = "400", description = "Invalid input for updating customer",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found with the specified id",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class))) 
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto updateCustomerById(@PathVariable int id, @RequestBody CustomerDto customerDto) {
        return convertToDto(customerService.updateCustomerById(id, customerDto.getName(), customerDto.getEmail(),
        customerDto.getPassword()));
    }

    /*
     * @param id
     * @return no content
     */
    @DeleteMapping(value = { "/customers/{id}", "/customers/{id}/" })
    @Operation(summary = "Delete customer by id", description = "Deletes the customer with the specified id")
    @ApiResponse(responseCode = "204", description = "Successful deletion of customer")
    @ApiResponse(responseCode = "404", description = "Customer not found with the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCustomerById(@PathVariable int id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = { "/customers", "/customers/" })
    @Operation(summary = "Delete all customers", description = "Deletes all customers")
    @ApiResponse(responseCode = "204", description = "Successful deletion of all customers")
    @ApiResponse(responseCode = "404", description = "No customers found",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllCustomers() {
        customerService.deleteAllCustomers();
    }

    private CustomerDto convertToDto(Customer customer) {
        return new CustomerDto(customer.getAccountId(), customer.getName(), customer.getEmail(), customer.getPassword());
    }
}