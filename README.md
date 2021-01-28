# **micronaut-casino** 
*Micronaut Web Application Backend with Docker for testing of different functionalities and fun*

  Application is reactive (rxjava2)
  
  Build with gradle and secured via Micronaut-Security
  
  All services use consul for service-discovery and jaeger for call tracing, these and all DBs are run via docker container.
  
  
### services and purpose

  *gateway*  
  - Service to handle all incoming http requests
  - Propagation of JWT Token across services
  - Communication with other services
     
  *account*  
  - Service to persist Users in MySql DB with vertx mysql client
  - Encoded Passwords stored in seperate DB
  - Communicate with *cardgame* via client
  - Event Listener when logged in and persist last login time for user
  - Implementation of micronaut security
  - Micronaut-Test based on Spock written in Groovy

  *cardgame*
  - Simple cardgame- higher card wins
  - Redis (with Lettuce redis client) to save stages of game in memory while played and from which it is persisted when finished
  - NoSql MongoDB to persist the finished game as cardgame and the actual stage of the game as decks to retrace every step of game if necessary
  - Communicates with *account* via client
  
  *data*
  - Database migration via flyway
  - Postgres DBs
  - micronaut-data JDBC:
    - Game entity is saved in DB
    - DTO, dynamic finders, etc, but not ORM
    
  
  #### TODOS: ####
  
  - Implement ORM persistence with JPA and Hibernate
  - Micronaut cache for account-db queries
  - Refactor gradle scripts for proper multi-module build
  - Implement proper call tracing
