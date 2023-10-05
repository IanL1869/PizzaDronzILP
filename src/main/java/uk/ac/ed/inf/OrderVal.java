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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
public class OrderVal implements OrderValidation {


    private boolean checkCardDigits(Order orderToValidate){

        return orderToValidate.getCreditCardInformation().getCreditCardNumber().matches("\\d{16}");
    }

    private boolean checkExpDate(Order orderToValidate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy");
        dateFormat.setLenient(false); // for strict parsing

        // try to convert to type Date
        try {
            Date expiryDate = dateFormat.parse(orderToValidate.getCreditCardInformation().getCreditCardExpiry());
            Calendar currentDate = Calendar.getInstance();
            currentDate.set(Calendar.DAY_OF_MONTH, 1);

            // expiry date is before current date then it is invalid
            if (expiryDate.before(currentDate.getTime())){
                return false;
            }

            // if cannot convert then the input is invalid
        } catch (ParseException e){
            return false;
        }

        return true;

    }

    private boolean checkCVV(Order orderToValidate){

        return orderToValidate.getCreditCardInformation().getCreditCardNumber().matches("\\d{3}");


    }

    private boolean checkTotal(Order orderToValidate){
        int price = Arrays.stream(orderToValidate.getPizzasInOrder()).mapToInt(Pizza::priceInPence).sum();

        return price + 100 == orderToValidate.getPriceTotalInPence();
    }

    private boolean checkMaxPizza(Order orderToValidate){

        return orderToValidate.getPizzasInOrder().length > 4;

    }
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



    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {





        return null;
    }
}
