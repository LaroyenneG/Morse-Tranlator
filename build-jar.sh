#!/bin/bash



(cd ./document/; pdflatex Morse-Tranlator.tex);

rm  -r out/

mkdir -p out/production/Morse-Translator/morse/translator/assets/ 2>/dev/null;

javac -classpath . ./src/morse/translator/*.java ./src/*.java -d out/production/Morse-Translator/;
cp ./src/morse/translator/assets/* ./out/production/Morse-Translator/morse/translator/assets/;


cp ./document/Morse-Tranlator.pdf ./out/production/Morse-Translator/;


cd ./out/production/Morse-Translator/;
jar cvmf ../../../MANIFEST.MF Guillaume-Laroyenne.jar *.class ./morse/translator/*.class  ./morse/translator/assets/* ./Morse-Tranlator.pdf ../../../src;

exit;
