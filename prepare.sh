#!/bin/bash

stop-yarn.sh
stop-dfs.sh
rm -rf ~/hdata/datanode
rm -rf ~/hdata/namenode

mkdir ~/hdata/datanode
mkdir ~/hdata/namenode

hdfs namenode -format
start-dfs.sh
start-yarn.sh
