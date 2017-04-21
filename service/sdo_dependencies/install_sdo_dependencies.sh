#!/bin/bash

mvn install:install-file \
	-Dfile=./sdoapi.jar \
	-DgroupId=com.oracle.spatial \
    	-DartifactId=sdoapi \
	-Dversion=12.1.0.2 \
	-Dpackaging=jar
mvn install:install-file \
	-Dfile=./sdondme.jar \
	-DgroupId=com.oracle.spatial \
    	-DartifactId=sdondme \
	-Dversion=12.1.0.2 \
	-Dpackaging=jar
mvn install:install-file \
	-Dfile=./sdonm.jar \
	-DgroupId=com.oracle.spatial \
    	-DartifactId=sdonm \
	-Dversion=12.1.0.2 \
	-Dpackaging=jar
mvn install:install-file \
	-Dfile=./sdoutl.jar \
	-DgroupId=com.oracle.spatial \
    	-DartifactId=sdoutl \
	-Dversion=12.1.0.2 \
	-Dpackaging=jar

exit 0
