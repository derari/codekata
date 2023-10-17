package code.kata16;

class BasicPaymentHandlerTest extends PaymentHandlerTestBase {

    @Override
    protected PaymentHandler instance(OtherServices services) {
        return new BasicPaymentHandler(services);
    }
}
