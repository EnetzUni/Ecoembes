There are two options for running this project:

*Import into Eclipse and then, in the Gradle Tasks view, double-click on the project name under application and select run.
* Use the Command Line: Open a command console and execute the command .\gradlew run.

By default, the socket server operates on port 9500.

To change the port, use the following command:
.\gradlew run -Parg1=6000 (or specify any port of your choice).