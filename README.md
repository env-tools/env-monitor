# env-monitor
[![Build Status](https://travis-ci.org/env-tools/env-monitor.svg?branch=master)](https://travis-ci.org/env-tools/env-monitor)

Generic web-based monitoring app for distributed multi-process systems

#Project participants:

- Yury Yakovlev (env-tools and yyakovlev committers)

*Java developer, interested in learning JS, Angular JS, and improving experience with persistence layer technologies for Java.*

- Michal Skuza

*Java and AngularJS developer, Luxoft Poland*

- Ilya Sungurov (bombinmybag)

*Javascript developer, interested in learning Java 8, Hibernate, and getting practical experience with Typescript*

- Anastasiya Plotnikova (anastasiya14)

*Student of LETI University*

- Sergey Moldachev (InnerFlameFact)

*Student of LETI University*

- Maksim Fyodorov (jesa29)

*Student of LETI University*

#How to run:
*Build web UI:*
- Install npm (is part of node js)
- If you're behind proxy, set up proxy parameters:
```sh
set HTTP_PROXY=http://DOMAIN%5Cusername:password@server.com:PORT
```
- Install global bower
```sh
npm i bower -g
```
- Install global gulp (windows only)
```sh
npm i gulp@3.9.0 -g
```
- Go to UI module
```sh
cd env-monitor/env-monitor/env-monitor-ui/src/main/resources/static/
```
- Download npm dependencies (required first time)
```sh
npm install
```
- Download bower dependencies (required if new js libraries appeared in project)
```sh
bower install
```
- Build project (required if js sources changed)
```sh
gulp dist
```

*To run the prototype standalone (from command line, using java):*
- Build with maven 3
- Take jar from env-monitor-ui/target
- Run with java -jar &lt;jarfile&gt;
- In command line, use -Dserver.port=&lt;other_port&gt; if you don't like default 8080

*To run the prototype standalone (from command line, using maven):*
- Build with maven 3
- cd env-monitor-ui
- Run with mvn spring-boot:run

*To run the prototype from your IDE*
- Import root maven pom.xml into your project
- Find class "Application" and method "main", run it from IDE 
- In your run configuration (Java startup settings) use -Dserver.port=&lt;other_port&gt; if you don't like default 8080

*Final step:*
- Access http://localhost:8080

#How to debug:
*To debug the prototype remotely as a standalone app:*
 - cd env-monitor-ui
 - mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
 - Launch remote debugging session in your IDE
 
*To debug from your IDE*
 - Run Application::main in debug mode

#Developers guide

Client/server interaction is built on asynchronous message exchange over Websockets (STOMP protocol). Message format is JSON.
The application is built from modules: 1 Core Module and several pluggable modules
 - Core module which is tightly coupled with Spring controllers and always lives together with them 
 - M_APPLICATIONS module
 - M_QUERY_LIBRARY module
 - More modules will be added during development
 
Modules are currently Spring beans which communicate through Spring Integration channels in the same Spring integration context. 
However they could communicate through a lightweight messaging broker and live in standalone processes (if this is ever implemented)

UI requests are routed to Core module which in turn:
 - either provides response immediately (because has all necessary data) 
 - or delegates processing to the responsible module

Currently, the following types of interactions/flows are supported:

1. Pluggable module sends some data model to Core module (which in turn caches it in memory). This happens on module startup, and also later when it's decided that data has been updated and the update should be sent. Full data is re-sent to be cached. When Core module receives the updated model, it broadcasts the updates for interested client subscribers.
 
When used:
 -Applications data is sent by M_APPLICATIONS module : 

ApplicationsModule::onModelUpdate()

 -Query tree is sent by M_QUERY_LIBRARY module

QueryLibraryModule:
treeUpdateTriggerService.triggerUpdate();

2. // TO BE DONE

