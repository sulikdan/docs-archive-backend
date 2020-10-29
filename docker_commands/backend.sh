#!/bin/bash

# get latest
docker pull madgyver/docs-archive-backend-api:latest

# mixed
#docker create --name backend-api -p 8085:8085 --network db-mongo-net madgyver/docs-archive-backend-api --spring.data.mongodb.host=mongo-db --ocr.address=ocr-api
##docker create --name backend-api -p 8085:8085 madgyver/docs-archive-backend-api --spring.data.mongodb.host=mongo-db --ocr.address=ocr-api
##docker network connect db-mongo-net backend-api
#docker network connect ocr-net backend-api
#docker network connect backend-net backend-api
##docker network connect bridge backend-api
#docker start backend-api

# Anothe block
#docker create --name backend-api -p 8085:8085 --network db-mongo-net madgyver/docs-archive-backend-api --spring.data.mongodb.host=mongo-db --ocr.address=ocr-api
#docker network connect ocr-net backend-api
#docker network connect backend-net backend-api
#docker start backend-api


#Current
docker create --name backend-api -p 8085:8085 --network backend-comm madgyver/docs-archive-backend-api --spring.data.mongodb.host=mongo-db --ocr.address=ocr-api
docker network connect frontend-comm backend-api
docker start backend-api