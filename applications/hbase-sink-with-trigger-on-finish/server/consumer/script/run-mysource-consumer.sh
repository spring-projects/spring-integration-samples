#!/bin/sh
#
# Run an analysis of the dataset to print out statistics
export MAVEN_OPTS="-Xmx10g"

cd ..

mvn exec:java -Dexec.mainClass="org.springframework.integration.samples.server.Consumer" -Dexec.args="mysource"
