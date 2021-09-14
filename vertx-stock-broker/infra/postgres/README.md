# Dockerized Postgres

https://hub.docker.com/_/postgres

## Ephemeral Postgres instances
This is the quickest way to get started:
```
docker run --name my-postgres -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=vertx-stock-broker -p 5432:5432 -d postgres:13.1-alpine
```

* User: postgres
* Password: secret
* Database: vertx-stock-broker

## Docker Compose
Execute from root directory:
```
docker-compose -f ./infra/postgres/postgres.yml up
```

## Docker Swarm
Execute from root directory:
```
docker stack deploy -c ./infra/postgres/postgres.yml postgres
```

Contains a volume for permanent storage of data. On system restart the data is available again.
