#!/usr/bin/env bash

ant && ant -f jarbuild.xml && rlwrap java -jar ./javaScheme.jar
