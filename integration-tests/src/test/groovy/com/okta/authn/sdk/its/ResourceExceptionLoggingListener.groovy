/*
 * Copyright 2018 Okta, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.okta.authn.sdk.its

import com.okta.sdk.resource.ResourceException
import org.testng.ITestResult
import org.testng.TestListenerAdapter

class ResourceExceptionLoggingListener extends TestListenerAdapter {

    @Override
    void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr)

        def throwable = tr.getThrowable()
        if (throwable instanceof ResourceException) {
            PrintStream err = System.err
            err.println("Test failure due to ResourceException: ${throwable.message}")
            throwable.causes.each {
                err.println("    ${it.summary}")
            }

        }
    }
}
