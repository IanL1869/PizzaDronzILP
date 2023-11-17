package uk.ac.ed.inf.restClient;

import uk.ac.ed.inf.ilp.gsonUtils.LocalDateSerializer;

public class DeliveriesJSON {

    public String orderNo;
    public String orderStatus;
    public String orderValidationCode;
    public int costInPence;

    // Constructors, getters, setters, and other methods

    public DeliveriesJSON(String orderNo, String orderStatus, String orderValidationCode, int costInPence) {
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.orderValidationCode = orderValidationCode;
        this.costInPence = costInPence;
    }

}
