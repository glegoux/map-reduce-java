#!/usr/bin/env bash

filename=${1:-../shared-folder/output}

cd "`dirname "$0"`" && \
sort --field-separator=',' --key=2 --reverse "${filename}" | head
