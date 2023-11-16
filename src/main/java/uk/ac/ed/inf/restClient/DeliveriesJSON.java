package uk.ac.ed.inf.restClient;

import uk.ac.ed.inf.ilp.gsonUtils.LocalDateSerializer;

public class DeliveriesJSON {

    private String orderNo;
    private String orderStatus;
    private String orderValidationCode;
    private int costInPence;

    // Constructors, getters, setters, and other methods

    public DeliveriesJSON(String orderNo, String orderStatus, String orderValidationCode, int costInPence) {
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.orderValidationCode = orderValidationCode;
        this.costInPence = costInPence;
    }

}
