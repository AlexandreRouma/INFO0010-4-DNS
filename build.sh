#!/bin/sh
BUILD_DIR=build

# Compile
mkdir -p $BUILD_DIR
find src -name "*.java" > $BUILD_DIR/sources.txt
javac -d $BUILD_DIR @$BUILD_DIR/sources.txt