#!/usr/bin/env bash

docker run --rm -d --name sockswarehouse -p 5432:5432 -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=sockswarehouse postgres:14.7-alpine