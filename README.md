Running the program

You must have the a recent Gradle version installed in your system. This can be done by following the Gradle installation steps on https://gradle.org.

When using the program first ensure that you are within the T16_R1_GROUP1A1 folder.

If you are running the program for the first time run the following commands:
1. gradle clean build
2. gradle run
The GUI should appear on your screen and you may begin using the program.

If you are running the program but it is not the first time:
1. gradle run
The GUI should appear on your screen and you may begin using the program.

If you want to run the tests for the program run the following command:
1. gradle test
The test names will appear in the terminal with an indication as to whether said test passed or failed.

Note that to run the program as desired the java fx package, version 12.0.2 or higher is required to view the GUI that displays the output of the program which may be downloaded from
https://openjfx.io.

Additionally, an executable Jar file for the project can be made by following the steps on https://ktor.io/docs/fatjar.html#build.
