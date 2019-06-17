# PoC Space to showcase all my Sample/Demo applications

[![CircleCI](https://circleci.com/gh/pradyb/poc-space/tree/master.svg?style=svg)](https://circleci.com/gh/pradyb/poc-space/tree/master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4b8bdfb38f2b4c3087c2076429e09885)](https://www.codacy.com/app/pradyb/poc-space?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pradyb/poc-space&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/4b8bdfb38f2b4c3087c2076429e09885)](https://www.codacy.com/app/pradyb/poc-space?utm_source=github.com&utm_medium=referral&utm_content=pradyb/poc-space&utm_campaign=Badge_Coverage)


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