- # General

  - #### Team#: Team 53

  - #### Names: Zachary Pinto and Lasse Nordahl

  - #### Project 5 Video Demo Link: https://youtu.be/Vc3a1Zv7PRo

  - #### Instruction of deployment:

```
## Deployment Instructions

#start tomcat server
source /home/ubuntu/tomcat/bin/startup.sh

#Database Setup
mysql -u cs122b -p --database=cs122b < sql_creation/CreationInsertion.sql

#Encrypt Passwords
cd cs122b-spring20-project3-encryption-example/
mvn exec:java -Dexec.mainClass="UpdateSecurePassword"

#add employee creds
"classta@email.edu" and password "classta"

git clone https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53.git

cd cs122b-spring20-team53

git log

cd src/resources
vim config.properties

# Show DB
mysql -u cs122b -p -e "use cs122b;select count(*) from artist;select count(*) from album;select count(*) from track;select count(*) from track_meta;"

mysql -u cs122b -p -e "use cs122b; select * from user;"

# check the currently deployed apps
ls -lah /home/ubuntu/tomcat/webapps

# run react app build
cd cs122b-spring20-team-53/app
# run one of the following
# for HTTPS
REACT_APP_API_URL=https://ec2-3-94-82-6.compute-1.amazonaws.com:8443/unnamed/api npm run-script build
# for HTTP
REACT_APP_API_URL=http://ec2-3-94-82-6.compute-1.amazonaws.com:8080/unnamed/api npm run-script build

# copy build to web dir
npm run-script predeploy

# build artifact
cd ..
mvn package

#start tomcat
starttomcat

# run parser here
commands are below
cd ..
mkdir xml_creation
cp xml_creation/ cs122b/data_collection_creation/xml_creation

# put artifact in deployable dir
cp target/unnamed.war ~/tomcat/webapps

# check currently deployed apps
ls -lah /home/ubuntu/tomcat/webapps

# execute parser

#add employee creds
"classta@email.edu" and password "classta"

cd ..
mkdir xml_creation
cp xml_creation/ cs122b/data_collection_creation/xml_creation

#artists
mvn exec:java -Dexec.mainClass="com.cs122b.parser.MainParser" -Dexec.cleanupDaemonThreads=false -Dexec.args="data_collection_creation/xml_creation/artists.xml"

#albums
mvn exec:java -Dexec.mainClass="com.cs122b.parser.MainParser" -Dexec.cleanupDaemonThreads=false -Dexec.args="data_collection_creation/xml_creation/albums.xml"
#track
mvn exec:java -Dexec.mainClass="com.cs122b.parser.MainParser" -Dexec.cleanupDaemonThreads=false -Dexec.args="data_collection_creation/xml_creation/tracks.xml"

#track_metas
mvn exec:java -Dexec.mainClass="com.cs122b.parser.MainParser" -Dexec.cleanupDaemonThreads=false -Dexec.args="data_collection_creation/xml_creation/track_metas.xml"

#search things inserted from xml
Searchable Artists
- Guitar Duo
- L’Orchestra Cinematique

Searchable Albums
- In'terview in Concert
- Midnight Mushrumps

Searchable Tracks
- Missed Calls (feat. Hayley Kiyoko)
- Good In Goodbye

# dashboard demo
Songs To Add From Dashboard
When You Come Back Down
- N~~ickel Creek~~

When You Come Back Down
- Kina Grannis

When You Come Back Down
- The UVM Top Cats
```

- #### Collaborations and Work Distribution:

### Lasse Nordahl

- Application routing for new pages
- Cart/Playlist generation functionality for Songs and Albums (Front-end and Back-end)
- Create Playlist Page
- Playlists page
- Playlist Endpoints
- Playlist Session Endpoints
- Spotify Integration
- Create playlist functionality on playlist page
- Implicit Grant Flow on Front-End for Spotify
- Spotify Snapshot SQL and Endpoints
- ReCaptcha integration
- Password Encruption
- Prepared Statement Conversion
- Dashboard Meta Endpoints
- Insert Track/Artist/Album stored procedures and endpoints
- Front-end of Dashboard
- Created android app
- Edited backend endpoints for android integration
- Connection pooling
- Set up Master/Slave instances

