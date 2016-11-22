#!/usr/bin/env bash
# Launch the word count with map reduce algorithm
# for chosen file.

# absolute path name of the file 
# or relative path from folder containing master.jar
declare -r MODE=${1:-local}
declare -r FILENAME=${2:-data/input.txt}


cd "`dirname "$0"`" && \
cd .. && \
java -jar master.jar "${MODE}" "${FILENAME}"
