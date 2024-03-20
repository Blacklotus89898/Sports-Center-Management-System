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

@Service
public class PaymentMethodService {

    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    @Autowired
    CustomerService customerService;

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

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

    @Transactional
    public PaymentMethod getPaymentMethod(int paymentId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findPaymentMethodByPaymentId(paymentId);
        if(paymentMethod == null) {
            throw new SCSException(HttpStatus.NOT_FOUND, "Payment method with ID " + paymentId + " does not exist.");
        }
        return paymentMethod;
    }

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

    @Transactional
    public List<PaymentMethod> getAllPaymentMethods() {
        return ServiceUtils.toList(paymentMethodRepository.findAll());
    }

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

    @Transactional
    public void deletePaymentMethod(int paymentId) {
        PaymentMethod paymentMethod = getPaymentMethod(paymentId); // This method throws if not found
        paymentMethodRepository.delete(paymentMethod);
    }

    @Transactional
    public void deletePaymentMethodByEmail(String customerEmail) {
        List<PaymentMethod> paymentMethods = ServiceUtils.toList(paymentMethodRepository.findAll());
        for (PaymentMethod p : paymentMethods) {
            if (p.getCustomer().getEmail().equals(customerEmail)) {
                paymentMethodRepository.delete(p);
            }
        }
        
        throw new SCSException(HttpStatus.NOT_FOUND, "Payment method with email address " + customerEmail + " does not exist.");
    }

    @Transactional
    public void deleteAllPaymentMethods() {
        paymentMethodRepository.deleteAll();
    }
}
