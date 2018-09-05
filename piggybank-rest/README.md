## Create project

```
$ spring init \
    -d=web,lombok,data-mongodb \
    -groupId=io.github.joaovicente \
    -artifactId=piggybank \
    -name=piggybank \
    piggybank
```

## Create `DepositController.java`

```
$ vim ./src/main/java/io/github/joaovicente/piggybank/DepositController.java
```


```
package io.github.joaovicente.piggybank;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class DepositController {
    @RequestMapping(value = "/deposit", method = RequestMethod.POST)

    public CreateDepositRequestDto createDeposit(@RequestBody CreateDepositRequestDto createDepositRequestDto) {
        return createDepositRequestDto;
    }
}
```

## Create `CreateDepositRequestDTO`

```
$ vim ./src/main/java/io/github/joaovicente/piggybank/CreateDepositRequestDto.java
```

```
package io.github.joaovicente.piggybank;

import lombok.Data;

@Data
public class CreateDepositRequestDto {
    private String description;
    private float amount;
}
```


Run the app

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



