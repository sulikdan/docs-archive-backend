# docs-archive-backend
Docs-archive is module of a bigger project.
It's representing backend part.
The whole project consists of:
* FrontEnd - angular - https://github.com/EnjoyB/docs-archive-frontend
* BackEnd - Java - https://github.com/EnjoyB/docs-archive-backend
* OcrApi - Java - https://github.com/EnjoyB/docs-archive-ocr-api
* Database - MongoDB

## Requirements
To start current project, it is needed to have:
* unix-OS(recommended)
* docker
* docker-compose

## Start-up application
Before proceeding to following steps, it's necessary to download or copy *docker-compose.yml*, located in the folder *docker_commands*.
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

