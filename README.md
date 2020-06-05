- # General

  - #### Team#: Team 53

  - #### Names: Zachary Pinto and Lasse Nordahl

  - #### Project 5 Video Demo Link:

  - #### Instruction of deployment:

  - #### Collaborations and Work Distribution:

* # Connection Pooling
  - #### Include the filename/path of all code/configuration files in GitHub of using JDBC Connection Pooling.
        - The connection code is handled in an abstracted object utilized in every endpoint that accesses the database
        - SQLClient: [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/web/src/com/cs122b/client/SQLClient.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/web/src/com/cs122b/client/SQLClient.java)
        - Services utilizing SQLClient: [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/tree/master/web/src/com/cs122b/service](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/tree/master/web/src/com/cs122b/service)

  - #### Explain how Connection Pooling is utilized in the Fabflix code.
          - Reference for how we handled our design from a Piazza post: [https://piazza.com/class/k8d7td08e9b1sx?cid=1313](https://piazza.com/class/k8d7td08e9b1sx?cid=1313)
        - In our application, when creating a SQLClient object, a connection is created using the pooling configuration defined in context.xml ([https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/web/web/META-INF/context.xml](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/web/web/META-INF/context.xml)). This resource has a max connection pool of 100 connections available. We also only allow a maximum of 30 connections to become idle at any point in time. The length of time allowed for timeouts is 10000. In terms of how our SQL connection is used throughout the application, all endpoints accessing the database create a SQLClient object that utilize our pooling connection. After the prepared statements are executed, the database connection is terminated with the SQLClient method `void closeConnection()`. This abstracts database connection away from everything else and keeps the code easy to edit through the changing of resource files.

  - #### Explain how Connection Pooling works with two backend SQL.
          - Answer comes from: [https://www.mssqltips.com/sqlservertip/5630/understanding-sql-server-connection-pooling-in-adonet/](https://www.mssqltips.com/sqlservertip/5630/understanding-sql-server-connection-pooling-in-adonet/)
        - When we create connection pools for multiple database instances on the backend, it will pool together connections to the same database together. So, if you have a slave and master instance connection opened, then if you try to create a new slave connection, it will connect that connection to the slave pool. This lets the program pool together multiple different connections but have them separated by database. For our implementation, because we have the tomcat instances reading from their local databases, there isn't any handling to worry about this though. Write commands are hardcoded to be sent to the master IP as described in the lecture and Piazza post. In terms of our implementation, a better description can be found in the master/slave section below.

- # Master/Slave

  - #### Include the filename/path of all code/configuration files in GitHub of routing queries to Master/Slave SQL.
        - We did not have any code/configuration files for routing queries as we did not use the mysql router. Code for utilizing different servers can be found in the SQLClient object below.
        - SQLClient: [https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/web/src/com/cs122b/client/SQLClient.java](https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-53/blob/master/web/src/com/cs122b/client/SQLClient.java)
  - #### How read/write requests were routed to Master/Slave SQL?
        - We based our architecture based on the piazza post listed here: [https://piazza.com/class/k8d7td08e9b1sx?cid=1313](https://piazza.com/class/k8d7td08e9b1sx?cid=1313)
        - For our project, we made each Tomcat instance access the local SQL server, and the write access will always access the master instance through the public IP (Security groups are defined, it uses the public IP because we had our VM's on different AWS accounts so we couldn't use the inner AWS IP). When write requests are made on the master instance, they are replicated on the slave instance. This means that all requests on either Tomcat instance will be acting with the same set of data, but be distributed across multiple instances.
        - In general, under a different backend configuration, we would set up mysql-router to be used for distributing requests across the Master/Slave instances. This would list a series of slave instances as readonly under a specific port on the local machine (Say 7000), and when a connection is created, it will pick the next valid slave instance defined in the router. We would also list master instances under a read/write port (Say 7001), and when we create a connection for write requests, it will pick the next available master mysql instance. We decided not to utilize this for our project as we still had to configure our backend to utilize a different URL for write requests anyway. With more master/slave mysql instances we would utilize this.
* # JMeter TS/TJ Time Logs
  - #### Instructions of how to use the `log_processing.*` script to process the JMeter logs.

- # JMeter TS/TJ Time Measurement Report

| **Single-instance Version Test Plan**         | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
| --------------------------------------------- | ---------------------------- | -------------------------- | ----------------------------------- | ------------------------- | ------------ |
| Case 1: HTTP/1 thread                         | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 2: HTTP/10 threads                       | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 3: HTTPS/10 threads                      | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 4: HTTP/10 threads/No connection pooling | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |

| **Scaled Version Test Plan**                  | **Graph Results Screenshot** | **Average Query Time(ms)** | **Average Search Servlet Time(ms)** | **Average JDBC Time(ms)** | **Analysis** |
| --------------------------------------------- | ---------------------------- | -------------------------- | ----------------------------------- | ------------------------- | ------------ |
| Case 1: HTTP/1 thread                         | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 2: HTTP/10 threads                       | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
| Case 3: HTTP/10 threads/No connection pooling | ![](path to image in img/)   | ??                         | ??                                  | ??                        | ??           |
