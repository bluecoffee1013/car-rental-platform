#!/usr/bin/env bash

sh ./stop.sh
docker rmi mapp-flow
cd ../../
mvn clean compile package -Dmaven.test.skip=true

cd mapp-flow
mvn docker:build -Dmaven.test.skip=true