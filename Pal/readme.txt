Final Project
Symbolic Programming 458
Joseph Sackett
Mar. 19, 2014

Installation:
1) Unzip project file.
2) Open command prompt.
3) Navigate to unzipped project file area.
4) Place javac and java in your path.
5) Execute this command to compile source:
   	javac -cp .;clojure-1.5.1.jar PalletizerCj.java
   	Note: This is distributed prebuilt so above step is redundant.
6) Execute this command to execute program:
	java -cp .;clojure-1.5.1.jar PalletizerCj
	Note: Batch file run_palletizer.bat will execute the above step.
7) 2D Palletizer application window should open.
   New - creates new list of items and clears pallet.
   Shuffle - shuffles existing list of items and clears pallet.
   Palletize - palletizes the items into the pallet.
