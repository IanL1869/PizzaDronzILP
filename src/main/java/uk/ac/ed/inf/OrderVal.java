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
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

public class OrderVal implements OrderValidation {


    private boolean checkCardDigits(Order orderToValidate){

        return orderToValidate.getCreditCardInformation().getCreditCardNumber().matches("\\d{16}");
    }

    private boolean checkExpDate(Order orderToValidate){




        try {

            String expiryDate =  orderToValidate.getCreditCardInformation().getCreditCardExpiry();
            LocalDate dateOfOrder = orderToValidate.getOrderDate();

            if (expiryDate.matches("\\d{2}/\\d{2}")) {
                String[] parts = expiryDate.split("/");
                int monthValue = Integer.parseInt(parts[0]);
                Month expiryMonth = Month.of(monthValue);

                LocalDate expiryDateParsed = LocalDate.of(Year.now().getValue(), expiryMonth, 1);

                return !dateOfOrder.isAfter(expiryDateParsed);
            } else {
                // Invalid format
                return false;
            }


        }catch (Exception e){
            return false;
        }

    }

    private boolean checkCVV(Order orderToValidate){

        return orderToValidate.getCreditCardInformation().getCvv().matches("\\d{3}");

    }

    private boolean checkTotal(Order orderToValidate){

        int countPrice = 0;
        for (int j = 0; j < orderToValidate.getPizzasInOrder().length; j++) {

            countPrice = countPrice + orderToValidate.getPizzasInOrder()[j].priceInPence();
        }

        int totalPrice = orderToValidate.getPriceTotalInPence() - 100;

        return countPrice == totalPrice;

//        int totalPrice = Arrays.stream(orderToValidate.getPizzasInOrder())
//                .mapToInt(Pizza::priceInPence)
//                .sum();
//
//        int expectedTotalPrice = orderToValidate.getPriceTotalInPence() - 100;


//        return totalPrice == expectedTotalPrice;
    }



    private boolean checkPizzaDef(Restaurant[] restaurant ,Order orderToValidate){




        boolean flag = true;
        for (int j = 0; j < orderToValidate.getPizzasInOrder().length; j++) {
            boolean foundOnMenu = false;
            for (Restaurant value : restaurant) {

                if (Arrays.asList(value.menu()).contains(orderToValidate.getPizzasInOrder()[j])) {
                    foundOnMenu = true;
                    break;
                }
            }
            if (!foundOnMenu) {
                flag = false;
                break;
            }
        }


        return flag;


    }

    private boolean checkMaxPizza(Order orderToValidate){

        if (orderToValidate.getPizzasInOrder().length > 4) {
            return false;
        }else if (orderToValidate.getPizzasInOrder().length < 1){
            return false;
        }
        return true;

    }

    private boolean checkMultiRestaurants(Restaurant[] restaurant, Order orderToValidate) {



        boolean flag = false;

        for (Restaurant value : restaurant) {

            if (Arrays.asList(value.menu()).containsAll(Arrays.asList(orderToValidate.getPizzasInOrder()))) {
                flag = true;
                break;
            }

        }

        return flag;
    }

    private boolean checkRestaurantClosure(Restaurant[] restaurant, Order orderToValidate){

        if (orderToValidate.getPizzasInOrder().length < 1){
            return true;
        }

        LocalDate dateOfOrder = orderToValidate.getOrderDate();
        DayOfWeek dayOfOrder = dateOfOrder.getDayOfWeek();


        boolean flag = false;


        for (Restaurant value : restaurant) {

            for (int j = 0; j < orderToValidate.getPizzasInOrder().length; j++) {

                if (Arrays.asList(value.menu()).contains(orderToValidate.getPizzasInOrder()[j])) {

                    DayOfWeek[] daysOpen = value.openingDays();

                    if (Arrays.asList(daysOpen).contains(dayOfOrder)) {
                        flag = true;
                    }

                }

            }

        }

        return flag;



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
            orderToValidate.setOrderStatus(OrderStatus.VALID_BUT_NOT_DELIVERED);
            orderToValidate.setOrderValidationCode(OrderValidationCode.NO_ERROR);


        }

        return orderToValidate;
    }
}
