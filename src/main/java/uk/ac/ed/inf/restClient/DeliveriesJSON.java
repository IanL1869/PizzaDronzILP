package uk.ac.ed.inf.restClient;
/**
 * Represents delivery information described in JSON format.
 */
public class DeliveriesJSON {

    /**
     * The order number associated with the delivery.
     */
    public String orderNo;

    /**
     * The status of the delivery order.
     */
    public String orderStatus;

    /**
     * The validation code associated with the delivery order.
     */
    public String orderValidationCode;

    /**
     * The cost of the delivery in pence.
     */
    public int costInPence;

    /**
     * Constructs a new DeliveriesJSON object with the specified parameters.
     *
     * @param orderNo The order number associated with the delivery.
     * @param orderStatus The status of the delivery order.
     * @param orderValidationCode The validation code associated with the delivery order.
     * @param costInPence The cost of the delivery in pence.
     */
    public DeliveriesJSON(String orderNo, String orderStatus, String orderValidationCode, int costInPence) {
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.orderValidationCode = orderValidationCode;
        this.costInPence = costInPence;
    }
}
