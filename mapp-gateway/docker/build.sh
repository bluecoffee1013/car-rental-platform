#!/usr/bin/env bash

sh ./stop.sh
docker rmi mapp-gateway
cd ../../
mvn clean compile package -Dmaven.test.skip=true

cd mapp-gateway
mvn docker:build -Dmaven.test.skip=true