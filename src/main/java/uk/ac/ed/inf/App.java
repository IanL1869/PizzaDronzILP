package uk.ac.ed.inf;

import uk.ac.ed.inf.handler.DeliveryHandler;
import uk.ac.ed.inf.restClient.RestClient;

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

        // check for argument length
        if(args.length != 2){
            System.err.println("Wrong number of arguments. Please enter 2 arguments.");
            System.exit(1);
        }
        // arguments.
        String orderDate = args[0];
        String baseURL = args[1];

        // check the date is of valid form and the URL not malformed.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate parsedDate = LocalDate.parse(orderDate, formatter);
            new URL(baseURL);

        }catch (DateTimeParseException e){
            System.err.println("Input Date is invalid. Please enter a valid date of the format YYYY-MM-DD.");
            System.exit(1);

        }catch (MalformedURLException e){
            System.err.println("Given URL is invalid.");
            System.exit(1);

        }
        // initialise Rest Client and check Rest Service is alive.
        RestClient restClient = new RestClient(baseURL, orderDate);
        if (restClient.getIsAlive()) {
            // call the delivery handler and write the files.
            DeliveryHandler deliveryHandler = new DeliveryHandler(restClient, orderDate);
            deliveryHandler.filesWriter();
        }else{
            System.err.println("Rest Service is Dead.");
            System.exit(1);
        }
    }
}
