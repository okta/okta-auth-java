[<img src="https://aws1.discourse-cdn.com/standard14/uploads/oktadev/original/1X/0c6402653dfb70edc661d4976a43a46f33e5e919.png" align="right" width="256px"/>](https://devforum.okta.com/)
[![Maven Central](https://img.shields.io/maven-central/v/com.okta.authn.sdk/okta-authn-sdk-api.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.okta.authn.sdk%22%20a%3A%22okta-authn-sdk-api%22)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Support](https://img.shields.io/badge/support-Developer%20Forum-blue.svg)][devforum]
[![API Reference](https://img.shields.io/badge/docs-reference-lightgrey.svg)][javadocs]
[![Build Status](https://travis-ci.com/okta/okta-auth-java.svg?branch=master)](https://travis-ci.com/okta/okta-auth-java)

# Okta Java Authentication SDK

* [Release status](#release-status)
* [Need help?](#need-help)
* [Getting started](#getting-started)
* [Usage guide](#usage-guide)
* [Configuration reference](#configuration-reference)
* [Building the SDK](#building-the-sdk)
* [Contributing](#contributing)
 
The Okta Authentication SDK is a convenience wrapper around [Okta's Authentication API](https://developer.okta.com/docs/api/resources/authn.html).

## Is This Library Right for Me?

This SDK is a convenient HTTP client wrapper for [Okta's Authentication API](https://developer.okta.com/docs/api/resources/authn/). These APIs are powerful and useful if you need to achieve one of these cases:

- You have an existing application that needs to accept primary credentials (username and password) and do custom logic before communicating with Okta.
- You have significantly custom authentication workflow or UI needs, such that Okta’s hosted sign-in page or [Sign-In Widget](https://github.com/okta/okta-signin-widget) do not give you enough flexibility.

The power of this SDK comes with more responsibility and maintenance: you will have to design your authentication workflow and UIs by hand, respond to all relevant states in Okta’s authentication state machine, and keep up to date with new features and states in Okta.

Otherwise, most applications can use the Okta hosted sign-in page or the Sign-in Widget. For these cases, you should use [Okta's Spring Boot Starter](https://github.com/okta/okta-spring-boot), [Spring Security](https://developer.okta.com/blog/2017/12/18/spring-security-5-oidc) or other OIDC/OAuth 2.0 library.

## Authentication State Machine

Okta's Authentication API is built around a [state machine](https://developer.okta.com/docs/api/resources/authn#transaction-state). In order to use this library you will need to be familiar with the available states. You will need to implement a handler for each state you want to support.  

![State Model Diagram](https://developer.okta.com/img/auth-state-model.png "State Model Diagram")

We also publish these libraries for Java:
 
* [Spring Boot Integration](https://github.com/okta/okta-spring-boot/)
* [Okta JWT Verifier for Java](https://github.com/okta/okta-jwt-verifier-java)
* [Management SDK](https://github.com/okta/okta-sdk-java)
 
You can learn more on the [Okta + Java][lang-landing] page in our documentation.
 
## Release status

This library uses semantic versioning and follows Okta's [library version policy](https://developer.okta.com/code/library-versions/).

| Version | Status                    |
| ------- | ------------------------- |
| 1.x   | :warning: Retired |
| 2.x.x | :heavy_check_mark: Stable ([migration guide](https://github.com/okta/okta-auth-java/blob/master/MIGRATING.md)) |
 
The latest release can always be found on the [releases page][github-releases].
 
## Need help?
 
If you run into problems using the SDK, you can
 
* Ask questions on the [Okta Developer Forums][devforum]
* Post [issues][github-issues] here on GitHub (for code errors)
 
## Getting started
 
To use this SDK you will need to include the following dependencies:

For Apache Maven:

``` xml
<dependency>
    <groupId>com.okta.authn.sdk</groupId>
    <artifactId>okta-authn-sdk-api</artifactId>
    <version>${okta.authn.version}</version>
</dependency>
<dependency>
    <groupId>com.okta.authn.sdk</groupId>
    <artifactId>okta-authn-sdk-impl</artifactId>
    <version>${okta.authn.version}</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>com.okta.sdk</groupId>
    <artifactId>okta-sdk-httpclient</artifactId>
    <version>${okta.sdk.version}</version>    <!-- Latest published version in Maven Central -->
    <scope>runtime</scope>
</dependency>
```

For Gradle:

```groovy
compile 'com.okta.authn.sdk:okta-authn-sdk-api:${okta.authn.version}'
runtime 'com.okta.authn.sdk:okta-authn-sdk-impl:${okta.authn.version}'
runtime 'com.okta.sdk:okta-sdk-httpclient:${okta.sdk.version}'
```

where `${okta.sdk.version}` is the latest published version in Maven Central.

### SNAPSHOT Dependencies

Snapshots are deployed off of the 'master' branch to [OSSRH](https://oss.sonatype.org/) and can be consumed using the following repository configured for Apache Maven or Gradle:

```txt
https://oss.sonatype.org/content/repositories/snapshots/
```

You'll also need:

* An Okta account, called an _organization_ (sign up for a free [developer organization](https://developer.okta.com/signup) if you need one)

 
Construct a client instance by passing it your Okta domain name and API token:
 
[//]: # (NOTE: code snippets in this README are updated automatically via a Maven plugin by running: mvn okta-code-snippet:snip)
 
[//]: # (method: createClient)
```java
AuthenticationClient client = AuthenticationClients.builder()
    .setOrgUrl("https://{yourOktaDomain}")
    .build();
```
[//]: # (end: createClient)
 
Hard-coding the Okta domain works for quick tests, but for real projects you should use a more secure way of storing these values (such as environment variables). This library supports a few different configuration sources, covered in the [configuration reference](#configuration-reference) section.
 
## Usage guide

These examples will help you understand how to use this library. You can also browse the full [API reference documentation][javadocs].

Once you initialize a `AuthenticationClient`, you can call methods to make requests to the Okta Authentication API. To call other Okta APIs, see the [Management SDK](https://github.com/okta/okta-sdk-java).

### Authenticate a User

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

**NOTE:** `UNKNOWN` is not an actual state in Okta's state model. The method handleUnknown is called when an unimplemented or unrecognized state is reached. This could happen if:

- Your handler doesn't have an implementation for the state that was just returned
- Your Okta organization configuration changed, and a new state is now possible (for example, an admin turned on multi-factor authentication)
- Okta added something new to the state model entirely


## Configuration reference
  
This library looks for configuration in the following sources:

0. An `okta.yaml` at the root of the applications classpath
0. An `okta.yaml` file in a `.okta` folder in the current user's home directory (`~/.okta/okta.yaml` or `%userprofile\.okta\okta.yaml`)
0. Environment variables
0. Java System Properties
0. Configuration explicitly passed to the constructor (see the example in [Getting started](#getting-started))
 
Higher numbers win. In other words, configuration passed via the constructor will override configuration found in environment variables, which will override configuration in `okta.yaml` (if any), and so on.
 
### YAML configuration
 
The full YAML configuration looks like:
 
```yaml
okta:
  client:
    connectionTimeout: 30 # seconds
    orgUrl: "https://{yourOktaDomain}" # i.e. https://dev-123456.oktapreview.com
    proxy:
      port: null
      host: null
      username: null
      password: null
    requestTimeout: 10 # seconds
    rateLimit:
      maxRetries: 2
```
 
### Environment variables
 
Each one of the configuration values above can be turned into an environment variable name with the `_` (underscore) character:
 
* `OKTA_CLIENT_CONNECTIONTIMEOUT`
* `OKTA_CLIENT_RATELIMIT_MAXRETRIES`
* and so on

### System properties

Each one of of the configuration values written in 'dot' notation to be used as a Java system property:
* `okta.client.connectionTimeout`
* `okta.client.rateLimt.maxRetries`
* and so on

## Connection Retry / Rate Limiting

By default this SDK will retry requests that are return with a `503`, `504`, `429`, or socket/connection exceptions.  To disable this functionality set the properties `okta.client.requestTimeout` and `okta.client.rateLimit.maxRetries` to `0`.

Setting only one of the values to zero will disable that check. Meaning, by default, four retry attempts will be made. If you set `okta.client.requestTimeout` to `45` seconds and `okta.client.rateLimit.maxRetries` to `0`. This SDK will continue to retry indefinitely for `45` seconds.  If both values are non zero, this SDK will attempt to retry until either of the conditions are met (not both).

## Setting Request Headers, Parameters, and Device Fingerprinting

All of the `AuthenticationClient` requests allow setting additional HTTP headers and query parameters.  This is useful in a variety of situations:

* [Device Finterprinting](https://developer.okta.com/docs/reference/api/authn/#primary-authentication-with-device-fingerprinting)
* Setting the `X-Forwarded-For` header
* Setting additional query paramters that have not been added to the SDK yet

Create a `RequestContext` object, and include it as a method parameter when using the `AuthenticationClient`.

[//]: # (method: headersAndQuery)
```java
List<Header> headers = new ArrayList<>();

// set any header
headers.add(new Header("aHeaderName", "aValue"));

// X-Forwarded-For
headers.add(Header.xForwardedFor("10.10.0.1"));

// X-Device-Fingerprint
headers.add(Header.xDeviceFingerprint("your-finger-print"));
List<QueryParameter> queryParameters = new ArrayList<>();

// set query param
queryParameters.add(new QueryParameter("aQueryParam", "aValue"));
RequestContext requestContext = new RequestContext(headers, queryParameters);
```
[//]: # (end: headersAndQuery)

## Building the SDK
 
In most cases, you won't need to build the SDK from source. If you want to build it yourself, take a look at the [build instructions wiki](https://github.com/okta/okta-auth-java/wiki/Build-It) (though just cloning the repo and running `mvn install` should get you going).
 
## Contributing
 
We're happy to accept contributions and PRs! Please see the [contribution guide](CONTRIBUTING.md) to understand how to structure a contribution.

[devforum]: https://devforum.okta.com/
[javadocs]: https://developer.okta.com/okta-auth-java/
[lang-landing]: https://developer.okta.com/code/java/
[github-issues]: https://github.com/okta/okta-auth-java/issues
[github-releases]: https://github.com/okta/okta-auth-java/releases
