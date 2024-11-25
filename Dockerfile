FROM eclipse-temurin:17
COPY target/hotelbookingapp.jar hotelbookingapp.jar
CMD [ "java","-jar","hotelbookingapp.jar" ]