Airplane seating algorithm:

I have created a REST end point using Spring Boot Framework which will accept two inputs

1. inputArray - 2d array
2. numberOfPassengers - integer

Based on the inputs it will perform seating passengers as per rules mentioned in Airplane-SS.pdf

Output returned is a list of string where each entry in the list represent each row in airplane.

In each row, data is comma separated where each item represents passenger number along with seat category.

Seat Category:
A - Aisle
W - Window
C - Center

This Rest Service is consumed using POSTMAN, Kindly refer "RestInputOutput.PNG" image for viewing input and output format of this rest service.

The logic are written in AirplaneSeatingController.java
The tests are performed in AirplaneSeatingApplicationTests.java
Postman request template is available in Airplane Seating.postman_collection.json

