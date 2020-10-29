#!/bin/bash

# get latest
docker pull madgyver/docs-archive-frontend:latest

#docker run --name front-end -p 4200:4200 --network backend-net -d madgyver/docs-archive-frontend
docker run --name front-end -p 4200:80 --network frontend-comm -d madgyver/docs-archive-frontend