#!/bin/sh
BUILD_DIR=build

# Compile
mkdir -p $BUILD_DIR
javac -d $BUILD_DIR src/*

# Run
cd $BUILD_DIR
java Client $@