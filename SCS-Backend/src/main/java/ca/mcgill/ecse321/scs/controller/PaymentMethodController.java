package ca.mcgill.ecse321.scs.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.scs.model.PaymentMethod;
import ca.mcgill.ecse321.scs.service.PaymentMethodService;
import ca.mcgill.ecse321.scs.dto.PaymentMethodRequestDto;
import ca.mcgill.ecse321.scs.dto.PaymentMethodResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import ca.mcgill.ecse321.scs.dto.ErrorDto;

@CrossOrigin(origins = "*")
@RestController
@Tag(name = "PaymentMethod", description = "Endpoints for managing payment method.")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;
    
    /**
     * create payment method
     * @param paymentMethodDto
     * @return the created payment method
     */
    @PostMapping(value = { "/paymentMethod", "/paymentMethod/" })
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create payment method", description = "Creates new payment method")
    @ApiResponse(responseCode = "201", description = "Successful creation of payment method")
    @ApiResponse(responseCode = "400", description = "Invalid input for creating payment method", 
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public PaymentMethodResponseDto createPaymentMethod(@RequestBody PaymentMethodRequestDto paymentMethodRequestDto) {
        long cardNumber = paymentMethodRequestDto.getCardNumber();
        int expiryMonth = paymentMethodRequestDto.getExpiryMonth();
        int expiryYear = paymentMethodRequestDto.getExpiryYear();
        int securityCode = paymentMethodRequestDto.getSecurityCode();
        int accountId = paymentMethodRequestDto.getAccountId();

        PaymentMethod paymentMethod = paymentMethodService.createPaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, accountId);
        return new PaymentMethodResponseDto(paymentMethod);
    }

    /**
     * update payment method
     * 
     * @param paymentId
     * @param paymentMethodDto
     * @return the updated payment method
     */
    @PutMapping(value = { "/paymentMethod/{paymentId}", "/paymentMethod/{paymentId}/" })
    @Operation(summary = "Update payment method", description = "Updates the payment method with the specified payment id")
    @ApiResponse(responseCode = "200", description = "Successful update of payment method")
    @ApiResponse(responseCode = "400", description = "Invalid input for updating payment method",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "404", description = "Payment method not found with the specified payment id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public PaymentMethodResponseDto updatePaymentMethod(@PathVariable int paymentId, @RequestBody PaymentMethodRequestDto paymentMethodRequestDto) {
        long cardNumber = paymentMethodRequestDto.getCardNumber();
        int expiryMonth = paymentMethodRequestDto.getExpiryMonth();
        int expiryYear = paymentMethodRequestDto.getExpiryYear();
        int securityCode = paymentMethodRequestDto.getSecurityCode();
        int accountId = paymentMethodRequestDto.getAccountId();

        PaymentMethod paymentMethod = paymentMethodService.updatePaymentMethod(paymentId, cardNumber, expiryMonth, expiryYear, securityCode, accountId);
        return new PaymentMethodResponseDto(paymentMethod);
    }

    /**
     * get payment method with account id
     * 
     * @param accountId
     * @return payment method associated with a specific account
     */
    @GetMapping(value = { "/customers/{accountId}/paymentMethod", "/customers/{accountId}/paymentMethod/" })
    @Operation(summary = "Get payment method by account id for a customer", description = "Retrieves the payment method with the specified account id for the specified customer")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of payment method")
    @ApiResponse(responseCode = "404", description = "Payment method not found with the specified account id for the specified customer",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public PaymentMethodResponseDto getPaymentMethodByAccountId(@PathVariable int accountId) {
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodByAccountId(accountId);
        return new PaymentMethodResponseDto(paymentMethod);
    }

    /**
     * get payment method by payment id
     * 
     * @param paymentId
     * @return the found payment method
     */
    @GetMapping(value = { "/paymentMethod/{paymentId}", "/paymentMethod/{paymentId}/" })
    @Operation(summary = "Get payment method by payment id", description = "Retrieves the payment method for the specified payment id")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of payment method")
    @ApiResponse(responseCode = "404", description = "Payment method not found for the specified payment id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public PaymentMethodResponseDto getPaymentMethodByPaymentId(@PathVariable int paymentId) {
        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethod(paymentId);
        return new PaymentMethodResponseDto(paymentMethod);
    }

    /**
     * delete payment method by payment id
     * 
     * @param paymentId
     */
    @DeleteMapping(value = { "/paymentMethod/{paymentId}", "/paymentMethod/{paymentId}/" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete payment method by a payment id", description = "Deletes the payment method with the specified payment id")
    @ApiResponse(responseCode = "204", description = "Successful deletion of custom hours")
    @ApiResponse(responseCode = "404", description = "Payment method not found with the specified payment id",
                 content = @Content(mediaType = "application/json",
                 schema = @Schema(implementation = ErrorDto.class)))
    public void deletePaymentMethod(@PathVariable int paymentId) {
        paymentMethodService.deletePaymentMethod(paymentId);
    }

    /**
     * delete all payment methods
     * 
     */
    @DeleteMapping(value = { "/paymentMethod/", "/paymentMethod" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete all payment method", description = "Deletes all payment method in the repo")
    @ApiResponse(responseCode = "204", description = "Successful deletion of all payment methods")
    public void deleteAllPaymentMethods() {
        paymentMethodService.deleteAllPaymentMethods();
    }
}
