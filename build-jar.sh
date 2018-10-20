#!/bin/bash


javac -classpath . ./src/morse/translator/*.java ./src/*.java
jar cvmf MANIFEST.MF MorseTranslator.jar *.class ./document/Morse-Tranlator.pdf/ ./src/morse/translator/assets/morse_code.txt