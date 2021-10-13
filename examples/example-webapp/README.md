# Okta Java AuthN SDK - Springboot Sample

## Introduction

This Sample web application will demonstrate below common flows:
1. Login with username/password
2. Password Recovery 
3. Sign In with Multifactor Authentication using Email

## Prerequisites

- [JDK 8][jdk-8] or later
- [Apache Maven][apache-maven] 3.6.x or later

## Installation & Running The App

1. set the below env variables:

```
export OKTA_CLIENT_ORGURL=https://{yourOktaDomain}
export OKTA_CLIENT_TOKEN={yourApiToken}
```

2. Navigate to folder `examples/example-webapp` and run `mvn spring-boot:run`

Now navigate to http://localhost:8080 in your browser.

If you see an index page, then things are working!

[jdk-8]: https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
[apache-maven]: https://maven.apache.org/download.cgi
