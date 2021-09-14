# Dockerized MySQL

https://hub.docker.com/_/mysql

## Ephemeral MySQL instances
This is the quickest way to get started:
```
docker run --name my-mysql -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_PASSWORD=secret -e MYSQL_DATABASE=vertx-stock-broker -p 3306:3306 -d mysql:8.0
```

* User: root
* Password: secret
* Database: vertx-stock-broker

Note: for easy access we are using the root user.

## Docker Compose
Execute from root directory:
```
docker-compose -f ./infra/mysql/mysql.yml up
```

## Docker Swarm
Execute from root directory:
```
docker stack deploy -c ./infra/mysql/mysql.yml mysql
```

Contains a volume for permanent storage of data. On system restart the data is available again.
