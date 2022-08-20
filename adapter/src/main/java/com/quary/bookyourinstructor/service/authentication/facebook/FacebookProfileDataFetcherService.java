package com.quary.bookyourinstructor.service.authentication.facebook;

import bookyourinstructor.usecase.authentication.facebook.FacebookProfileDataFetcher;
import bookyourinstructor.usecase.authentication.facebook.FacebookUserData;
import com.quary.bookyourinstructor.model.authentication.EmailAndExternalIdentity;
import com.quary.bookyourinstructor.model.user.ExternalIdentity;
import com.quary.bookyourinstructor.model.user.ExternalIdentityProvider;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@Slf4j
public class FacebookProfileDataFetcherService implements FacebookProfileDataFetcher {

    private final RestTemplate restTemplate;
    private final String graphApiBase;

    @Autowired
    public FacebookProfileDataFetcherService(RestTemplate restTemplate,
                                             @Value("${external-login.facebook.graph-api-base}") final String graphApiBase) {
        this.restTemplate = restTemplate;
        this.graphApiBase = graphApiBase;
    }

    @Override
    public FacebookUserData fetchEmailAndExternalId(String accessToken) {
        log.info("Getting facebook profile data basing on the access token");
        final String path = "/me?fields=email,first_name,last_name&redirect=false&access_token=" + accessToken;
        final FacebookMeEndpointResponse response = restTemplate.getForObject(graphApiBase + path, FacebookMeEndpointResponse.class);
        Objects.requireNonNull(response, "FB graph api /me endpoint responded with null object");
        return buildUserData(response);
    }

    private static FacebookUserData buildUserData(FacebookMeEndpointResponse facebookResponse) {
        EmailAndExternalIdentity emailAndExternalIdentity = new EmailAndExternalIdentity(facebookResponse.getEmail(),
                new ExternalIdentity(facebookResponse.getId(), ExternalIdentityProvider.FACEBOOK));
        return new FacebookUserData(emailAndExternalIdentity, facebookResponse.getFirst_name(), facebookResponse.getLast_name());
    }

    @Data
    private static class FacebookMeEndpointResponse {
        private String email;
        private String first_name;
        private String last_name;
        private String id;
    }
}
