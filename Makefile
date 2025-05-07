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

lint-deb: build-deb
	-lintian graph_comp.deb

setup-dependencies:
	apt update
	apt install -y maven python3-pip checkstyle lintian