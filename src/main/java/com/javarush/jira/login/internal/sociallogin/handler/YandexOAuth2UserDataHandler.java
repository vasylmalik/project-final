package com.javarush.jira.login.internal.sociallogin.handler;

import org.springframework.stereotype.Component;

@Component("yandex")
public class YandexOAuth2UserDataHandler implements OAuth2UserDataHandler {
    @Override
    public String getFirstName(OAuth2UserData oAuth2UserData) {
        return oAuth2UserData.getData("first_name");
    }

    @Override
    public String getLastName(OAuth2UserData oAuth2UserData) {
        return oAuth2UserData.getData("last_name");
    }

    @Override
    public String getEmail(OAuth2UserData oAuth2UserData) {
        return oAuth2UserData.getData("default_email");
    }
}
