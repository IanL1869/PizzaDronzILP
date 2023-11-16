package uk.ac.ed.inf.handler;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;

public class OrderVal implements OrderValidation {

    private boolean checkCardDigits(Order orderToValidate){

        // return whether card has 16 digits
        return orderToValidate.getCreditCardInformation().getCreditCardNumber().matches("\\d{16}");

    }

    private boolean checkExpDate(Order orderToValidate){

        // Define desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");

        // get expiry date from card
        String expiryDate = orderToValidate.getCreditCardInformation().getCreditCardExpiry();

        // check expiry date is in correct form
        if (expiryDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")){

            // as it is in correct form parse it into format "MM/yy"
            YearMonth parsedExpDate = YearMonth.parse(expiryDate, formatter);

            // Take date to be at the end of the month
            LocalDate expDateEndofMonth = parsedExpDate.atEndOfMonth();

            // get today's date
            LocalDate currentDate = LocalDate.now();


            return !expDateEndofMonth.isBefore(currentDate);

        }

        return false;
    }

    private boolean checkCVV(Order orderToValidate){

        // returns if 3 digits or not
        return orderToValidate.getCreditCardInformation().getCvv().matches("\\d{3}");

    }

    private boolean checkTotal(Order orderToValidate){

        // for loop to count the price of all pizzas in order
        int countPrice = 0;

        for (int j = 0; j < orderToValidate.getPizzasInOrder().length; j++) {

            // update the price count
            countPrice = countPrice + orderToValidate.getPizzasInOrder()[j].priceInPence();

        }

        // define the total price recorded in the order and minus the delivery charge
        int totalPrice = orderToValidate.getPriceTotalInPence() - SystemConstants.ORDER_CHARGE_IN_PENCE;

        // return if the two prices match
        return countPrice == totalPrice;

    }

    private boolean checkPizzaDef(Restaurant[] restaurant ,Order orderToValidate){

        boolean flag = true;

        // loop through the order
        for (int j = 0; j < orderToValidate.getPizzasInOrder().length; j++) {

            // use a boolean to identify if a pizza is found on at least one menu
            boolean foundOnMenu = false;

            // nested loop to go through all restaurants
            for (Restaurant value : restaurant) {

                // check that a menu contains a pizza from the order
                if (Arrays.asList(value.menu()).contains(orderToValidate.getPizzasInOrder()[j])) {

                    foundOnMenu = true;     // pizza is on menu so set boolean to true and break the nested loop
                    break;

                }
            }

            if (!foundOnMenu) {
                flag = false;       // if the pizza was never found on any menu then the flag is set to false
                break;              // break outer loop if this is the case as that pizza is not on any menu

            }
        }

        return flag;

    }

    private boolean checkMaxPizza(Order orderToValidate){

        // check that there is no more than 4 pizzas in given order
        return orderToValidate.getPizzasInOrder().length <= SystemConstants.MAX_PIZZAS_PER_ORDER;

    }


    private boolean checkMultiRestaurants(Restaurant[] restaurant, Order orderToValidate) {

        boolean flag = false;

        // loop through all restaurants
        for (Restaurant value : restaurant) {

            // check that a restaurant menu contains ALL pizzas from one order
            if (new HashSet<>(Arrays.asList(value.menu())).containsAll(Arrays.asList(orderToValidate.getPizzasInOrder()))) {

                flag = true;    // update flag and break loop if condition is met as we have found the restaurant
                break;

            }

        }

        return flag;

    }

    private boolean checkRestaurantClosure(Restaurant[] restaurant, Order orderToValidate){

        // get the day of week that an order is placed
        DayOfWeek dayOfOrder = orderToValidate.getOrderDate().getDayOfWeek();

        boolean flag = false;

        // loop through all restaurants
        for (Restaurant value : restaurant) {

            // nested loop to go through all pizzas in order
            for (int j = 0; j < orderToValidate.getPizzasInOrder().length; j++) {

                if (Arrays.asList(value.menu()).contains(orderToValidate.getPizzasInOrder()[j])) {

                    DayOfWeek[] daysOpen = value.openingDays(); // get array that shows weekdays open for restaurant that has that pizza

                    if (Arrays.asList(daysOpen).contains(dayOfOrder)) {

                        flag = true;    // if the restaurant contains the day of the week that the order is placed update flag to true

                    }

                }

            }

        }

        return flag;

    }

    @Override
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {

        // go through all functions and check for each error. If that error is raised update the corresponding OrderValidationCode and OrderStatus.
        if(!checkCardDigits(orderToValidate)){
            
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);

        }else if (!checkExpDate(orderToValidate)){

            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);

        }else if (!checkCVV(orderToValidate)){

            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.CVV_INVALID);

        }else if (!checkTotal(orderToValidate)){

            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);

        }else if(!checkPizzaDef(definedRestaurants, orderToValidate)){

            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);

        }else if(!checkRestaurantClosure(definedRestaurants , orderToValidate)){

            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);

        }else if (!checkMaxPizza(orderToValidate)){

            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);

        }else if (!checkMultiRestaurants(definedRestaurants, orderToValidate)){

            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            orderToValidate.setOrderValidationCode((OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS));

        }else{

            // no error hit
            orderToValidate.setOrderStatus(OrderStatus.VALID_BUT_NOT_DELIVERED);
            orderToValidate.setOrderValidationCode(OrderValidationCode.NO_ERROR);

        }

        return orderToValidate;
    }

}
