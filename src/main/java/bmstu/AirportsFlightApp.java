package bmstu;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class AirportsFlightApp {
    private static String flightsFile = "FlightsTable.csv";
    private static String airportsFile = "AirportsTable.csv";

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

        JavaPairRDD<String, String> airportsInfo = inputAirportsFile.


    }
}
