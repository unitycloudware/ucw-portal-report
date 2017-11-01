@echo off

REM Maven automatically fetches the latest SNAPSHOT on daily basis. 
REM You can force Maven to download latest snapshot build using -U 
REM switch to any Maven command.

mvn clean resources:resources install -U