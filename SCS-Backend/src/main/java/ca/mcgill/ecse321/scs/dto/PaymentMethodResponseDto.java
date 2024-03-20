package ca.mcgill.ecse321.scs.dto;

import ca.mcgill.ecse321.scs.model.PaymentMethod;

public class PaymentMethodResponseDto {
    private long cardNumber;
    private int expiryMonth;
    private int expiryYear;
    private int securityCode;
    private int paymentId;

    private CustomerDto customer;

    public PaymentMethodResponseDto() {}

    public PaymentMethodResponseDto(PaymentMethod paymentMethod) {
        this.cardNumber = paymentMethod.getCardNumber();
        this.expiryMonth = paymentMethod.getExpiryMonth();
        this.expiryYear = paymentMethod.getExpiryYear();
        this.securityCode = paymentMethod.getSecurityCode();
        this.paymentId = paymentMethod.getPaymentId();

        this.customer = new CustomerDto(paymentMethod.getCustomer().getAccountId(), paymentMethod.getCustomer().getName(), paymentMethod.getCustomer().getEmail(), paymentMethod.getCustomer().getPassword());
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpiryMonth(int expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public void setExpiryYear(int expiryYear) {
        this.expiryYear = expiryYear;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public long getCardNumber() {
        return this.cardNumber;
    }

    public int getExpiryMonth() {
        return this.expiryMonth;
    }

    public int getExpiryYear() {
        return this.expiryYear;
    }

    public int getSecurityCode() {
        return this.securityCode;
    }

    public int getPaymentId() {
        return this.paymentId;
    }

    public CustomerDto getCustomer() {
        return this.customer;
    }
}
