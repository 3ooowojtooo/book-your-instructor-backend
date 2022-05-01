package com.quary.bookyourinstructor.controller.authentication;

import bookyourinstructor.usecase.authentication.credentials.CredentialsAuthenticateUseCase;
import bookyourinstructor.usecase.authentication.facebook.FacebookAuthenticateUseCase;
import com.quary.bookyourinstructor.controller.authentication.mapper.AuthenticationMapper;
import com.quary.bookyourinstructor.controller.authentication.request.CredentialsAuthenticationRequest;
import com.quary.bookyourinstructor.controller.authentication.request.FacebookAuthenticationRequest;
import com.quary.bookyourinstructor.controller.authentication.response.AuthenticationResponse;
import com.quary.bookyourinstructor.model.authentication.EmailAndPassword;
import com.quary.bookyourinstructor.model.authentication.exception.InvalidEmailOrPasswordException;
import com.quary.bookyourinstructor.model.authentication.exception.UserWithEmailAlreadyExists;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final CredentialsAuthenticateUseCase credentialsAuthenticateUseCase;
    private final FacebookAuthenticateUseCase facebookAuthenticateUseCase;
    private final AuthenticationMapper authenticationMapper;

    @PostMapping(path = "/credentials", consumes = "application/json", produces = "application/json")
    @CrossOrigin(origins = "https://localhost:3000")
    public AuthenticationResponse credentialsAuthenticate(@RequestBody final CredentialsAuthenticationRequest request)
            throws InvalidEmailOrPasswordException {
        final EmailAndPassword emailAndPassword = authenticationMapper.mapToEmailAndPassword(request);
        String jwtToken = credentialsAuthenticateUseCase.authenticate(emailAndPassword);
        return new AuthenticationResponse(jwtToken);
    }

    @PostMapping(path = "/facebook", consumes = "application/json", produces = "application/json")
    @CrossOrigin(origins = "https://localhost:3000")
    public AuthenticationResponse facebookAuthenticate(@RequestBody final FacebookAuthenticationRequest request)
            throws UserWithEmailAlreadyExists {
        String jwtToken = facebookAuthenticateUseCase.authenticate(request.getAccessToken());
        return new AuthenticationResponse(jwtToken);
    }
}
