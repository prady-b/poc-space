## Spring Boot Reactive 
 * Implementation CRUD APIs for User Service 
    * Router Function and Handler 
    * Reactive Mongo
    ```sh
    ./gradlew bootRun
    ```
    
Method	| Path	| Description | JSON
------------- | ------------------------- | ------------- | --------
GET	| /users	| Get all User data		
GET	| /users/{userid}	| Get specific user data
POST	| /users	| Create new User | `{`<br>`"userLoginName":"PradyB",`<br>`"password":"xxx",`<br>`"firstName":"Prady",`<br>`"lastName":"B"`<br>`}`
PUT	| /users/{userid}	| Update existing User
DELETE	| /users/{userid}	| Delete existing User

## 