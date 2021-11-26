package bmstu;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

import scala.Tuple2;

public class AirportsFlightApp {
    private static final String FLIGHTS_FILE = "FlightsTable.csv";
    private static final String AIRPORTS_FILE = "AirportsTable.csv";
    private static final String PATH = "output";
    private static final String DELIMITER = ",";
    private static final int INDEX_AIRPORT_ID = 0;
    private static final int INDEX_AIRPORT_NAME = 1;
    private static final int INDEX_ORIGIN_ID = 11;
    private static final int INDEX_DESTINATION_ID = 14;
    private static final int INDEX_DELAY = 18;
    private static final int INDEX_CANCELLED = 19;


    private static JavaRDD<String> removeHead(JavaRDD<String> str) {
        String header = str.first();
        return str.filter(s -> !s.equals(header));
    }
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("AirportsFlightApp");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> inputFlightsFile = sc.textFile(FLIGHTS_FILE);
        JavaRDD<String> inputAirportsFile = sc.textFile(AIRPORTS_FILE);
        inputFlightsFile = removeHead(inputFlightsFile);
        inputAirportsFile = removeHead(inputAirportsFile);

        Map<String, String> airportsInfoMap = inputAirportsFile.mapToPair(row -> {
            String[] col = row.split(",");
            String airportName = col[INDEX_AIRPORT_NAME];
            String airportID = col[INDEX_AIRPORT_ID];
            return new Tuple2<>(airportID, airportName);
        }).collectAsMap();
        
        final Broadcast<Map<String, String>> airportsBroadcast = sc.broadcast(airportsInfoMap);

        JavaPairRDD<Tuple2<String, String>, Flight> flightsInfo = inputFlightsFile.mapToPair(row -> {
            String[] col = row.split(DELIMITER);
            String destinationID = col[INDEX_DESTINATION_ID];
            String originID = col[INDEX_ORIGIN_ID];
            Double delay;
            try {
                delay = Double.parseDouble(col[INDEX_DELAY]);
            } catch (NumberFormatException e) {
                delay = 0.0;
            }

            Boolean cancelled;
            double cancl = Double.parseDouble(col[INDEX_CANCELLED]);
            cancelled = cancl == 1;

            Tuple2<String, String> result = new Tuple2<>(originID, destinationID);
            Flight currentFlight = new Flight(delay, cancelled);
            return new Tuple2<>(result, currentFlight); 

        });
        flightsInfo.groupByKey()
        .mapValues(val -> {
            Iterator<Flight> it = val.iterator();
            double maxDelay = Double.MIN_VALUE;
            int countCancelledFlights = 0;
            int countDelayedFlights = 0;
            int countFlights = 0;
            while (it.hasNext()) {
                Flight current = it.next();
                double checkDelay = current.getDelay();
                if (checkDelay > 0) {
                    maxDelay = Double.max(maxDelay, checkDelay);
                    countDelayedFlights++;
                }
                boolean checkCancelled = current.getIsCannceled();
                if (checkCancelled) {
                    countCancelledFlights++;
                }
                countFlights++;
            }
            double delayedAndCancelledFlightsInPercents = ((countDelayedFlights + countCancelledFlights) / countFlights) * 100;
            String result = "MaxDelay: " + maxDelay + "\n" + "Delayed and cancelled flights in %: " + delayedAndCancelledFlightsInPercents + "%\n";
            return result;
        })
        .map(val -> {
            return "From " + airportsBroadcast.value().get(val._1._1) + " To " + airportsBroadcast.value().get(val._1._2) + "\n";
        })
        .saveAsTextFile(PATH);
    }
}
