# OSMembrane - The GUI for osmosis [![Build Status](https://travis-ci.org/foxylion/OSMembrane.svg?branch=master)](https://travis-ci.org/foxylion/OSMembrane)
OSMembrane is a frontend to the Osmosis data processing tool. It helps by grouping tasks in functions and shows a visual representation of the pipeline. Additionally, it features a bounding box chooser and an AutoComplete list of key/key.value lists imported from JOSM.

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