### Zachary Pinto

- Write SQL schema
- Write scripts to generate sample data
- Create song selection and artist selection pages
- Create tracks, track, artist, and artists endpoint
- Create SQLClient class
- Connect the API to list views
- Configure for deployment
- Deploy to AWS
- Sorting for albums, artists, songs
- Searching for albums, artists, songs
- Browse by title and genre
- Links genres from artists
- Add query params to URL
- Update user
- Login
- Create user
- HTTPS Conversion
- Employee integration to Login
- XML Creation
- XML Parsing/Insertion/Optimization
- Inconsistency report
- Full Text Search
- Fuzzy Search
- Jmeter Tests
- Jmeter Graphs
- Ts/Tj Logs
- log_processing.py
- AWS and GCP Load Balancer

* # Connection Pooling

  - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.

  * The connection code is handled in an abstracted object utilized in every endpoint that accesses the database
  * SQLClient: [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/web/src/com/cs122b/client/SQLClient.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/web/src/com/cs122b/client/SQLClient.java)
  * Services utilizing SQLClient: [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/tree/master/web/src/com/cs122b/service](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/tree/master/web/src/com/cs122b/service)

  - #### Explain how Connection Pooling is utilized in the Fabflix code.

  * Reference for how we handled our design from a Piazza post: [https://piazza.com/class/k8d7td08e9b1sx?cid=1313](https://piazza.com/class/k8d7td08e9b1sx?cid=1313)
  * In our application, when creating a SQLClient object, a connection is created using the pooling configuration defined in context.xml ([https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/web/web/META-INF/context.xml](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/web/web/META-INF/context.xml)). This resource has a max connection pool of 100 connections available. We also only allow a maximum of 30 connections to become idle at any point in time. The length of time allowed for timeouts is 10000. In terms of how our SQL connection is used throughout the application, all endpoints accessing the database create a SQLClient object that utilize our pooling connection. After the prepared statements are executed, the database connection is terminated with the SQLClient method `void closeConnection()`. This abstracts database connection away from everything else and keeps the code easy to edit through the changing of resource files.

  - #### Explain how Connection Pooling works with two backend SQL.

  * Answer comes from: [https://www.mssqltips.com/sqlservertip/5630/understanding-sql-server-connection-pooling-in-adonet/](https://www.mssqltips.com/sqlservertip/5630/understanding-sql-server-connection-pooling-in-adonet/)
  * When we create connection pools for multiple database instances on the backend, it will pool together connections to the same database together. So, if you have a slave and master instance connection opened, then if you try to create a new slave connection, it will connect that connection to the slave pool. This lets the program pool together multiple different connections but have them separated by database. For our implementation, because we have the tomcat instances reading from their local databases, there isn't any handling to worry about this though. Write commands are hardcoded to be sent to the master IP as described in the lecture and Piazza post. In terms of our implementation, a better description can be found in the master/slave section below.

- # Master/Slave

  - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.

  * We did not have any code/configuration files for routing queries as we did not use the mysql router. Code for utilizing different servers can be found in the SQLClient object below.
  * SQLClient: [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/web/src/com/cs122b/client/SQLClient.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/web/src/com/cs122b/client/SQLClient.java)

- #### How read/write requests were routed to Master/Slave SQL?

  - We based our architecture based on the piazza post listed here: [https://piazza.com/class/k8d7td08e9b1sx?cid=1313](https://piazza.com/class/k8d7td08e9b1sx?cid=1313)
  - For our project, we made each Tomcat instance access the local SQL server, and the write access will always access the master instance through the public IP (Security groups are defined, it uses the public IP because we had our VM's on different AWS accounts so we couldn't use the inner AWS IP). When write requests are made on the master instance, they are replicated on the slave instance. This means that all requests on either Tomcat instance will be acting with the same set of data, but be distributed across multiple instances.
  - In general, under a different backend configuration, we would set up mysql-router to be used for distributing requests across the Master/Slave instances. This would list a series of slave instances as readonly under a specific port on the local machine (Say 7000), and when a connection is created, it will pick the next valid slave instance defined in the router. We would also list master instances under a read/write port (Say 7001), and when we create a connection for write requests, it will pick the next available master mysql instance. We decided not to utilize this for our project as we still had to configure our backend to utilize a different URL for write requests anyway. With more master/slave mysql instances we would utilize this.

* # JMeter TS/TJ Time Logs [Log File](/web/jmeter/logs) [log_processing.py](/web/jmeter/log_processing.py)
  - If you were to run this server the, the logs would be written in the directory where the server was started from.
  - The logger implementation can be found in [MyLogger.java](web/src/com/cs122b/utils/MyLogger.java)
    - This logger is globally available and is called in the servlets and the service functions called by the servlets.
    - They write logs in the format of `[test_name]:<Ts or Tj>:<time>`
  - log_processing.py will read `tstjlogs.log` and generate a report that lists each type of test along with its time.
    - Parses through each line which is one log
  - To Run:
    - Place log file named `tstjlogs.log` in the directory
    - Run `python3 log_processing.py`
    - Report is generated in nano seconds

- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**       | **Graph Results Screenshot**                                                                                 | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** |
| ------------------------------------------- | ------------------------------------------------------------------------------------------------------------ | -------------------------- | ----------------------------------- | ------------------------- |
| Case 1: HTTP/1 thread (Master)              | ![Case 1 (Master)](/web/jmeter/graphs/Master_Single-Instance_Case_1_HTTP1_thread.png)                        | 236                        | 6.1885944453069869908               | 3.3740147004424780874     |
| Case 2: HTTP/10 threads (Slave)             | ![Case 2 (Slave)](/web/jmeter/graphs/Slave_Single-Instance_Case_2_HTTP10_threads.png)                        | 301                        | 5.7474654747474751559               | 3.075768626               |
| Case 3: HTTPS/10 threads (Orig)             | ![Case 3 (Orig)](/web/jmeter/graphs/Orig_Single-Instance_Case_3_HTTPS10_threads.png)                         | 523                        | 6.188594445306979885                | 3.374014700442470094      |
| Case 4: HTTP/10 threads/No pooling (Master) | ![Case 4 (Master)](/web/jmeter/graphs/Master_Single-Instance_Case_4_HTTP10_threadsNo_connection_pooling.png) | 323                        | 47.220282155840003                  | 5.1260523264977884139     |

| **Scaled Version Test Plan**             | **Graph Results Screenshot**                                                                          | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** |
| ---------------------------------------- | ----------------------------------------------------------------------------------------------------- | -------------------------- | ----------------------------------- | ------------------------- |
| Case 1: HTTP/1 thread (AWS)              | ![Case 1 (AWS)](/web/jmeter/graphs/AWS_Scaled-Version_Case_1_HTTP1_thread.png)                        | 239                        | 3.2250253038548755136               | 1.8114671325648417088     |
| Case 2: HTTP/10 threads (AWS)            | ![Case 2 (AWS)](/web/jmeter/graphs/AWS_Scaled-Version_Case_2_HTTP10_threads.png)                      | 287                        | 3.74075282245549978                 | 2.1926598448863634516     |
| Case 2: HTTP/10 threads (GCP)            | ![Case 2 (GCP)](/web/jmeter/graphs/GCP_Scaled-Version_Case_2_HTTP10_threads.png)                      | 247                        | 3.468074322267619802                | 2.0890288903225808781     |
| Case 3: HTTP/10 threads/No pooling (AWS) | ![Case 3 (AWS)](/web/jmeter/graphs/AWS_Scaled-Version_Case_3_HTTP10_threadsNo_connection_pooling.png) | 316                        | 32.264295990851515228               | 5.0170719070175442411     |

## Pooling

- Based on the results of our tests, it seems that pooling is significantly more efficient when it comes to reading from the database.
- This will allows faster response time and increases throughput.

## Load Balancing

- While load balancing did not necessarily reduce the time for each request, It did allow for higher through put which is equally as important as response time.
- One reason that it may have been slightly slower may stem from the fact that the load balancer is under a different AWS account to reduce the chances of being charged for multiple instances. This means that it cannot use the private network inside AWS.

## Extra

- It seems that the original instance of Synthesizer performed poorly compared to the others.
- This may have been due to some kind of memory leak in tomcat, since this instance of tomcat has been running longer than the others.
