# OSMembrane - The GUI for [osmosis](https://github.com/openstreetmap/osmosis) [![Build Status](https://travis-ci.org/openstreetmap/osmembrane.svg?branch=master)](https://travis-ci.org/openstreetmap/osmembrane)
OSMembrane is a frontend to the Osmosis data processing tool. It helps by grouping tasks in functions and shows a visual representation of the pipeline. Additionally, it features a bounding box chooser and an AutoComplete list of key/key.value lists imported from JOSM.

![screenshot](http://wiki.openstreetmap.org/w/images/0/09/OSMembraneThumb.png)

## Install the current version
A build of a recent version is always available on GitHub, you can download it [here](https://github.com/openstreetmap/OSMembrane/releases).

[Java 8](https://www.java.com/de/download/) is required to run OSMembrane. After the installation of Java just double click the downloaded file.

A quick [tutorial](https://github.com/openstreetmap/OSMembrane/blob/master/manual/manual.pdf) on how to use OSMembrane you can find [here](https://github.com/openstreetmap/OSMembrane/blob/master/manual/manual.pdf).

## Building the application
The repository contains all necessary files to build the application from scratch. Only a current version of [Apache Maven](https://maven.apache.org/download.cgi) is required. To actually build the application simply run the Makefile. Windows users can use [Vagrant](https://www.vagrantup.com/downloads.html) to get a matching build environment.

```
# Installs required custom maven dependencies in the local repository
# This command must only be executed once
~ $ make depdendencies

# Actually builds the application
~ $ make build
```

The resulting jar is located in the `target` folder.

## Credits
OSMembrane was developed by Christian Endres, Jakob Jarosch and Tobias Kuhn during a practical student project at the University of Stuttgart which was supervised by Igor Podolskiy.
