#!/bin/bash

# Maven automatically fetches the latest SNAPSHOT on daily basis. 
# You can force Maven to download latest snapshot build using -U 
# switch to any Maven command.

mvn clean resources:resources install -U