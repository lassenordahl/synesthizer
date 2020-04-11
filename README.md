# CS122B Songs Project

## Video Links

### Project 1

- https://www.youtube.com/watch?v=t2Wi-SXb-Z4

## Deployment Instructions

Before creating the artifact, the react app needs to be built and copied over to the web directory of the project.

```
cd cs122b-spring20-team-53
mysql -u cs122b -p -e "show databases;"

mysql -u cs122b -p < create_table.sql

cd ..
mysql -u cs122b -p --database=cs122b < CreationInsertion.sql

mysql -u cs122b -p -e "use cs122b;select count(*) from artist;select count(*) from tracks;"


#start tomcat server
source /home/ubuntu/tomcat/bin/startup.sh

# run react app build
cd cs122b-spring20-team-53/app
REACT_APP_API_URL=http://ec2-3-94-82-6.compute-1.amazonaws.com:8080/unnamed/api
npm run-script build

# copy build to web dir
npm run-script predeploy

# build artifact
cd ..
mvn package

# check the currently deployed apps
ls -lah /home/ubuntu/tomcat/webapps

# put artifact in deployable dir
cp target/unnamed.war ~/tomcat/webapps

# check currently deployed apps
ls -lah /home/ubuntu/tomcat/webapps
```

## Project Contributions

### Lasse Nordahl

- Start React App
- Do main css
- Design song, artist, album list view
- Create track_meta, album, track_in_album endpoints
- Create album selection page
- Add hyperlinks to single view pages
- Add popularity sorting to album and song endpoint

### Zachary Pinto

- Write SQL schema
- Write scripts to generate sample data
- Create song selection and artist selection pages
- Create tracks, track, artist, and artists endpoint
- Create SQLClient class
- Connect the API to list views
- Configure for deployment
- Deploy to AWS
