#!/usr/bin/env bash

declare -r DEST="${1:-${HOME}/map-reduce-env/}"

cd `dirname $0` && \
mkdir -vp "${DEST}" && \
mkdir -vp "${DEST}/shared-folder/" && \
mkdir -vp "${DEST}/profiler/" && \
touch "${DEST}/profiler/profiler.log" && \
cp -vfr "../data/" "${DEST}" && \
cp -vfr "../script/" "${DEST}" && \
cp -vfr "../config/" "${DEST}" && \
cd "../master" && \
mvn clean install && \
cp -vf "./target/map-reduce-master-1.0.0-SNAPSHOT-jar-with-dependencies.jar" \
"${DEST}/master.jar"  && \
cd ../slave && \
mvn clean install && \
cp -vf "./target/map-reduce-slave-1.0.0-SNAPSHOT-jar-with-dependencies.jar" \
"${DEST}/slave.jar"
