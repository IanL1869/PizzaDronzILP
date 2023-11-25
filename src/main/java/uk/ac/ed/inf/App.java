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
 * The PizzaDronz app implements
 *
 *
 */
public class App 
{


    public static void main(String[] args ) throws IOException {

        String orderDate = "2023-09-01";
        String baseURL = "https://ilp-rest.azurewebsites.net";


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
