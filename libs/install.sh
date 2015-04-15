#!/bin/bash

mvn install:install-file \
   -Dfile=bboxchooser-0.3.0.jar \
   -DgroupId=de.osmembrane.thirdparty \
   -DartifactId=bboxchooser \
   -Dversion=0.3.0 \
   -Dpackaging=jar

mvn install:install-file \
   -Dfile=jmapviewer-0.1.0.jar \
   -DgroupId=de.osmembrane.thirdparty \
   -DartifactId=jmapviewer \
   -Dversion=0.1.0 \
   -Dpackaging=jar
