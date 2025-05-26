package code.kata.kata16;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

abstract class PaymentHandlerTestBase {

    private final OtherServices services = Mockito.mock(OtherServices.class);
    private final Customer customer = new Customer("customer@example.com");

    protected PaymentHandler instance() {
        return instance(services);
    }
    protected abstract PaymentHandler instance(OtherServices services);

    @Test
    void physicalProductIsShipped() {
        var order = new Order(customer, "Extra Large", ProductType.KEBAB);

        instance().paymentReceived(order);

        var expected = new PackingSlip(order);
        verify(services).generatePackingSlip(Department.SHIPPING, expected);

        verifyNoMoreInteractions(services);
    }

    @Test
    void bookShipmentForRoyalties() {
        var order = new Order(customer, "Things for Dummies", ProductType.BOOK);

        instance().paymentReceived(order);

        var expected = new PackingSlip(order);
        verify(services).generatePackingSlip(Department.SHIPPING, expected);
        verify(services).generatePackingSlip(Department.ROYALTIES, expected);

        verifyNoMoreInteractions(services);
    }
    @Test
    void extraItem() {
        var order = new Order(customer, "Learning to Ski", ProductType.VIDEO);

        instance().paymentReceived(order);

        var expected = new PackingSlip(order);
        expected.items().add(order.extraItem("First Aid", ProductType.VIDEO));
        verify(services).generatePackingSlip(Department.SHIPPING, expected);

        verifyNoMoreInteractions(services);
    }

    @Test
    void comissionPayment() {
        var order = new Order(customer, "Extra Large", ProductType.KEBAB, "007");

        instance().paymentReceived(order);

        var expected = new PackingSlip(order);
        verify(services).generatePackingSlip(Department.SHIPPING, expected);
        verify(services).generateCommissionPayment(order);

        verifyNoMoreInteractions(services);
    }

    @Test
    void membershipActivation() {
        var order = new Order(customer, "100kg FTW", ProductType.GYM_BASIC);

        instance().paymentReceived(order);

        verify(services).activateMembership(customer, ProductType.GYM_BASIC);

        verifyNoMoreInteractions(services);
    }

    @Test
    void membershipUpgrade() {
        var order = new Order(customer, "1000kg FTW", ProductType.GYM_PREMIUM);
        when(services.hasMembership(any(), any())).thenAnswer(i -> i.getArgument(1) == ProductType.GYM_BASIC);

        instance().paymentReceived(order);

        verify(services).upgradeMembership(customer, ProductType.GYM_PREMIUM);
        verify(services, atLeast(0)).hasMembership(any(), any());

        verifyNoMoreInteractions(services);
    }
}
