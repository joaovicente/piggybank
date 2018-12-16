#!/usr/bin/env bash
http localhost:8080/kids name=Albert
http localhost:8080/transactions kidId=$(http localhost:8080/kids | jq '.[0].id' | sed 's/"//g') date=2018-01-01 kind=CREDIT amount=100 description="Initial credit"
http localhost:8080/transactions kidId=$(http localhost:8080/kids | jq '.[0].id' | sed 's/"//g') date=2018-01-02 kind=DEBIT amount=50 description="Shopping day"

http localhost:8080/kids name=Beth
http localhost:8080/transactions kidId=$(http localhost:8080/kids | jq '.[1].id' | sed 's/"//g') date=2018-01-01 kind=CREDIT amount=150 description="Gift from uncle"
http localhost:8080/transactions kidId=$(http localhost:8080/kids | jq '.[1].id' | sed 's/"//g') date=2018-01-02 kind=DEBIT amount=50 description="Lunch with friends"

http localhost:8080/kids name=Carol
http localhost:8080/transactions kidId=$(http localhost:8080/kids | jq '.[2].id' | sed 's/"//g') date=2018-01-01 kind=CREDIT amount=80 description="Gift from aunt"
http localhost:8080/transactions kidId=$(http localhost:8080/kids | jq '.[2].id' | sed 's/"//g') date=2018-01-02 kind=DEBIT amount=20 description="Cinema"

http localhost:8080/kids-and-balances