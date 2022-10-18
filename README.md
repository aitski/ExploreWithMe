# java-explore-with-me
ExploreWithMe project

Application Explore With Me helps to share information about events, request participation and comment.

Tools applied: Spring Boot, Hibernate, PostgreSQL, Docker compose

It consists of 2 micro-services:
1) Server - includes all necessary logic for the application
2) Statistics - stores number of views and allows to get this information

Server diagram

![Diagram](/server.png)

Statistics diagram

![Diagram](/statistics.png)

Server has 3 levels of privacy:
1) Public - view events and comments
2) Private - create events, request participation, comment events
3) Admin - create categories, approve/reject events, delete comments

Links to complete information about API (open in editor.swagger.io):

[service](/ewm-main-service-spec.json)

[statistics](/ewm-stats-service-spec.json)
