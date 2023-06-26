package com.javarush.jira.login.internal.sociallogin;

import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.login.internal.UserRepository;
import com.javarush.jira.login.internal.sociallogin.handler.OAuth2UserData;
import com.javarush.jira.login.internal.sociallogin.handler.OAuth2UserDataHandler;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    public static final String SOCIAL_PASSWORD = "[social_pwd]";

    private final UserRepository repository;
    private final Map<String, OAuth2UserDataHandler> oAuth2UserDataHandlers;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String clientRegistrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserDataHandler oAuth2UserDataHandler = oAuth2UserDataHandlers.computeIfAbsent(clientRegistrationId,
                clientRegId -> {
                    throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT),
                            "Unknown provider: " + clientRegistrationId);
                });
        OAuth2UserData oAuth2UserData = new OAuth2UserData(oAuth2User, userRequest);
        String email = oAuth2UserDataHandler.getEmail(oAuth2UserData);
        String firstName = oAuth2UserDataHandler.getFirstName(oAuth2UserData);
        if (email == null || firstName == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN),
                    clientRegistrationId + " account does not contain email or first name");
        }
        return new CustomOAuth2User(oAuth2User, repository.findByEmailIgnoreCase(email.toLowerCase()).orElseGet(() ->
                repository.save(new User(null, email, SOCIAL_PASSWORD,
                        firstName, oAuth2UserDataHandler.getLastName(oAuth2UserData), "", Role.DEV))));
    }
}
