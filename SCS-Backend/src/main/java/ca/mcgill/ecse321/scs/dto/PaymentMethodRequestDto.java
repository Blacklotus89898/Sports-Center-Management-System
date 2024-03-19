package ca.mcgill.ecse321.scs.dto;

public class PaymentMethodRequestDto {
    private long cardNumber;
    private int expiryMonth;
    private int expiryYear;
    private int securityCode;
    private int paymentId;

    private CustomerDto customer;

    public PaymentMethodRequestDto() {}

    public PaymentMethodRequestDto(long cardNumber, int expiryMonth, int expiryYear, int securityCode, int paymentId, int accountId) {
        this.cardNumber = cardNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.securityCode = securityCode;
        this.paymentId = paymentId;
        this.customer = new CustomerDto(accountId, null, null, null, null);
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
