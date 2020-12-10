# DocsArchive-backend
DocsArchive-backend is a module/component of a bigger project.
It's representing backend part.
The whole project consists of:
* [FrontEnd](https://github.com/EnjoyB/docs-archive-frontend) - angular - UI 
* [BackEnd](https://github.com/EnjoyB/docs-archive-backend) - Java - Main logic
* [OcrApi](https://github.com/EnjoyB/docs-archive-ocr-api) - Java - Wrapped OCR into HTTP API server (You are welcome to use
this component to you advantage as you see it.)
* Database - MongoDB

## About
DocsArchive is an Electronic Document Management System (EDMS) for
a small group of people. The current version is a working prototype. The idea came to be as I was getting myself
crazy at times I was looking for a paper document. Often, I was wasting my
time to find the necessary document and thought about idea, why there is not
tool for that. Well, there are tools for that, but none of them
was written in Java, so I could customize them to myself...

## Requirements
To start current project, it is needed to have:
* docker
* docker-compose

## Docker
All mentioned components are available at DockerHub:
* [FrontEnd](https://hub.docker.com/repository/docker/madgyver/docs-archive-frontend)
* [BackEnd](https://hub.docker.com/repository/docker/madgyver/docs-archive-backend-api) 
* [OcrApi](https://hub.docker.com/repository/docker/madgyver/docs-archive-ocr-api)


## Start-up application
Before proceeding to following steps, it's necessary to download or copy **docker-compose.yml**, located in the [folder](https://github.com/EnjoyB/docs-archive-backend/tree/master/docker_commands) **docker_commands**.
In the mentioned file are defined all necessary settings for starting up the app.

Executing the following command, will be all necessary container images downloaded:
```
docker-compose pull
```

App can be start with following command:

```
docker-compose up
```
Using option
```
-d
```
with previous command, will be the start-up application run in background.

