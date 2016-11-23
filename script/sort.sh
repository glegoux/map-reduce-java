#!/usr/bin/env bash

filename=${1:-../shared-folder/output}
nb_of_words=${2:-50}

cd "`dirname "$0"`" && \
sort --field-separator=',' --key=2  --numeric-sort --reverse "${filename}" | head -n ${nb_of_words}
