# Okta Java AuthN SDK - Springboot Sample

## Introduction

This Sample web application will demonstrate below common flows:
1. Login with username/password
2. Password Recovery 
3. Sign In with Multifactor Authentication using Email

## Prerequisites

- [JDK 17][jdk-17] or later
- [Apache Maven][apache-maven] 3.6.x or later

You will also need:

An Okta account, called an Organization (sign up for a free developer organization if you need one) and ensure it is NOT 
Okta Identity Engine enabled (should be a legacy v1 org).

## Installation & Running The App

1. Set the below env variables:

```
export OKTA_CLIENT_ORGURL=https://{yourOktaDomain}
export OKTA_CLIENT_TOKEN={yourApiToken}
```

2. Navigate to `examples/example-webapp` and run `mvn spring-boot:run`

Visit http://localhost:8080 in your browser.

If you see an index page, then your app is working!

[jdk-8]: https://www.oracle.com/java/technologies/downloads/#java17
[apache-maven]: https://maven.apache.org/download.cgi
