#!/bin/bash


CLASSPATH=`echo build/*.jar`
for i in ${ES_HOME}/libexec/*.jar;
do
    CLASSPATH="${CLASSPATH}:${i}"
done

java -cp ${CLASSPATH} IndexDumper $@
