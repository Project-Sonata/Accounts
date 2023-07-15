package com.odeyalo.sonata.account.web.interceptor;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(CustomizableRequest request) {
        request.addCookie("Authorization", "token");
    }
}

