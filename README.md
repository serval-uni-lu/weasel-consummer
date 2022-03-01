# Weasel model consummer

### Reqs
- java version 11 in path
- maven version 3.8.4 in path

### Setup
1. To build the project run `mvn clean install`
2. Move the model file to the disered location
3. Find a free port on the machine
4. Start the server with `mvn spring-boot:run`
5. Additional properties (e.g. port model path) can be passed in 
   1. `ressources/application.properties` 
   2. via command line arguments `-Dspring-boot.run.arguments=--server.port=8080,--weasel.model.path=/path/to/model`

e.g. ``mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8080,--weasel.model.path=/path/to/model``
