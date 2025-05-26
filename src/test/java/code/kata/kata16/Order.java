package code.kata.kata16;

import code.kata.kata17.PaymentMethod;

public record Order(
        Customer customer,
        String productName,
        ProductType productType,
        String salesAgentId,
        PaymentMethod payment
) {
    public Order {
    }

    public Order(Customer customer, String productName, ProductType productType) {
        this(customer, productName, productType, null, PaymentMethod.CREDIT_CARD);
    }

    public Order(Customer customer, String productName, ProductType productType, String salesAgentId) {
        this(customer, productName, productType, salesAgentId, PaymentMethod.CREDIT_CARD);
    }

    public Order(Customer customer, String productName, ProductType productType, PaymentMethod payment) {
        this(customer, productName, productType, null, payment);
    }

    public Order extraItem(String productName, ProductType productType) {
        return new Order(customer(), productName, productType, salesAgentId(), payment());
    }
}
