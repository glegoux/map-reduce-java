#!/usr/bin/env bash
# execution:
# bash gen-ip.sh c129 36 > ip.txt

room=$1 
number_machines=$2

for n in `seq 1 ${number_machines}`; do
  echo "${room}-`printf "%02d" $n`"
done
