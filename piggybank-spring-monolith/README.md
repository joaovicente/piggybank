# Deploy to Heroku

```mvn heroku:deploy```



## Run the app locally

*run Mongodb docker in a new terminal*

```
$ sudo docker-compose -f ./docker-compose-mongodb.yml
```

> When you want to stop the containers define in the compose file `Ctrl+C` is not enough. To bring them down fully, you will need to run the inverse down command: `docker-compose -f ../docker-compose-mongodb.yml down`


```
$ mvn spring-boot:run
```

## Make the first deposit

```
$ http POST localhost:8080/deposit description="first deposit" amount=10

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Date: Wed, 05 Sep 2018 06:11:04 GMT
Transfer-Encoding: chunked

{
    "amount": 10.0,
    "description": "first deposit"
}
```


## Persist deposit using MongoDB


### Setup MongoDB docker

```
vim docker-compose-mongodb.yml
```

```
---
version: '2'
services:
  mongodb:
    container_name: mongodb
    image: mongo:3.0.4
    ports:
      - "27017:27017"
    command: mongod --smallfiles
```


## Create a repository to store Transactions (Deposits and Withdrawalss)

```
$ vim ./src/main/java/io/github/joaovicente/piggybank/Transaction.java
```

