#!/usr/bin/env bash


PORT=7017

findAtPort() { lsof -i :$1; }

killAtPort()
{
    for port in "$@"
	do
	    findAtPort "$port" | awk 'FNR == 2{print $2'} | xargs kill;
	done
}

killAtPort $PORT
echo "Server Killed at port $PORT"
