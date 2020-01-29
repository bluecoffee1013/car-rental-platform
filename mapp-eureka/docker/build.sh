#!/usr/bin/env bash

sh ./stop.sh
docker rmi mapp-eureka
cd ../../
mvn clean compile package -Dmaven.test.skip=true

cd mapp-eureka
mvn docker:build -Dmaven.test.skip=true