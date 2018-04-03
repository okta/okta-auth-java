package com.okta.authn.sdk.its.util

import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit

class StopWatch {

    static long timeEventInSeconds(clos, TimeUnit timeUnit = TimeUnit.SECONDS) {
        def beginTime = Instant.now()
        def endTime
        try {
            clos.call()
        } finally {
            endTime = Instant.now()
        }
        return timeUnit.convert(Duration.between(beginTime, endTime).toMillis(), TimeUnit.MILLISECONDS)
    }
}