#!/usr/bin/env bash

sh ./stop.sh
docker rmi mapp-mgt-webservice
cd ../../
mvn clean compile package -Dmaven.test.skip=true

cd mapp-mgt-webservice
mvn docker:build -Dmaven.test.skip=true