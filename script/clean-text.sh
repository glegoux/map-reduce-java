#!/usr/bin/env bash

declare -r filename="$1"
declare -r excluded_word='un une des la le l les au aux du mon ma mes ton '\
'ta tes sonsa see notre nos votre vos leur leurs ce cett cet ces a et ou '\
'no or car ainsi'

get_words() {
  local words='\('
  local first=true
  for word in $excluded_word; do
    if $first; then
      words="$words $word "
      first=false
    else
      words="$words \| $word"
    fi
  done
  words="$words \)"
  echo -n "$words"
}

cat ${filename} \
  | tr '\r\t' ' ' \
  | tr '[:punct:]' ' ' \
  | sed '/^[ ]*$/d' \
  | tr '[:upper:]' '[:lower:]' \
  | sed 's/[^-a-zA-Z ]//g' \
  | sed 's/ \+/ /g' \
  | iconv -f utf8 -t ascii//TRANSLIT//IGNORE \
  | sed "s/$/ /" \
  | sed "s/^/ /" \
  | sed "s/`get_words`/ /g" \
  | sed "s/$ \+//" \
  | sed "s/^ \+//"

