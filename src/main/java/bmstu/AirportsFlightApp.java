package bmstu;

import java.util.Collections;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;

import scala.Tuple2;

public class AirportsFlightApp {
    private static final String flightsFile = "FlightsTable.csv";
    private static final String airportsFile = "AirportsTable.csv";
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
        JavaRDD<String> inputFlightsFile = sc.textFile(flightsFile);
        JavaRDD<String> inputAirportsFile = sc.textFile(airportsFile);
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
            String[] col = row.split(",");
            String destinationID = col[INDEX_DESTINATION_ID];
            String originID = col[INDEX_ORIGIN];
            Double delay;
            try {
                delay = Double.parseDouble(col[INDEX_DELAY]);
            } catch (NumberFormatException e) {
                delay = 0.0;
            }

            Boolean cancelled;
            double cancl = Double.parseDouble(col[INDEX_CANCELLED]);
            cancelled = cancl == 1;

            Tuple2<String, String> result = new Tuple2<>() 

        });

    }
}
