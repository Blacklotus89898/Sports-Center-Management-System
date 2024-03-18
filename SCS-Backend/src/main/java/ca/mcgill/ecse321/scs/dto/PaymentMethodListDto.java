package ca.mcgill.ecse321.scs.dto;

import java.util.List;

public class PaymentMethodListDto {
    private List<PaymentMethodResponseDto> paymentMethodResponseDtos;

    // default constructor required for deserialization
    public PaymentMethodListDto() {}

    public PaymentMethodListDto(List<PaymentMethodResponseDto> paymentMethodResponseDtos) {
        this.paymentMethodResponseDtos = paymentMethodResponseDtos;
    }

    public List<PaymentMethodResponseDto> getPaymentMethod() {
        return paymentMethodResponseDtos;
    }

    public void setPaymentMethod(List<PaymentMethodResponseDto> paymentMethodResponseDtos) {
        this.paymentMethodResponseDtos = paymentMethodResponseDtos;
    }
}
