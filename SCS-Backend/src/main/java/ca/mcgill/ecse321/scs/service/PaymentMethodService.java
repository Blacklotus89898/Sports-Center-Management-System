package ca.mcgill.ecse321.scs.service;

import ca.mcgill.ecse321.scs.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.scs.model.Customer;
import ca.mcgill.ecse321.scs.model.PaymentMethod;
import ca.mcgill.ecse321.scs.exception.SCSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The PaymentMethodService class provides methods for managing payment methods.
 * It interacts with the PaymentMethodRepository and CustomerService
 * to perform CRUD operations on payment methods.
 */
@Service
public class PaymentMethodService {

    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    @Autowired
    CustomerService customerService;

    /**
     * Set the PaymentMethodRepository
     * @param paymentMethodRepository
     */
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Set the CustomerService
     * @param paymentMethodRepository
     */
    @Transactional
    public PaymentMethod createPaymentMethod(long cardNumber, int expiryMonth, int expiryYear, int securityCode, int accountId) {
        
        int securityCodeLength = (int) (Math.log10(securityCode) + 1);
        int cardNumberLength = (int) (Math.log10(cardNumber) + 1);
        int expiryMonthLength = (int) (Math.log10(expiryMonth) + 1);
        int expiryYearLength = (int) (Math.log10(expiryYear) + 1);

        if (securityCodeLength != 3) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Security code must be 3 digits.");
        } else if (cardNumberLength != 16) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Card number must be 16 digits.");
        } else if (expiryMonthLength > 2) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Expiry month must be 2 digits.");
        } else if (expiryMonth > 12 || expiryMonth < 1) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Expiry month must be in the range from 1 to 12.");
        } else if (expiryYearLength != 2) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Expiry year must be 2 digits.");
        } else if (expiryYear < 24) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Expiry year must not be expired.");
        }

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCardNumber(cardNumber);
        paymentMethod.setExpiryMonth(expiryMonth);
        paymentMethod.setExpiryYear(expiryYear);
        paymentMethod.setSecurityCode(securityCode);

        Customer customer = customerService.getCustomerById(accountId);
        paymentMethod.setCustomer(customer);

        // remove the old payment method if it exists
        List<PaymentMethod> paymentMethods = ServiceUtils.toList(paymentMethodRepository.findAll());
        for (PaymentMethod p : paymentMethods) {
            if (p.getCustomer().getAccountId() == accountId) {
                paymentMethodRepository.delete(p);
            }
        }
        
        return paymentMethodRepository.save(paymentMethod);
    }

    /**
     * Get the payment method with the specified ID.
     * @param paymentId the ID of the payment method to retrieve
     * @return the payment method with the specified ID
     * @throws SCSException if the payment method with the specified ID is not found
     */
    @Transactional
    public PaymentMethod getPaymentMethod(int paymentId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findPaymentMethodByPaymentId(paymentId);
        if(paymentMethod == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Payment method with ID " + paymentId + " does not exist.");
        }
        return paymentMethod;
    }

    /**
     * Get the payment method with the specified account ID.
     * @param accountId the account ID of the payment method to retrieve
     * @return the payment method with the specified account ID
     * @throws SCSException if the payment method with the specified account ID is not found
     */
    @Transactional
    public PaymentMethod getPaymentMethodByAccountId(int accountId) {
        List<PaymentMethod> paymentMethods = ServiceUtils.toList(paymentMethodRepository.findAll());
        for (PaymentMethod p : paymentMethods) {
            if (p.getCustomer().getAccountId() == accountId) {
                return p;
            }
        }
        
        throw new SCSException(HttpStatus.NOT_FOUND, "Payment method with account id " + accountId + " does not exist.");
    }

    /**
     * Get all payment methods.
     * @return a list of PaymentMethod objects representing all payment methods
     */
    @Transactional
    public List<PaymentMethod> getAllPaymentMethods() {
        return ServiceUtils.toList(paymentMethodRepository.findAll());
    }

    /**
     * Update the payment method with the specified ID.
     * @param paymentId the ID of the payment method to update
     * @param cardNumber the new card number
     * @param expiryMonth the new expiry month
     * @param expiryYear the new expiry year
     * @param securityCode the new security code
     * @param accountId the account ID of the payment method to update
     * @return the updated payment method
     */
    @Transactional
    public PaymentMethod updatePaymentMethod(int paymentId, long cardNumber, int expiryMonth, int expiryYear, int securityCode, int accountId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findPaymentMethodByPaymentId(paymentId);
        if (paymentMethod == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Payment method with ID " + paymentId + " does not exist.");
        }

        int securityCodeLength = (int) (Math.log10(securityCode) + 1);
        int cardNumberLength = (int) (Math.log10(cardNumber) + 1);
        int expiryMonthLength = (int) (Math.log10(expiryMonth) + 1);
        int expiryYearLength = (int) (Math.log10(expiryYear) + 1);

        if (securityCodeLength != 3) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Security code must be 3 digits.");
        } else if (cardNumberLength != 16) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Card number must be 16 digits.");
        } else if (expiryMonthLength > 2) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Expiry month must be 2 digits.");
        } else if (expiryMonth > 12 || expiryMonth < 1) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Expiry month must be in the range from 1 to 12.");
        } else if (expiryYearLength != 2) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Expiry year must be 2 digits.");
        } else if (expiryYear < 24) {
            throw new SCSException(HttpStatus.BAD_REQUEST, "Expiry year must not be expired.");
        }

        paymentMethod.setCardNumber(cardNumber);
        paymentMethod.setExpiryMonth(expiryMonth);
        paymentMethod.setExpiryYear(expiryYear);
        paymentMethod.setSecurityCode(securityCode);
        
        return paymentMethodRepository.save(paymentMethod);
    }

    /**
     * Delete the payment method with the specified ID.
     * @param paymentId the ID of the payment method to delete
     */
    @Transactional
    public void deletePaymentMethod(int paymentId) {
        PaymentMethod paymentMethod = getPaymentMethod(paymentId); // This method throws if not found
        paymentMethodRepository.delete(paymentMethod);
    }

    /**
     * Delete all payment methods.
     */
    @Transactional
    public void deleteAllPaymentMethods() {
        paymentMethodRepository.deleteAll();
    }
}
