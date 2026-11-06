package com.javarush.jira.login.internal.sociallogin.handler;

import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

@AllArgsConstructor
public class OAuth2UserData {
    private final OAuth2User oAuth2User;
    private final OAuth2UserRequest oAuth2UserRequest;

    @Nullable
    @SuppressWarnings("unchecked")
    public <A> A getData(String name) {
        A attribute = oAuth2User.getAttribute(name);
        return attribute != null ? attribute : (A) oAuth2UserRequest.getAdditionalParameters().get(name);
    }
}
