Objective :

  The primary goal of this task is to implement the following two microservices :
  1- Customer Microservice: responsible for the management of organization customers
  ( handles customer creation, updates, deletions and stores them in an H2 database ) and sends notifications to a RabbitMQ queue.
  
  2- Notification Microservice: responsible for managing notifications,Consumes notifications 
  from RabbitMQ, stores them in an H2 database, and broadcasts them to WebSocket  using STOMP.

 Customer Microservice :
 
 Design Decisions
   1- Architecture
     Manages customer data with operations to create, update, and delete customers
     and store  them in H2 database based  on table relationship with customerOrgnization table and Address table.
     Publishes customer events baes on Operations (Customer-created, Customer updated,Customer deleted) 
     to RabbitMQ for further processing by the Notification Microservice.

   2-Databasd:
    used H2 In memory database. you check check ot  by 
    (http://localhost:8080/h2-console) 
      with credinatial mentioned in application.prperties file.

   3- RabbitMQ is used for sending customer events to the Notification Microservice.

   4-Validation: API requests are validated to ensure that customer data is correct and meets the required constraints
   (Ex: If the customer of type Organization, the the org info MUST be filled.)

   5-Documentation: Swagger (Springdoc OpenAPI) is used to provide API documentation accessible at 
   (http://localhost:8080/swagger-ui/index.html)

   6- Actuator: Integrated Actuator for observability metrics (e.g., Prometheus metrics)
   and customized health-check endpoint by provide extra info (project name, version, latest build date).
     accessible at (http://localhost:8080/actuator/prometheus) ,
     (http://localhost:8080/actuator/health).
   
 Shortcomings
  -Scalability: May require further optimization for high load scenarios .
  -the code not handling  all cases of issues may happend with RabbitMQ .
  
 Assumptions :
 -RabbitMQ Configuration: Assumes RabbitMQ is properly configured and running.

 Notification Microservice :
 
 Design Decisions
 1- Architecture
 Consumes customer notifications from RabbitMQ, stores them in an H2 in-memory database,
 and broadcasts them to WebSocket clients using STOMP.
 Maintains WebSocket sessions for active subscribers and uses a thread pool to send notifications in parallel.
 you can connect to websocket and read message 
 using UI :  http://localhost:8081/index.html 

2- WebSocket Communication:
STOMP over WebSocket is used to send notifications to clients.

3-Concurrency:
Notifications are dispatched using a thread pool with 5 threads to handle sending notifications
to multiple WebSocket clients in parallel.

4- Actuator: Integrated Actuator for observability metrics (e.g., Prometheus metrics) 
and customized health-check endpoint by provide extra info (project name, version, latest build date).
     accessible at (http://localhost:8080/actuator/prometheus) ,(http://localhost:8080/actuator/health).
     
5-Documentation: Swagger (Springdoc OpenAPI) is used to provide API documentation accessible 
at (http://localhost:8080/swagger-ui/index.html)

Shortcomings:
-Scalability: H2 in-memory database may not be suitable for production; consider a persistent database.
-the recived message  after sent to Websocket not forword to any Microservices
-the code not handling  all cases of issues may happend with RabbitMQ or WebSocket.

Assumptions:
WebSocket Clients: Assumes clients use SocketJS or compatible libraries to connect via WebSocket.

Testing
Customer Microservice: Test customer databse  operations, validation, and RabbitMQ messaging.
Notification Microservice: Test WebSocket connection management, notification broadcasting, 
and RabbitMQ event handling. Ensure parallel thread execution for notification dispatch.
