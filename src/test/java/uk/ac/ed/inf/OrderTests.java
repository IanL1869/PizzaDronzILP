package uk.ac.ed.inf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class OrderTests extends TestCase {
    // Declare a variable to store an instance of the Order class
    private Restaurant[] definedRestaurants;
    private OrderVal orderValidator;

    // This method runs before every test method
    protected void setUp() throws Exception {
        super.setUp();

        definedRestaurants = new Restaurant[] {
                new Restaurant("Civerinos Slice", new LngLat(-3.1912869215011597,55.945535152517735),
                        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                        new Pizza[] {new Pizza("Margarita", 1000), new Pizza("Calzone", 1400)}),
                new Restaurant("Sora Lella Vegan Restaurant", new LngLat(-3.202541470527649,55.943284737579376),
                        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY},
                        new Pizza[] {new Pizza("Meat Lover", 1400), new Pizza("Vegan Delight", 1100)}),
                new Restaurant("Domino's Pizza - Edinburgh - Southside", new LngLat(-3.1838572025299072,55.94449876875712),
                        new DayOfWeek[] {DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                        new Pizza[] {new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)}),
                new Restaurant("Soderberg Pavillion", new LngLat(-3.1940174102783203,55.94390696616939),
                        new DayOfWeek[] {DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                        new Pizza[] {new Pizza("Proper Pizza", 1400), new Pizza("Pineapple & Ham & Cheese", 900)})
        };

        orderValidator = new OrderVal();
    }


    public void testCardNumberInvalidLength() {
        Order order = new Order("1",
                                LocalDate.of(2023, 10, 2),
                                OrderStatus.UNDEFINED,
                                OrderValidationCode.UNDEFINED,
                1100,
                                new Pizza[] {new Pizza("Margarita", 1000)},
                                null);

        order.setCreditCardInformation(new CreditCardInformation("123456789012345",
                "12/25",
                "123"));

        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderInvalidCardNumber.getOrderValidationCode(), OrderValidationCode.CARD_NUMBER_INVALID);
    }

    public void testCardNumberInvalidType() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234x6789y123456",
                "12/25",
                "123"));

        Order orderInvalidType = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderInvalidType.getOrderValidationCode(), OrderValidationCode.CARD_NUMBER_INVALID);
    }

    public void testExpiryDateBeforeCurrent() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "09/23",
                "123"));

        Order orderDateBefore = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderDateBefore.getOrderValidationCode(), OrderValidationCode.EXPIRY_DATE_INVALID);
    }

    public void testExpiryDateInvalidType() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "ab/25",
                "123"));

        Order orderInvalidExpiry = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderInvalidExpiry.getOrderValidationCode(), OrderValidationCode.EXPIRY_DATE_INVALID);
    }

    public void testExpiryDateInvalidFormat() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "01/01/25",
                "123"));

        Order orderInvalidExpiry = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderInvalidExpiry.getOrderValidationCode(), OrderValidationCode.EXPIRY_DATE_INVALID);
    }

    public void testCVVInvalidLength() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "12"));

        Order orderCVVLength = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderCVVLength.getOrderValidationCode(), OrderValidationCode.CVV_INVALID);
    }

    public void testCVVInvalidType() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "12a"));

        Order orderCVVLength = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderCVVLength.getOrderValidationCode(), OrderValidationCode.CVV_INVALID);
    }

    public void testInvalidTotal() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1000,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderCVVType = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderCVVType.getOrderValidationCode(), OrderValidationCode.TOTAL_INCORRECT);
    }

    public void testPizzaUndefinedObject() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                100,
                new Pizza[] {new Pizza("Some made up pizza", 0)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderInvalidPizzaObject = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderInvalidPizzaObject.getOrderValidationCode(), OrderValidationCode.PIZZA_NOT_DEFINED);
    }

    public void testPizzaUndefinedName() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Some made up pizza", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderInvalidPizzaName = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderInvalidPizzaName.getOrderValidationCode(), OrderValidationCode.PIZZA_NOT_DEFINED);
    }

    public void testPizzaUndefinedPrice() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                200,
                new Pizza[] {new Pizza("Margarita", 100)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderInvalidPizzaPrice = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderInvalidPizzaPrice.getOrderValidationCode(), OrderValidationCode.PIZZA_NOT_DEFINED);
    }

    public void testMaxPizza() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                5100,
                new Pizza[] {new Pizza("Margarita", 1000),
                             new Pizza("Margarita", 1000),
                             new Pizza("Margarita", 1000),
                             new Pizza("Margarita", 1000),
                             new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderMaxPizzas = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderMaxPizzas.getOrderValidationCode(), OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
    }

    public void testMinPizza() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                100,
                new Pizza[] {},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderMinPizzas = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderMinPizzas.getOrderValidationCode(), OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
    }

    public void testPizzaFromMultiple() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                2500,
                new Pizza[] {new Pizza("Margarita", 1000),
                        new Pizza("Meat Lover", 1400)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderMultipleRestaurants = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderMultipleRestaurants.getOrderValidationCode(), OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
    }

    public void testRestaurantClosed() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                2400,
                new Pizza[] {new Pizza("Super Cheese", 1400),
                        new Pizza("All Shrooms", 900)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));



        Order orderRestaurantClosed = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderRestaurantClosed.getOrderValidationCode(), OrderValidationCode.RESTAURANT_CLOSED);
    }

    public void testValidOrder() {
        Order order = new Order("1",
                LocalDate.of(2023, 10, 4),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                2400,
                new Pizza[] {new Pizza("Super Cheese", 1400),
                        new Pizza("All Shrooms", 900)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderMinPizzas = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(orderMinPizzas.getOrderValidationCode(), OrderValidationCode.NO_ERROR);
    }
}
