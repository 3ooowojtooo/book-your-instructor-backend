package com.quary.bookyourinstructor.controller.authentication;

import bookyourinstructor.usecase.authentication.credentials.CredentialsAuthenticateUseCase;
import bookyourinstructor.usecase.authentication.credentials.NewUserRegistrationCredentialsUseCase;
import bookyourinstructor.usecase.authentication.facebook.FacebookAuthenticateUseCase;
import com.quary.bookyourinstructor.controller.authentication.mapper.AuthenticationMapper;
import com.quary.bookyourinstructor.controller.authentication.request.CredentialsAuthenticationRequest;
import com.quary.bookyourinstructor.controller.authentication.request.FacebookAuthenticationRequest;
import com.quary.bookyourinstructor.controller.authentication.request.NewUserRegistrationCredentialsRequest;
import com.quary.bookyourinstructor.controller.authentication.response.AuthenticationResponse;
import com.quary.bookyourinstructor.model.authentication.EmailAndPassword;
import com.quary.bookyourinstructor.model.authentication.NewUserData;
import com.quary.bookyourinstructor.model.authentication.exception.InvalidEmailOrPasswordException;
import com.quary.bookyourinstructor.model.authentication.exception.UserNotFoundException;
import com.quary.bookyourinstructor.model.authentication.exception.UserWithEmailAlreadyExists;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final CredentialsAuthenticateUseCase credentialsAuthenticateUseCase;
    private final FacebookAuthenticateUseCase facebookAuthenticateUseCase;
    private final NewUserRegistrationCredentialsUseCase newUserRegistrationCredentialsUseCase;
    private final AuthenticationMapper authenticationMapper;

    @PostMapping(path = "/credentials", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthenticationResponse credentialsAuthenticate(@RequestBody final CredentialsAuthenticationRequest request) throws InvalidEmailOrPasswordException {
        final EmailAndPassword emailAndPassword = authenticationMapper.mapToEmailAndPassword(request);
        String jwtToken = credentialsAuthenticateUseCase.authenticate(emailAndPassword);
        return new AuthenticationResponse(jwtToken);
    }

    @PostMapping(path = "/facebook", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthenticationResponse facebookAuthenticate(@RequestBody final FacebookAuthenticationRequest request) throws UserWithEmailAlreadyExists, UserNotFoundException {
        String jwtToken = facebookAuthenticateUseCase.authenticate(request.getAccessToken(), request.getUserType());
        return new AuthenticationResponse(jwtToken);
    }

    @PostMapping(path = "/credentials/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void registerNewUserCredentials(@RequestBody final NewUserRegistrationCredentialsRequest request) throws UserWithEmailAlreadyExists {
        final NewUserData newUserData = authenticationMapper.mapToNewUserData(request);
        newUserRegistrationCredentialsUseCase.registerNewUser(newUserData);
    }
}
