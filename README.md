ASE - reliability reports
-------------------------------------
Michael Mittermayr, 1126749
Version: 1, 2015-06-24

## Pre-Requirements ##

Tools:
- maven (current version)
- java 8
- mongodb
- free ports: 8080 (Api-Gateway), 9000 9100 9200 9300 9090

## Configuration ##
Every service comes with it's own application.properties files located in the resources folder.
Inside this file the ip address of the mongodb server should be changed, if it is not located at the localhost!

## Compilation ##
mvn install -DskipTests=true

## Running ##
There is no fixed order the services need to be started in. Each one can be started by switching to the folder (ase.analysis) and using:
mvn spring-boot:run 

Alternatively it can alse be done using:
java -jar target/SERVICE_NAME.jar

#################
#  FIRST START  #
#################

Running the datasource for the first time takes quite some time as the realtime data is generates by the simulation.
The system generates data for about 1 month (2015-06-01 till 2015-06-30).

The resulting data is stored inside the mongodb, requires some diskspace :) (around 1 or 2 gb)

On my machine first start took 130 seconds. Spring prints DataSourceApplication started message, after that the service can be used.

ATTENTION: During RT data generation no output is printed!

##################
#   Limitation   #
##################

The system is designed to use several analysis services at the same time, however, this would require an additional load balancer in front of the analysis services as well as a different activemq configuration.
The current configuration takes care about message priority and scheduling the analysis tasks on a single service. Therefore it uses only an embedded broker to reduces setup complexity.

Ports may not be changed except for the API-Gateway. All the services have to run on the same machine as there is no way to configure the service hostname/ip-addresses implemented.

##################
#     USAGE      #
##################

Important:
All the requests are case sensitive!
_______________________________________________________________________________


###############################################################################
Request Report (Medium priority): Possible priority levels (Low, Medium, High).
###############################################################################
POST: http://localhost:8080/report/request/Medium/
ContentType: application/json
-------------------------------------------------------------------------------
Content: e.g.

{
  "userId": "1126749",
  "line" : "U1",
  "stationFrom" : "Karlsplatz",
  "stationTo": "Leopoldau",
  "hourStart": 8,
  "minuteStart": 0,
  "hourEnd": 9,
  "minuteEnd" : 0,
  "from" : "2015-06-01",
  "to" : "2015-06-07"

}


###############################################################################
Get Notifications (userID: 1126749):
###############################################################################
GET: http://localhost:8080/notification/1126749/
-------------------------------------------------------------------------------


###############################################################################
Get Report (report ID: 558b1b66254ac7c5dcfefca0):
###############################################################################
GET: http://localhost:8080/report/view/558b1b66254ac7c5dcfefca0
-------------------------------------------------------------------------------



###############################################################################
Get Report Metadata (data concerns) (report ID: 558b1b66254ac7c5dcfefca0):
###############################################################################
GET: http://localhost:8080/report/metadata/558b1b66254ac7c5dcfefca0
-------------------------------------------------------------------------------
