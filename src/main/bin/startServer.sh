#!/usr/bin/env bash

version="1.0"
jar="aroma-email-service-$version.jar"

nohup java -jar $jar > server.log &
