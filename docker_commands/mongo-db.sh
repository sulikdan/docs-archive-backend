#!/bin/bash

# Mixed
#docker run --name mongo-db -p 27017:27017 --network mongo-db -v mongodbdata:/data/db -d mongo:4.4.1
#docker create --name mongo-db -p 27017:27017 -v mongodbdata:/data/db mongo:4.4.1
#docker network connect db-mongo-net mongo-db
#docker network connect bridge mongo-db
#docker start mongo-db


# Try 1
#docker run --name mongo-db -p 27017:27017 --network mongo-db -v mongodbdata:/data/db -d mongo:4.4.1

# Current
docker run --name mongo-db -p 27017:27017 --network backend-comm -v mongodbdata:/data/db -d mongo:4.4.1