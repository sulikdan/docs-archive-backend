# Document Management System
- shortcut DMS
- usually for big companies
- paid
- some support OCR
- shared documents between devices, through internet conenction

## DMS existing systems

|                          | Templafy | M-Files | Docuware | MasterControl Document Control | Xait |
|:------------------------:|:--------:|:---------:|:--------:|:------------------------------:|:----:|
|         DMS-name          |          |         |          |                                      ||
|           Paid           |          |         |          |                                      ||
| OCR-Variety of languages |          |         |          |                                      ||
|         DMSname          |          |         |          |                                      ||
|         Mobile aaps          |          |         |          |                                      ||
|         Cloud based          |          |         |          |                                      ||
|         Document range          |          |         |          |                                      ||
|         Integration          |          |         |          |                                      ||

- another <https://www.pcmag.com/picks/the-best-document-management-software>
  - Box (for Business)
  - Microsoft OneDrive for Bussiness
  - Ademero Content Central
  - Fluix
  - PaperTracer
- OpenKM - <https://www.openkm.com/>
  - oriented more for corporation use than single user use - makes logic
- eFileCabinet - <https://www.efilecabinet.com/ocr-and-document-management-system/>
- scanService - <https://www.scanservice.cz/en/sluzba/complete-ecm-ocr-dms-solutions/>
- mayan - <https://www.mayan-edms.com/>

##  OCR posibilities <https://www.slant.co/topics/2579/~best-ocr-libraries>
- Tesseract
  - <https://stackoverflow.com/questions/13974645/using-tesseract-from-java?answertab=votes#tab-top>
  - <https://github.com/bytedeco/javacpp-presets/tree/master/tesseract>
  - <https://docparser.com/blog/improve-ocr-accuracy/>
  - free
  - oldest, first HP, latter Google
  - need to use maven repository, which wraps it ... forerunner Tess4j
  - using original complicated like a hell
  - **Preprocessing very important** - in general
