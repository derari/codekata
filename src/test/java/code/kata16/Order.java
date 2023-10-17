package code.kata16;

public record Order(
        Customer customer,
        String productName,
        ProductType productType,
        String salesAgentId
) {

    public Order extraItem(String productName, ProductType productType) {
        return new Order(customer(), productName, productType, salesAgentId());
    }
}
