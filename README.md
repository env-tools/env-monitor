# env-monitor
Generic web-based monitoring app for distributed multi-process systems

#Project participants:

- Yury Yakovlev (yyakovlev)

*Java developer, interested in learning JS, Angular JS, and improving experience with persistence layer technologies for Java.*

- Ilya Sungurov (bombinmybag)

*Javascript developer, interested in learning Java 8, Hibernate, and getting practical experience with Typescript*

- Alexander Dodonov

*Java developer, I am interested in the modern architectural solutions and in the practical usage of Java 8*

- Marsel Maximov (marselmax)

*Java developer, interested in participating at development of new project from scratch*

#How to run:
To run the prototype:
- Build with maven 3
- Take jar from env-monitor-ui/target
- Run with java -jar &lt;jarfile&gt;. Use -Dserver.port=&lt;other_port&gt; if you don't like default 8080
- Access localhost:8080

To debug:
 - cd env-monitor-ui
 - mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
 Or just run Application::main from your IDE in debug mode
