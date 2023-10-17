package code.kata16;

public class BasicPaymentHandler implements PaymentHandler {

    private final OtherServices services;

    public BasicPaymentHandler(OtherServices services) {
        this.services = services;
    }

    @Override
    public void paymentReceived(Order order) {
        log("processing %s", order);
        if (order.productType().isPhysical()) {
            processPhysicalProduct(order);
        }
        if (order.productType().isMembership()) {
            processMembershipProduct(order);
        }
        log("done\n");
    }

    private void processPhysicalProduct(Order order) {
        var packingSlip = new PackingSlip(order);
        if (order.productType() == ProductType.VIDEO && order.productName().equals("Learning to Ski")) {
            log("Adding free 'First Aid' (result of a court decision in 1997)");
            packingSlip.items().add(order.extraItem("First Aid", ProductType.VIDEO));
        }
        log("generate packing slip for Shipping");
        services.generatePackingSlip(Department.SHIPPING, packingSlip);
        if (order.productType() == ProductType.BOOK) {
            log("generate packing slip for Royalties");
            services.generatePackingSlip(Department.ROYALTIES, packingSlip);
        }
        if (order.salesAgentId() != null) {
            log("generate commission payment");
            services.generateCommissionPayment(order);
        }
    }

    private void processMembershipProduct(Order order) {
        var upgrade = order.productType().getUpgradable().stream()
                        .anyMatch(p -> services.hasMembership(order.customer(), p));
        if (upgrade) {
            log("upgrading membership");
            services.upgradeMembership(order.customer(), order.productType());
        } else {
            log("activating membership");
            services.activateMembership(order.customer(), order.productType());
        }
    }

    protected void log(String message, Object... args) {
        System.out.print("\n> ");
        System.out.printf(message, args);
    }
}
