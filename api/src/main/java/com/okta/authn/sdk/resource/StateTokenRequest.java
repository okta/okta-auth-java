package com.okta.authn.sdk.resource;

import com.okta.sdk.resource.Resource;

public interface StateTokenRequest extends Resource {

    String getStateToken();
    StateTokenRequest setStateToken(String stateToken);
}
