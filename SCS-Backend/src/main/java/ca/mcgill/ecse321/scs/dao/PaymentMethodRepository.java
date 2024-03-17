package ca.mcgill.ecse321.scs.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.scs.model.PaymentMethod;

public interface PaymentMethodRepository extends CrudRepository<PaymentMethod, Integer> {
    public PaymentMethod getPaymentMethodByPaymentId(int paymentId);
}
