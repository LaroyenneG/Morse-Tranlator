#!/bin/bash



(cd ./document/; pdflatex Morse-Tranlator.tex);

rm  -r out/

mkdir -p out/production/Morse-Translator/morse/translator/assets/ 2>/dev/null;

javac -classpath . ./src/morse/translator/*.java ./src/*.java -d out/production/Morse-Translator/;
cp ./src/morse/translator/assets/morse_code.txt ./out/production/Morse-Translator/morse/translator/assets/morse_code.txt;


cp ./document/Morse-Tranlator.pdf ./out/production/Morse-Translator/;


cd ./out/production/Morse-Translator/;
jar cvmf ../../../MANIFEST.MF Morse-Translator.jar *.class ./morse/translator/*.class  ./morse/translator/assets/* ./Morse-Tranlator.pdf ../../../src;

exit;
