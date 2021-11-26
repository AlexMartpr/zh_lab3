hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/alexey

mvn package
hadoop fs -copyFromLocal FlightsTable.csv
hadoop fs -copyFromLocal AirportsTable.csv
spark-submit --class bmstu.AirportsFlightApp --master yarn-client --num-executors 3 /home/alexey/zh_lab3/target/AirportsFlightApp.jar
hadoop fs -copyToLocal output