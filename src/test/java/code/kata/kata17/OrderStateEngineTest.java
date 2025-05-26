package code.kata.kata17;

import code.kata.kata16.Order;
import code.kata.kata16.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.Map;

import static code.kata.kata17.OrderWorkflowKey.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

class OrderStateEngineTest {

    OrderStateEngine instance;
    final OtherServices services = Mockito.mock(OtherServices.class);
    final Customer customer = new Customer("customer@example.com");

    @BeforeEach
    void setUp() {
        var repository = new OrderWorkflowRepository(services);
        YAMLS.forEach(repository::addYaml);
        instance = new OrderStateEngine(repository);
    }

    @Test
    void creditCardOrder_shipmentWaiting() {
        var order = new Order(customer, "Extra Large", ProductType.KEBAB, PaymentMethod.CREDIT_CARD);

        var id = instance.create(order);
        instance.runAll();
        assertTrue(instance.isStarted(id, WAIT_FOR_PRODUCTS));
        assertFalse(instance.isStarted(id, SHIPMENT));
    }

    @Test
    void creditCardOrder_shipmentAvailable() {
        var order = new Order(customer, "Extra Large", ProductType.KEBAB, PaymentMethod.CREDIT_CARD);

        Mockito.doAnswer(inv -> instance.productsAvailable(inv.getArgument(0)))
               .when(services).waitForProducts(anyLong());

        var id = instance.create(order);
        instance.runAll();
        assertTrue(instance.isStarted(id, WAIT_FOR_PRODUCTS));
        assertTrue(instance.isStarted(id, SHIPMENT));
    }

    @Test
    void purchaseOrder_shipmentWaiting() {
        var order = new Order(customer, "Extra Large", ProductType.KEBAB, PaymentMethod.INVOICE);

        var id = instance.create(order);
        instance.runAll();
        assertTrue(instance.isStarted(id, WAIT_FOR_PRODUCTS));
        assertFalse(instance.isStarted(id, SHIPMENT));
    }

    @Test
    void creditCardOrder_availableAndPayed() {
        var order = new Order(customer, "Extra Large", ProductType.KEBAB, PaymentMethod.CREDIT_CARD);

        Mockito.doAnswer(inv -> instance.productsAvailable(inv.getArgument(0)))
                .when(services).waitForProducts(anyLong());

        var id = instance.create(order);
        instance.runAll();
        assertTrue(instance.isStarted(id, WAIT_FOR_PRODUCTS));
        assertTrue(instance.isStarted(id, SHIPMENT));

        instance.paymentReceived(id);
        instance.runAll();
        assertTrue(instance.isStarted(id, COMPLETED));

    }

    private static final Map<OrderWorkflowKey, String> YAMLS = Map.of(
            NEW, """
            - if:
                any:
                  - paymentMethod: credit_card
                  - orderMethod: purchase
              then:
                schedule: wait_for_products
            """,
            PAYMENT_RECEIVED, RulesParserSimplifiedYamlTest.getPaymentEventYaml() + """
            - if:
                isStarted: shipment
              then:
                schedule: completed
              else:
                schedule: wait_for_products
            """,
            WAIT_FOR_PRODUCTS, "WaitForProducts",
            SHIPMENT, """
            - if:
                isStarted: payment_received
              then:
                schedule: completed
            """,
            CANCELLED, "[]",
            COMPLETED, "[]"
    );
}