- gocr
- Copyfish
- OCR.Space
- ABBYY FineReader -- better
- Onyx {https://www.quora.com/What-are-the-best-open-source-OCR-libraries}
- Tess-two
- ABBYY Cloud OCR API.
- Asprise
- Cvisiontech OCR SDK Free

## Database use
- relational
- graph
- no-relational

## Possible extensions
* using GDrive
* ask google to use email as place where can be installed server that will occasionaly do calculations
* search in stored text images
* Users and Admin + rights roles
* Statistic page - storage used, how often, etc.
* aimed for small group/ single person solution instead of corporation
* open-sourced
* free to use app, but no support
* use cloud for bigger use? Or just to save money?
* scan with phone?
* how to solve access?
  * buying server, where it is deployed
  * or does it make sense to have server deployed on your computer, but use IPv6 to get access?
  * theoretically, if it's private network IPv4 is enough
  * support in more than 1 language

## Questions to Kuchar
* Ma to realne buducnost?
* Nevadi, ze existuju uz nejake riesenia, podobne mojemu?
* Podmienky pre uspesnost Diplomky:
  * Musi to byt originalne?
  * cielene na singleUser
  * userFriendly?
  * vyvoj, pre znameho?
  * 


# Reasons why my own
Here are:
  * latest Spring etc. libraries
  * intended for single users/small group users as DMS for storage & searching ( see multiple tools ) - but many supported at PRO-level - payed one
  * easy to set up
  * very well documented
  * ?? supports bar scanning
  * mirror saving for secuiry/saving to cloud(gdrive) if required
  * totally free
  * search in scanned text?
  * Supports Slovak version of APP
  * Supports multiple languages in OCR in compare with OpenKM
  * light-weight?
  * Missing test cases?

## Existing choices
### OpenMK

Positives:
* Community edition
  * free
  * open sourced - only the CE
* Use java
* App supports 35+- languages
* Many already implemented tools
* Support multiple relationalDBs - h2, hsqldb, mariadb, mysql, oracle, postgresql, sqlserver


Negatives:
* Awful documentation
* Very vague information about software
<!--* JSP - ??find good reason why not??-->
* Intended more for groups or corporation
* Only few tests
* OCR supports only 5 languages, even though it is using tesseract OCR
* Missing slovak app language
* CE - not supporting BarCodes reading
* using Relational DB - ???cannot read in fulltext scanned documments//slow?? - better MongoDB
* Though they have installer for CE, it's not working very well.. - not very friendly
* hardly specified what is supportedin CE edition, moreover it's written that it uses basic version ...
* not specified what file types are supported
* 

### Mayan

Positives:
* Better documentation for users
* Much more friendlier information about software
* Docker
* OpenSourced
* Free
* Paid only for servis
Negatives:
* app language not finished for Czech language and not even started for Slovak
* Intended more for groups or corporation
* PostgreSQL ?
* using docker, but the OS has to support it, in case using win 10 home edition or older editions, it's not supported
* different language i.e. python in contrast of Java, so **not possible to customize app**

### Teedy
* <https://teedy.io/#!/plans>
* lightweight
* opensource
* paid

### Paperless
* <https://github.com/the-paperless-project/paperless>
* opensource
* similar to Mayan
* also python written

##  Futher work - 17.6.2020
* use rather docker for my app, because it will make my time easier trying to deploy it/install for user.
* maybe separate documentation app & OCR tesseract
  * there is already tesseract docker image, though not sure what it supports - Clear OS tesseract docker image
  * Here is example how to create OCR docker image app - <https://medium.com/gft-engineering/creating-an-ocr-microservice-using-tesseract-pdfbox-and-docker-155beb7f2623>
  * Best choice would be installing Document App container to call/create another container with corresponding tesseract language
* work further Analysis, requirements, etc ...
  * looks through older works, find out how to correctly write it ...


## Software Requirements

### Functional
* user
  * login
  * change password
    * reset password through mail?
    * password hashing?
  * adding additional users, if admin
* documents
  * importing documents
    * immediate OCR scanning and saving
    * lazy OCR scanning and saving
  * searching imported documents
    * pagination
    * fulltext search
    * tag search
  * deleting documents from app
  * exporting documents?
* changing working OCR mode
  * input language
  * processing document reliability
    * faster & more unreliable
    * slower & more accurate


### Non-Functional
* Reliability - it would be supported by usage of docker, cheking input values/data
  * testable
* Scalability - enable possible scalling in high user requests demand
* Basic security - access to data should be somehow secured through frontend/backend
* Integration - software should be able to work with other applications through API, if they don't want ot use FE approach
* Extensibility
* Open-Source
* Free
* Reusability - ? for example OCR on separate docker image
* Documentation
* add more

### User Interface
Maybe later? Something simple:
* Login
* Search Documents component
* User component
* edit document
* view document

#### What to mentiones
* Requirements
* Docker image
  * different images with different OCR tesseract languages
* whats next - to start implementing?
  * MondoDB vs normal relational Database


### 29.6.2020 - TODO
* check elastic search? vs mongoDB search ... better DB for searching in text with extended posibilities
* implement and prepare OCR docker image
  * how it works with setting different analysing modes
  * how it works with using different languages for input
* template/intro for my BackendApp

### 3.7.2020
* JavaCPP Presets for Tesseract vs Tess4j
* https://www.youtube.com/watch?v=YFl2mCHdv24

### 17.7.2020
* MongoDB loosing data on insert???
* docker image problem??
* reactive programming
* webflux react vs default servlet API
    * cons
    * pro
    * my decision ...

### 24.7.2020
* Maybe change MongoDB
    * id generation ObjectId
    * ACID troubles?
* Limit input file to avoid craziness
* API only async - sync is pointless ..
* Document OCR - Automatic or immeadiatly, but queued with automatic
* Trouble with queueing, multi-threaded approach may be not thread safe
* Very "simple" approach avoiding big technologies
* DB repo built on CrudRepo - easy to switch to different DB type, if supported

### 24.7.2020
* Careful JPA vs CrudRepo vs Hibernate
    * JPA not working well with MongoDB another reason to use different DB
* reason why this:
    * my company father has a lot of documents
    * I as "abroad" student have to take care of different documents
    * Neccessity to store documents required by law
    * In usefull for user to have them stored