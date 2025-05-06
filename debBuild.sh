#!/bin/sh

TMP_DIR=tmp

echo "Starting debian package build"

mkdir -p $TMP_DIR/src/main/java/cpsc450/
mkdir -p $TMP_DIR/src/test/java/cpsc450/
mkdir -p target/
mkdir -p $TMP_DIR/DEBIAN/

echo "copying files"
echo "DEBIAN files"
cp ./DEBIAN/control $TMP_DIR/DEBIAN/
cp ./DEBIAN/postinst $TMP_DIR/DEBIAN/

echo "copy target directory"
cp -r ./target $TMP_DIR/target/

echo "copy code"
cp -r ./src/main/java/cpsc450 $TMP_DIR/src/main/java/cpsc450/

echo "copy tests"
cp -r ./src/test/java/cpsc450 $TMP_DIR/src/test/java/cpsc450/

echo "copy pom"
cp -r ./pom.xml $TMP_DIR/

echo "Building deb file"
dpkg-deb --root-owner-group --build $TMP_DIR
mv $TMP_DIR.deb graph_comp.deb


echo "Complete."