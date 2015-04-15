
all: dependencies build

dependencies:
	(cd libs/ && ./install.sh)
	
build:
	mvn package

