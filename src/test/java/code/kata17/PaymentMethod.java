package code.kata17;

public enum PaymentMethod {

    CREDIT_CARD,
    PREPAID,
    INVOICE,
    ;

    public OrderMethod getOrderMethod() {
        return this == INVOICE ? OrderMethod.PURCHASE : OrderMethod.ONLINE;
    }
}
