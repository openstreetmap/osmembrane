# OSMembrane - The GUI for [osmosis](https://github.com/openstreetmap/osmosis) [![Build Status](https://travis-ci.org/openstreetmap/OSMembrane.svg?branch=master)](https://travis-ci.org/openstreetmap/OSMembrane)
OSMembrane is a frontend to the Osmosis data processing tool. It helps by grouping tasks in functions and shows a visual representation of the pipeline. Additionally, it features a bounding box chooser and an AutoComplete list of key/key.value lists imported from JOSM.

![screenshot](http://wiki.openstreetmap.org/w/images/thumb/0/09/OSMembraneThumb.png/640px-OSMembraneThumb.png)

## Install the current version
A build of a recent version is always available on GitHub, you can download it [here](https://github.com/openstreetmap/OSMembrane/releases).

To run OSMembrane is [Java 8](https://www.java.com/de/download/) required. After the installation of Java just double click the downloaded file.

A quick [tutorial](https://github.com/openstreetmap/OSMembrane/blob/master/manual/manual.pdf) on how to use OSMembrane you can find [here](https://github.com/openstreetmap/OSMembrane/blob/master/manual/manual.pdf).

## Building the application
The repository contains all necessary files to build the application from scratch. Only a current version of [Apache Maven](https://maven.apache.org/download.cgi) is required. To actually build the application simply run the Makefile.

```
# Installs required custom maven dependencies in the local repository
# This command must only be executed once
~ $ make depdendencies

# Actually builds the application
~ $ make build
```

The resulting jar is located in the `target` folder.
