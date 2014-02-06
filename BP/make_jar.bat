REM Builds an executable JAR file for the application.

REM If javac & jar not in your path, set the path below to your JDK.
REM set path=%path%;C:\Tools\Java\jdk1.7.0_40\bin

javac com\sackett\reify\bp\BinPacking.java
jar cfm Palletizer2D.jar Manifest.txt com/sackett/reify/bp/*
