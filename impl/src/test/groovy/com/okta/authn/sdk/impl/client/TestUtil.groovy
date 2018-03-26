package com.okta.authn.sdk.impl.client

import org.testng.Assert

class TestUtil {

    static def expectException = { Class<? extends Throwable> catchMe, Closure callMe ->
        try {
            callMe.call()
            Assert.fail("Expected ${catchMe.getName()} to be thrown.")
        } catch(e) {
            if (!e.class.isAssignableFrom(catchMe)) {
                throw e
            }
        }
    }
}