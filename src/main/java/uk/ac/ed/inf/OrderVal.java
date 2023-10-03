package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.data.CreditCardInformation;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;

import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class OrderVal implements OrderValidation {
    @Override


//    UNDEFINED,
//    NO_ERROR,
//    CARD_NUMBER_INVALID,
//    EXPIRY_DATE_INVALID,
//    CVV_INVALID,
//    TOTAL_INCORRECT,
//    PIZZA_NOT_DEFINED,
//    MAX_PIZZA_COUNT_EXCEEDED,
//    PIZZA_FROM_MULTIPLE_RESTAURANTS,
//    RESTAURANT_CLOSED;


    private boolean checkCardDigits(Order orderToValidate){

        return orderToValidate.getCreditCardInformation().getCreditCardNumber().matches("\\d{16}");
    }
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {

        // checking card number is 16 digits



        boolean cardNoDigits = orderToValidate.getCreditCardInformation().getCreditCardNumber().matches("\\d{16}");

        if (!cardNoDigits){
            orderToValidate.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
        }


        // checking expiry date

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy");
        dateFormat.setLenient(false); // for strict parsing

        // try to convert to type Date
        try {
            Date expiryDate = dateFormat.parse(orderToValidate.getCreditCardInformation().getCreditCardExpiry());
            Calendar currentDate = Calendar.getInstance();
            currentDate.set(Calendar.DAY_OF_MONTH, 1);

        // expiry date is before current date then it is invalid
            if (expiryDate.before(currentDate.getTime())){
                orderToValidate.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
            }

        // if cannot convert then the input is invalid
        } catch (ParseException e){
            orderToValidate.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
        }


        // checking cvv
        boolean cvvNoDigits = orderToValidate.getCreditCardInformation().getCreditCardNumber().matches("\\d{3}");
        if (!cvvNoDigits){
            orderToValidate.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
        }


        //checking total

//        if (orderToValidate.getPriceTotalInPence() != order )



        if (orderToValidate.getPizzasInOrder().length > 4){

            orderToValidate.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);

        }


        



        return null;
    }
}
