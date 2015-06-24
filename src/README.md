ASE - reliability reports
-------------------------------------
Michael Mittermayr, 1126749
Version: 1, 2015-06-24

## Pre-Requirements ##

Services:
- maven (current version)
- mongodb
- free ports: 8080 (Api-Gateway), 9000 9100 9200 9300 9090

WebUI:
- for running the webui (angular) a webserver to deliver the files is required. (e.g. nginx)
- bower
- npm

## Configuration ##
Every service comes with it's own application.properties files located in the resources folder.
Inside this file the ip address of the mongodb server should be changed, if it is not located at the local host!

## Compilation ##
maven install -DskipTests=true

## Running ##
There is no fixed order the services need to be started in. Each one can be startet using
java -jar target/SERVICE_NAME.jar

#################
#  FIRST START  #
#################

Running the datasource for the first time takes quite some time as the realtime data is generates by the simulation.
The system generates data for about 1 month (2015-06-01 till 2015-06-30).

The resulting data is stored inside the mongodb, requires some diskspace :) (around 1 or 2 gb)

On my machine first start took 130 seconds. Spring prints DataSourceApplication started message, after that the service can be used.

##################
#   Limitation   #
##################

The system is designed to use several analysis services at the same time, however, this would require an additional loadbalancer in front of the analysis services as well as a different activemq configuration.
The current configuration takes care about message priority and scheduling the analysis tasks on one services. Therefore it uses only an embedded broker to reduces setup complexity


