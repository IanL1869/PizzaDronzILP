package uk.ac.ed.inf;

import uk.ac.ed.inf.handler.DeliveryHandler;
import uk.ac.ed.inf.restClient.WriteFiles;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Hello world!
 *
 */
public class App 
{




    public static void main(String[] args ) throws IOException {

        String orderDate = args[0];
        String baseURL = args[1];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate parsedDate = LocalDate.parse(orderDate, formatter);
            new URL(baseURL);

        }catch (DateTimeParseException e){
            System.out.println("Input Date is invalid");

        }catch (MalformedURLException e){
            System.out.println("Given URL is invalid");

        }

        DeliveryHandler deliveryHandler = new DeliveryHandler(baseURL, orderDate);
        deliveryHandler.filesWriter();
    }
}
