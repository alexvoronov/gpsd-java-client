#!/bin/bash

export PRJ="$(cd `dirname $0`; pwd)"

set -x

rsync -a \
    --exclude='*~' \
    --exclude='#*' \
    --exclude='.#*' \
    $PRJ/target/gpsd-client-1.0.jar \
    eee:/tmp
