hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/alexey

mvn package
hadoop fs -copyFromLocal FlightsTable.csv
hadoop fs -copyFromLocal AirportsTable.csv
spark-submit --class bmstu.AirportsFlightApp --master yarn-client --num-executors 3 target/spark-examples-1.0-SNAPSHOT.jar
