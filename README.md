# env-monitor
Generic web-based monitoring app for distributed multi-process systems

#Project participants:

- Yury Yakovlev (env-tools and yyakovlev committers)

*Java developer, interested in learning JS, Angular JS, and improving experience with persistence layer technologies for Java.*

- Ilya Sungurov (bombinmybag)

*Javascript developer, interested in learning Java 8, Hibernate, and getting practical experience with Typescript*

- Alexander Dodonov

*Java developer, I am interested in the modern architectural solutions and in the practical usage of Java 8*

- Marsel Maximov (marselmax)

*Java developer, interested in participating at development of new project from scratch*

#How to run:
*Build web UI:*
- Install npm (is part of node js)
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
- Download npm dependencies
```sh
npm install
```
- Download bower dependencies
```sh
bower install
```
- Build project
```sh
gulp dist
```

*To run the prototype standalone (from command line):*
- Build with maven 3
- Take jar from env-monitor-ui/target
- Run with java -jar &lt;jarfile&gt;. 
- In command line, use -Dserver.port=&lt;other_port&gt; if you don't like default 8080

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
