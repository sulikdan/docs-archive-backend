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
  * intended for single users/small group users as DMS for storage & searching [ see multiple tools ] - but many supported at PRO-level - payed one
  * easy to set up
  * very well documented
  * ?? supports bar scanning
  * mirror saving for secuiry/saving to cloud[gdrive] if required
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
    * fullteach search
    * tag search
  * deleting documents from app
  * exporting documents?
* changing working OCR mode
  * input language
  * proccessing document reliability
    * faster & more unreliable
    * slower & more accurate


### Non-Functional
* Reliability - stored data should be backed
* Scalability - possible scalling in high user requests
* Basic security - access to data should be somehow secured through frontend/backend
* Integration - software should be able to work with other applications through API, if they don't want ot use FE approach
* add more

### User Interface
