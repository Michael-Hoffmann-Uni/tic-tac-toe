# Tic-Tac-Toe
## Summary
This project is a networking implementation ([Spring-Framework](https://en.wikipedia.org/wiki/Spring_Framework)) of the commonly known game [Tic-Tac-Toe](https://en.wikipedia.org/wiki/Tic-tac-toe). Focus of the project was to use a structure that makes it possible to add other games later on (e.g. chess) by implementing the provided interfaces.

## Configuration
#### Gradle
The app uses libraries that are not included in the standard Java JDK. To include all depencencies a [Gradle](https://en.wikipedia.org/wiki/Gradle) script was added. Most IDEs are able to recognize the script automatically and will import the necessary libraries. If that is not the case you can do it manually. To do that in IntelliJ please access the Gradle menu on the right hand side and click the button with the two circling arrows (Reload All Gradle Projects).
#### Run application
To start a server on a machine you need to run StartServerUtility in the package src.jpp.games.networking.server. The application will run on port 61361. 
To start clients you need to run GameClient in the package src.jpp.games.networking.client. Multiple clients can run parallel on the same machine.
#### Commands
After starting a client the user will be asked to enter a username. This will register the user. Afterwards the following commands are available:
* list - list of open games
* create [name] - create new game with [name]
* join [name] - join a game
* play [move] - submit a move to the current game (e.g. R1C2 for Row=1 and Column=2)
* leave - leave current game
* hide - hide gameboard
* show - show gameboard
* exit - exit client
