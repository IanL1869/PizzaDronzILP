package uk.ac.ed.inf;

import uk.ac.ed.inf.handler.DeliveryHandler;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * The main class of the PizzaDronz application responsible for handling the command-line arguments,
 * validating the input and invoking the delivery handler to write the necessary files.
 */
public class App {

    /**
     * The main entry point of the PizzaDronz application.
     *
     * @param args An array of command-line arguments. The first element is expected to be
     *             formatted as 'yyyy-MM-dd', and the second element is a valid base URL.
     * @throws IOException If an IO error occurs when processing files.
     */
    public static void main(String[] args ) throws IOException {

        // arguments.
        String orderDate = args[0];
        String baseURL = args[1];

        // check the date is of valid form and the URL not malformed.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate parsedDate = LocalDate.parse(orderDate, formatter);
            new URL(baseURL);

        }catch (DateTimeParseException e){
            System.out.println("Input Date is invalid.");
            throw new RuntimeException(e);

        }catch (MalformedURLException e){
            System.out.println("Given URL is invalid.");
            throw new RuntimeException(e);

        }

        // call the delivery handler and write the files.
        DeliveryHandler deliveryHandler = new DeliveryHandler(baseURL, orderDate);
        deliveryHandler.filesWriter();
    }
}
