/*
 * Copyright 2017-Present Okta, Inc.
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
package com.okta.authn.sdk.resource;

public enum FactorType {

  CALL("call"),

  EMAIL("email"),

  PUSH("push"),

  QUESTION("question"),

  SMS("sms"),

  TOKEN_HARDWARE("token:hardware"),

  TOKEN_HOTP("token:hotp"),

  TOKEN_SOFTWARE_TOTP("token:software:totp"),

  TOKEN("token"),

  U2F("u2f"),

  WEB("web"),

  WEBAUTHN("webauthn");

  private String value;

  FactorType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

}
