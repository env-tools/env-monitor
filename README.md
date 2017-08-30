# env-monitor
[![Build Status](https://travis-ci.org/env-tools/env-monitor.svg?branch=master)](https://travis-ci.org/env-tools/env-monitor)

Generic web-based monitoring app for distributed multi-process systems

## Project participants:

- Yury Yakovlev (env-tools and yyakovlev committers)

*Java developer, interested in learning JS, Angular JS, and improving experience with persistence layer technologies for Java.*

- Michal Skuza

*Java and AngularJS developer, Luxoft Poland*

- Evgeniy Semenov

*Java developer, Luxoft Poland*

- Sergey Moldachev

*Java and Javascript developer, Luxoft*

- Igor Avdeev

*Java and Javascript developer, Luxoft*

## How to run:
*Build web UI:*
- Install npm (is part of node js)
- If you're behind proxy, set up proxy parameters:
```sh
set HTTP_PROXY=http://DOMAIN%5Cusername:password@server.com:PORT
```
- Install global gulp (windows only)
```sh
npm i gulp@3.9.0 -g
```
- Go to UI module
```sh
cd env-monitor/env-monitor/env-monitor-ui/src/main/resources/static/
```
- Download npm dependencies: tool dependencies and javascript library dependencies
```sh
npm install
```
Please note: bower is no more used

- Build project (required if js sources changed or index-template.html changed)
```sh
gulp dist
```

-If you have customized some placeholder values used in index-template.html, just put them in separate file like 
```sh
settings-myprofile.properties 
```
in directory 
```sh
\env-monitor\env-monitor-ui\src\main\resources
```
and use that file during gulp build with
```sh
gulp dist --profile myprofile
```

By default, file
```sh
\env-monitor\env-monitor-ui\src\main\resources\settings-default.properties 
```
will be used.

- If you're doing UI development, start tracking changes in js/css files 
(so that scripts.js and styles.css are re-generated automatically)
```sh
gulp watch
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
- Find class "ApplicationStartup" and method "main", run it from IDE 
- In your run configuration (Java startup settings) use -Dserver.port=&lt;other_port&gt; if you don't like default 8080
- If you're doing UI development, make sure your current directory is where 'env-monitor-ui' folder is located.
This will allow for instant web content re-loading when you refresh Browser page (don't forget 'gulp watch' also).

*Final step:*
- Access http://localhost:8080

## How to debug:
*To debug the prototype remotely as a standalone app:*
 - cd env-monitor-ui
 - mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
 - Launch remote debugging session in your IDE
 
*To debug from your IDE*
 - Run ApplicationStartup::main in debug mode


## Caveats

*There are settings for testing with real SSH service (xshellz) available in the internet. As it expires every 2 weeks, you may find that it's not available.

*If SSH is blocked at your PC or there are other SSH connection issues, modify configuration:
 - set applications.provider=org.envtools.monitor.provider.applications.mock.MockApplicationsModuleProvider in application.properties
 - set load-at-startup to false in shell.xShellz.com.xml
 
## Developers guide

### Introduction
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

### Data flow types
Currently, the following types of interactions/flows are supported.

#### Push from pluggable module ("Server-Side push")
Pluggable module sends some data model to Core module (which in turn caches it in memory). This happens on module startup, and also later when it's decided that data has been updated and the update should be sent. Full data is re-sent to be cached. When Core module receives the updated model, it broadcasts the updates for interested client subscribers.
 
When used:
 -Applications data is sent by M_APPLICATIONS module : 

ApplicationsModule::onModelUpdate()

 -Query tree is sent by M_QUERY_LIBRARY module

QueryLibraryModule:
treeUpdateTriggerService.triggerUpdate();

#### UI-initiated data request to pluggable module ("Message")
//TODO
#### UI-initiated data request to core module ("Call")
//TODO
#### UI-initiated data request to core module with subscription to further updates ("Subscription")
//TODO


