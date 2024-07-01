# Okta Java Authentication SDK Migration Guide
 
This SDK uses semantic versioning and follows Okta's [library version policy](https://developer.okta.com/code/library-versions/). In short, we do not make breaking changes unless the major version changes!

## Migrating from 1.x.x to 2.0.0

Version 2.0.0 of this SDK introduces a number of breaking changes from previous versions. 
In addition to some new classes/interfaces, some existing classes/interfaces are no longer backward compatible due to method renaming and signature changes.

## Migrating from 2.x.x to 3.0.0

Version 3.0.0 of this SDK requires Java 17 as the minimum version.
This version has examples that run on Spring Boot 3.x and this would require a Java 17 minimum version (see [here](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide#review-system-requirements)).

### Package `com.okta.authn.sdk.resource`

- Replaced `com.okta.sdk.resource.user.factor.FactorProfile` interface with `com.okta.authn.sdk.resource.FactorProfile` interface.
- Replaced `com.okta.sdk.resource.user.factor.FactorProvider` interface with `com.okta.authn.sdk.resource.FactorProvider` interface.
- Replaced `com.okta.sdk.resource.user.factor.FactorType` interface with `com.okta.authn.sdk.resource.FactorType` interface.

Note: Old interfaces above were pulled in from **okta-sdk-java** Management SDK hitherto. 
These are now migrated to reside locally within this Authentication SDK. 

Below SDK classes were previously moved to [okta-commons-java](https://github.com/okta/okta-commons-java)).

```
- com.okta.sdk.client.Proxy
- com.okta.sdk.lang.Classes
- com.okta.sdk.lang.Assert
- com.okta.sdk.lang.Strings
- com.okta.sdk.lang.Collections
- com.okta.sdk.lang.Locales
```
