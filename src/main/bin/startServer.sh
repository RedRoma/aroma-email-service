#!/usr/bin/env bash

version="1.1-SNAPSHOT"
jar="aroma-email-service-$version.jar"

nohup java -jar $jar > server.log &
