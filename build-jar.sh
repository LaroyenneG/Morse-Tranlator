#!/bin/bash


rm  -r out/

mkdir -p out/production/morse/translator/assets/ 2>/dev/null;

javac -classpath . ./src/morse/translator/*.java ./src/*.java -d out/production/;
cp ./src/morse/translator/assets/morse_code.txt ./out/production/morse/translator/assets/morse_code.txt;
cp ./document/Morse-Tranlator.pdf ./out/production/;


cd ./out/production/;
jar cvmf ../../MANIFEST.MF MorseTranslator.jar *.class ./morse/translator/*.class  ./morse/translator/assets/morse_code.txt ./Morse-Tranlator.pdf ../../src;

exit;