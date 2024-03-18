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

@CrossOrigin(origins = "*")
@RestController
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;

    /**
     * get payment method by card number
     * 
     * @param cardNumber
     * @return the found payment method
     */
    @GetMapping(value = { "/paymentMethod/cardNumber/{cardNumber}", "/paymentMethod/cardNumber/{cardNumber}/" })
    public PaymentMethodResponseDto getPaymentMethodById(@PathVariable int cardNumber) {
        return new PaymentMethodResponseDto(paymentMethodService.getPaymentMethodByCardNumber(cardNumber));
    }
    
    /**
     * create payment method
     * @param paymentMethodDto
     * @return the created payment method
     */
    @PostMapping(value = { "/paymentMethod", "/paymentMethod/" })
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentMethodResponseDto createPaymentMethod(@RequestBody PaymentMethodRequestDto paymentMethodDto) {
        long cardNumber = paymentMethodDto.getCardNumber();
        int expiryMonth = paymentMethodDto.getExpiryMonth();
        int expiryYear = paymentMethodDto.getExpiryYear();
        int securityCode = paymentMethodDto.getSecurityCode();
        int paymentId = paymentMethodDto.getPaymentId();
        int accountId = paymentMethodDto.getCustomer().getId();

        PaymentMethod paymentMethod = paymentMethodService.createPaymentMethod(cardNumber, expiryMonth, expiryYear, securityCode, paymentId, accountId);
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
    public PaymentMethodResponseDto updatePaymentMethod(@PathVariable int paymentId, @RequestBody PaymentMethodRequestDto paymentMethodDto) {
        long cardNumber = paymentMethodDto.getCardNumber();
        int expiryMonth = paymentMethodDto.getExpiryMonth();
        int expiryYear = paymentMethodDto.getExpiryYear();
        int securityCode = paymentMethodDto.getSecurityCode();
        int accountId = paymentMethodDto.getCustomer().getId();

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
    public void deletePaymentMethod(@PathVariable int paymentId) {
        paymentMethodService.deletePaymentMethod(paymentId);
    }

    /**
     * delete all payment methods
     * 
     */
    @DeleteMapping(value = { "/paymentMethod/", "/paymentMethod" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllPaymentMethods() {
        paymentMethodService.deleteAllPaymentMethods();
    }
}
