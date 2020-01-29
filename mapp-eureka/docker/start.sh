#!/usr/bin/env bash

#docker run -d -p 8761:8761 -v /home/web/logs:/logs:rw --privileged=true --name mapp-eureka mapp-eureka
docker-compose up -d
docker-compose ps