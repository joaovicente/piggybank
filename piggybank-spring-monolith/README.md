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
# REST API 

## Create Kid

```
$http POST http://localhost:8080/kids name=Albert
{
    "id": "ccf15063-a2b6-465a-b7b4-707a03791da9",
    "name": "Albert"
}
```

## Create Transaction
```
$ http POST http://localhost:8080/transactions kidId=ccf15063-a2b6-465a-b7b4-707a03791da9 kind=CREDIT amount=10000 date=2018-11-22 description="first transaction" 
{
    "amount": 10000,
    "date": "2018-11-22",
    "description": "first transaction",
    "id": "10058309-961a-4998-b4ef-a0674784e477",
    "kidId": "ccf15063-a2b6-465a-b7b4-707a03791da9",
    "kind": "CREDIT"
}
```

## Get kid balance

```
$ http http://localhost:8080/balance kidId==ccf15063-a2b6-465a-b7b4-707a03791da9
{
    "balance": 10000
}
```

## Get kids-and-balances

```
$ http https://localhost:8080/kids-and-balances
[
    {
        "kidBalance": 10000,
        "kidId": "ccf15063-a2b6-465a-b7b4-707a03791da9",
        "kidName": "Albert"
    }
]
```
