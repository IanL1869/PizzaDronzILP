package uk.ac.ed.inf;

import junit.framework.TestCase;
import uk.ac.ed.inf.handler.OrderVal;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;

import java.time.DayOfWeek;
import java.time.LocalDate;


public class OrderTests extends TestCase{

    private Restaurant[] definedRestaurants;
    private OrderVal orderValidator;

    protected void setUp() throws Exception {
        super.setUp();
        definedRestaurants = new Restaurant[] {
                new Restaurant("Civerinos Slice", new LngLat(-3.1912869215011597,55.945535152517735),
                        new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                        new Pizza[] {new Pizza("Margarita", 1000), new Pizza("Calzone", 1400)}),

                new Restaurant("Soderberg Pavillion", new LngLat(-3.1940174102783203,55.94390696616939),
                        new DayOfWeek[] {DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                        new Pizza[] {new Pizza("Proper Pizza", 1400), new Pizza("Pineapple & Ham & Cheese", 900)})
        };
        orderValidator = new OrderVal();
    }

    public void testCardNumberTooShort() {

        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("123456789012345",
                "11/28",
                "123"));

        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }

    public void testCardNumberTooLong() {

        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("12345678901234567",
                "11/28",
                "123"));

        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }

    public void testCardNumberWrongType() {

        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("!£$%^&*()_-+=?@",
                "11/28",
                "123"));

        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }

    public void testExpiryDateMonth(){
        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "34/12",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }

    public void testExpiryDateYear(){
        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "09/16",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }

    public void testCVVTooShort(){
        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "10/28",
                "12"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CVV_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }

    public void testCVVTooLong(){
        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "10/28",
                "1234"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CVV_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }

    public void testCVVWrongType(){
        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("2728338838383838",
                "11/28",
                "!£$"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.CVV_INVALID, orderInvalidCardNumber.getOrderValidationCode());
    }

    public void testTotalTooMuch(){
        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
                OrderStatus.UNDEFINED,
                OrderValidationCode.NO_ERROR,
                1100,
                new Pizza[] {new Pizza("Margarita", 1200)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "11/28",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.TOTAL_INCORRECT, orderInvalidCardNumber.getOrderValidationCode());
    }

    public void testTotalTooLittle(){
        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
                OrderStatus.UNDEFINED,
                OrderValidationCode.NO_ERROR,
                1100,
                new Pizza[] {new Pizza("Margarita", 900)},
                null);
        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "11/28",
                "123"));
        Order orderInvalidCardNumber = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.TOTAL_INCORRECT, orderInvalidCardNumber.getOrderValidationCode());
    }

    public void testPizzaUndefined(){
        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("peporonipizzzaplz", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "11/28",
                "123"));

        Order orderInvalidType = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.PIZZA_NOT_DEFINED, orderInvalidType.getOrderValidationCode());
        assertEquals(OrderStatus.INVALID, orderInvalidType.getOrderStatus());
    }

    public void testMaxPizzaExceeded(){
        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
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
                "11/28",
                "123"));

        Order orderInvalidType = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED, orderInvalidType.getOrderValidationCode());
        assertEquals(OrderStatus.INVALID, orderInvalidType.getOrderStatus());
    }

    public void testPizzaFromMultiple(){
        Order order = new Order("1",
                LocalDate.of(2023, 11, 3),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                2500,
                new Pizza[] {new Pizza("Margarita", 1000),
                        new Pizza("Proper Pizza", 1400)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "11/28",
                "123"));

        Order orderInvalidType = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS, orderInvalidType.getOrderValidationCode());
        assertEquals(OrderStatus.INVALID, orderInvalidType.getOrderStatus());
    }


    public void testRestaurantClosed(){
        Order order = new Order("1",
                LocalDate.of(2023, 11, 2),
                OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED,
                1100,
                new Pizza[] {new Pizza("Margarita", 1000)},
                null);

        order.setCreditCardInformation(new CreditCardInformation("1234567890123456",
                "12/25",
                "123"));

        Order orderInvalidType = orderValidator.validateOrder(order, definedRestaurants);
        assertEquals(OrderValidationCode.RESTAURANT_CLOSED, orderInvalidType.getOrderValidationCode());
        assertEquals(OrderStatus.INVALID, orderInvalidType.getOrderStatus());
    }



}
