[<img src="https://devforum.okta.com/uploads/oktadev/original/1X/bf54a16b5fda189e4ad2706fb57cbb7a1e5b8deb.png" align="right" width="256px"/>](https://devforum.okta.com/)
[![Maven Central](https://img.shields.io/maven-central/v/com.okta.authn.sdk/okta-authn-sdk-api.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.okta.authn.sdk%22%20a%3A%22okta-authn-sdk-api%22)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Support](https://img.shields.io/badge/support-Developer%20Forum-blue.svg)](https://devforum.okta.com/)

# Okta Authentication SDK

The Okta Authentication SDK is a convenience wrapper around [Okta's Authentication API](https://developer.okta.com/docs/api/resources/authn.html).

**NOTE:** Using an OAuth 2.0 or OIDC to integrate your application instead of this library will require much less work, and has a smaller risk profile. Please see [this guide](https://developer.okta.com/use_cases/authentication/) to see if using this API is right for your use case. You could also use our [Spring Boot Integration](https://github.com/okta/okta-spring-boot), or [Spring Security](https://developer.okta.com/blog/2017/12/18/spring-security-5-oidc) out of the box.

Okta's Authentication API is built around a [state machine](https://developer.okta.com/docs/api/resources/authn#transaction-state). In order to use this library you will need to be familiar with the available states. You will need to implement a handler for each state you want to support.  

![State Model Diagram](http://developer.okta.com/assets/img/auth-state-model.png "State Model Diagram")

# Usage

<!--
## Javadocs

You can see this project's Javadocs at https://developer.okta.com/okta-authn-sdk-java/ 
-->

## Dependencies

The only compile time dependency you will need is `okta-authn-sdk-api`.  You will also need to add the implementation dependencies too: `okta-authn-sdk-impl` and `okta-sdk-httpclient`.

``` xml
<dependency>
    <groupId>com.okta.sdk</groupId>
    <artifactId>okta-authn-sdk-api</artifactId>
    <version>${okta.authn.version}</version>
</dependency>
<dependency>
    <groupId>com.okta.sdk</groupId>
    <artifactId>okta-authn-sdk-impl</artifactId>
    <version>${okta.authn.version}</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>com.okta.sdk</groupId>
    <artifactId>okta-sdk-httpclient</artifactId>
    <version>${okta.sdk.version}</version>
    <scope>runtime</scope>
</dependency>
```

## SNAPSHOT Dependencies

Snapshots are deployed off of the 'master' branch to OSSRH and can be consumed using the following repository configured for Apache Maven or Gradle:
```
https://oss.sonatype.org/content/repositories/snapshots/
```

## Client configuration

There are a few ways to configure the client, but the easiest way is to create a `~/.okta/okta.yaml` file and `orgUrl` value:

``` yaml
okta:
  client:
    orgUrl: https://dev-123456.oktapreview.com
```

## Creating a Client

 Once you create your `okta.yaml` file, you can create a client with a couple of lines:

``` java
// Instantiate a builder for your client. If needed, settings like Proxy and connection timeouts can be defined here.
AuthenticationClientBuilder builder = AuthenticationClients.builder();

// No need to define anything else; build the AuthenticationClient instance.
AuthenticationClient client = builder.build();
```

For more details see: [Creating a Client](https://github.com/okta/okta-auth-java/wiki/Creating-a-Client)

## Authentication

An authentication flow usually starts with a call to `authenticate`:

```java
// could be where to redirect when authentication is done, a token, or null
String relayState = "/application/specific";
client.authenticate(username, password, relayState, stateHandler);
```

Everything looks pretty standard except for `stateHandler`.  The [`AuthenticationStateHandler`](https://github.com/okta/okta-auth-java/blob/master/api/src/main/java/com/okta/authn/sdk/AuthenticationStateHandler.java) is a mechanism to fire an event for the given authentication state returned.  Basically, it prevents you from needing to use something like a switch statement to check state of the `AuthenticationResponse`.

A typical `AuthenticationStateHandler` may look something like:

```java
public class ExampleAuthenticationStateHandler extends AuthenticationStateHandlerAdapter {

    @Override
    public void handleUnknown(AuthenticationResponse unknownResponse) {
        // redirect to "/error"
    }

    @Override
    public void handleSuccess(AuthenticationResponse successResponse) {
        
        // a user is ONLY considered authenticated if a sessionToken exists
        if (Strings.hasLength(successResponse.getSessionToken())) {
            String relayState = successResponse.getRelayState();
            String dest = relayState != null ? relayState : "/";
            // redirect to dest    
        }
        // other state transition successful 
    }

    @Override
    public void handlePasswordExpired(AuthenticationResponse passwordExpired) {
        // redirect to "/login/change-password"
    }
    
    // Other implemented states here
}
```

As noted in the above example, a user is ONLY considered authenticated if `AuthenticationResponse.getSessionToken()` is not null. This `sessionToken` can be exchanged via the [Okta Sessions API](https://developer.okta.com/docs/api/resources/authn.html#session-token) to start an SSO session, but that is beyond the scope of this library.

**NOTE:** `UNKNOWN` is not an actual state in Okta's Authentication state model. The method `handleUnknown` is called when an unimplemented state is reached (this could happen if someone turned on MFA support for your Okta organization but was not previously implemented in your state handler). It also possible Okta has added a new state to the state model, and someone from your organization enabled this new state.  

## Contribute to the Project

Take a look at the (contribution guide)[CONTRIBUTING.md] and the [build instructions wiki](https://github.com/okta/okta-auth-java/wiki/Build-It) (though just cloning the repo and running `mvn install` should get you going).
