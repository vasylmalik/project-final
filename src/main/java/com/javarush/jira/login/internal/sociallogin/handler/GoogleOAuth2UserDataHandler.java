package com.javarush.jira.login.internal.sociallogin.handler;

import org.springframework.stereotype.Component;

@Component("google")
public class GoogleOAuth2UserDataHandler implements OAuth2UserDataHandler {
    @Override
    public String getFirstName(OAuth2UserData oAuth2UserData) {
        return oAuth2UserData.getData("given_name");
    }

    @Override
    public String getLastName(OAuth2UserData oAuth2UserData) {
        return oAuth2UserData.getData("family_name");
    }

    @Override
    public String getEmail(OAuth2UserData oAuth2UserData) {
        return oAuth2UserData.getData("email");
    }
}
