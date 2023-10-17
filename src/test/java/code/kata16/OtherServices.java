package code.kata16;

public interface OtherServices {

    void generatePackingSlip(Department department, PackingSlip packingSlip);

    void generateCommissionPayment(Order order);

    void activateMembership(Customer customer, ProductType membership);

    boolean hasMembership(Customer customer, ProductType membership);

    void upgradeMembership(Customer customer, ProductType membership);
}
