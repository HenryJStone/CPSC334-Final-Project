# Author: Henry Stone

run:
	mvn clean compile exec:java

test:
	mvn test

clean:
	rm -f *.png

build-deb:
	./debBuild.sh