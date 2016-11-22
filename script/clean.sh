#!/usr/bin/env bash

cd `dirname $0` && \
cd "../master" && \
mvn clean && \
cd "../slave" && \
mvn clean
