package com.javarush.jira.login.internal.sociallogin.handler;

import org.springframework.stereotype.Component;

@Component("github")
public class GitHubOAuth2UserDataHandler implements OAuth2UserDataHandler {
    @Override
    public String getFirstName(OAuth2UserData oAuth2UserData) {
        String name = oAuth2UserData.getData("name");
        return name != null ? name : oAuth2UserData.getData("login");
    }

    @Override
    public String getLastName(OAuth2UserData oAuth2UserData) {
        return null;
    }

    @Override
    public String getEmail(OAuth2UserData oAuth2UserData) {
        return oAuth2UserData.getData("email");
    }
}
