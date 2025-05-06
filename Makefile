# Author: Henry Stone

run:
	mvn clean compile exec:java

test:
	mvn test

clean:
	rm -f *.png
	rm -f *.deb

build-deb:
	./debBuild.sh

deb-lint: build-deb
	-lintian *.deb