package com.quary.bookyourinstructor.service.authentication.facebook;

import bookyourinstructor.usecase.authentication.facebook.FacebookProfileDataFetcher;
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
    public EmailAndExternalIdentity fetchEmailAndExternalId(String accessToken) {
        final String path = "/me?fields=email&redirect=false&access_token=" + accessToken;
        log.info("Path: {}, accessToken: {}", graphApiBase + path, accessToken);
        final FacebookMeEndpointResponse response = restTemplate.getForObject(graphApiBase + path, FacebookMeEndpointResponse.class);
        Objects.requireNonNull(response, "FB graph api /me endpoint responded with null object");
        return new EmailAndExternalIdentity(response.getEmail(),
                new ExternalIdentity(response.getId(), ExternalIdentityProvider.FACEBOOK));
    }

    @Data
    private static class FacebookMeEndpointResponse {
        private String email;
        private String id;
    }
}
