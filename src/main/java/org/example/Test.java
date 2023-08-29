package org.example;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class Test {
        public static void main(String[] args) {
            try {
                JsonParser jsonParser = new JsonParser();
                JsonElement rootElement = jsonParser.parse(new FileReader("tickets.json"));
                JsonObject jsonObject = rootElement.getAsJsonObject();
                JsonArray tickets = jsonObject.getAsJsonArray("tickets");

                Map<String, Integer> minFlightTimes = new HashMap<>();
                List<Double> prices = new ArrayList<>();

                for (JsonElement ticketElement : tickets) {
                    JsonObject ticket = ticketElement.getAsJsonObject();
                    String origin = ticket.get("origin_name").getAsString();
                    String destination = ticket.get("destination_name").getAsString();
                    String carrier = ticket.get("carrier").getAsString();
                    String departureTime = ticket.get("departure_time").getAsString();
                    String arrivalTime = ticket.get("arrival_time").getAsString();

                    if (origin.equals("Владивосток") && destination.equals("Тель-Авив")) {
                        int flightTime = calculateFlightTime(departureTime, arrivalTime);

                        minFlightTimes.put(carrier, Math.min(minFlightTimes.getOrDefault(carrier, flightTime), flightTime));
                    }

                    double price = ticket.get("price").getAsDouble();
                    prices.add(price);
                }


                double averagePrice = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                double medianPrice = calculateMedian(prices);

                System.out.println("Minimum Flight Times:");
                minFlightTimes.forEach((carrier, time) -> System.out.println(carrier + ": " + time + " minutes"));

                System.out.println("\nPrice Statistics:");
                System.out.println("Average Price: " + averagePrice);
                System.out.println("Median Price: " + medianPrice);
                System.out.println("\nThe difference between the average price and the median: " + (averagePrice-medianPrice));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static int calculateFlightTime(String departureTime, String arrivalTime) {
            int departureHours = Integer.parseInt(departureTime.split(":")[0]);
            int departureMinutes = Integer.parseInt(departureTime.split(":")[1]);
            int arrivalHours = Integer.parseInt(arrivalTime.split(":")[0]);
            int arrivalMinutes = Integer.parseInt(arrivalTime.split(":")[1]);

            return (arrivalHours - departureHours) * 60 + (arrivalMinutes - departureMinutes);
        }

        private static double calculateMedian(List<Double> prices) {
            List<Double> sortedPrices = prices.stream().sorted().toList();
            int size = sortedPrices.size();
            if (size % 2 == 0) {
                return (sortedPrices.get(size / 2 - 1) + sortedPrices.get(size / 2)) / 2;
            } else {
                return sortedPrices.get(size / 2);
            }
        }
}

