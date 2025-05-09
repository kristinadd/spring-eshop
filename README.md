mvn spring-boot:run

mvn clean package

<!-- run in java way -->
java -jar target/ecom-0.0.1-SNAPSHOT.jar

<!-- API -->
http://localhost:8090/ecom/product/getall
http://localhost:8090/ecom/product/get/9
