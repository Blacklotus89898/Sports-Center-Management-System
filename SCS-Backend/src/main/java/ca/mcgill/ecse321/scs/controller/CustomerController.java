package ca.mcgill.ecse321.scs.controller;

import ca.mcgill.ecse321.scs.dto.CustomerDto;
import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import ca.mcgill.ecse321.scs.dto.ErrorDto;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /*  
     * @return all customers
     */
    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieves all customers")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of customers")
    @ApiResponse(responseCode = "404", description = "No customers found",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomer().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    /*
     * @param id
     * @return the found customer
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get customer by id", description = "Retrieves the customer with the specified id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of customer")
    @ApiResponse(responseCode = "404", description = "Customer not found with the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getCustomerById(@PathVariable Integer id) {
        return convertToDto(customerService.getCustomerById(id));
    }

    /*
     * @param email
     * @return the created customer
     */
    @PostMapping
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
    @PutMapping("/{id}")
    @Operation(summary = "Update customer by id", description = "Updates the customer with the specified id")
    @ApiResponse(responseCode = "200", description = "Successful update of customer")
    @ApiResponse(responseCode = "400", description = "Invalid input for updating customer",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "404", description = "Customer not found with the specified id",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDto.class))) 
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto updateCustomerById(@PathVariable Integer id, @RequestBody CustomerDto customerDto) {
        return convertToDto(customerService.updateCustomerById(id, customerDto.getName(), customerDto.getEmail(),
        customerDto.getPassword()));
    }

    /*
     * @param id
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer by id", description = "Deletes the customer with the specified id")
    @ApiResponse(responseCode = "204", description = "Successful deletion of customer")
    @ApiResponse(responseCode = "404", description = "Customer not found with the specified id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Integer id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
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
        return new CustomerDto(customer.getAccountId(), customer.getName(), customer.getEmail(), customer.getPassword(),
        customer.getCreationDate());
    }
}
