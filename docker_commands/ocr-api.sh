#!/bin/bash

# get latest
docker pull madgyver/docs-archive-ocr-api:latest

# Mixed
#docker run --name ocr-api -p 8086:8086 --network ocr-net -d madgyver/docs-archive-ocr-api --tesseract.path=/usr/share/tessdata/
#docker run --name ocr-api -p 8086:8086 --network ocr-net -d madgyver/docs-archive-ocr-api --tesseract.path=/usr/share/tessdata/

#docker create --name ocr-api -p 8086:8086 madgyver/docs-archive-ocr-api --tesseract.path=/usr/share/tessdata/
#docker network connect ocr-net ocr-api
#docker network connect db-mongo-net ocr-api
#docker network connect bridge ocr-api
#docker network connect host ocr-api
#docker start ocr-api
#
#docker run -d --name ocr-api-2 -p 8086:8086 madgyver/docs-archive-ocr-api --tesseract.path=/usr/share/tessdata/ &


# Try 1
#docker run --name ocr-api -p 8086:8086 --network ocr-net -d madgyver/docs-archive-ocr-api --tesseract.path=/usr/share/tessdata/


# Current
#docker run --name ocr-api -p 8086:8086 --network backend-comm -d madgyver/docs-archive-ocr-api --tesseract.path=/usr/share/tessdata/

# New
#docker run --name ocr-api -p 8080:8086 -d madgyver/docs-archive-ocr-api --tesseract.path=/usr/share/tessdata/ # this is NOT working from host
#docker run --name ocr-api -p 8086:8086 -d madgyver/docs-archive-ocr-api --tesseract.path=/usr/share/tessdata/ # this is NOT working from host
#docker run --name ocr-api -p 8086:8086 --network host -d madgyver/docs-archive-ocr-api --tesseract.path=/usr/share/tessdata/
#docker run --name ocr-api --network host -d madgyver/docs-archive-ocr-api --tesseract.path=/usr/share/tessdata/ #This is working from host

 docker inspect --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' ocr-api
