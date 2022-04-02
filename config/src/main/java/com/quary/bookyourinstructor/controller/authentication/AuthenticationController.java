package com.quary.bookyourinstructor.controller.authentication;

import bookyourinstructor.usecase.authentication.AuthenticateUseCase;
import com.quary.bookyourinstructor.controller.authentication.mapper.AuthenticationMapper;
import com.quary.bookyourinstructor.controller.authentication.request.AuthenticationRequest;
import com.quary.bookyourinstructor.controller.authentication.response.AuthenticationResponse;
import com.quary.bookyourinstructor.model.authentication.EmailAndPassword;
import com.quary.bookyourinstructor.model.authentication.exception.InvalidEmailOrPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticateUseCase authenticateUseCase;
    private final AuthenticationMapper authenticationMapper;

    @PostMapping
    public AuthenticationResponse authenticate(@RequestBody final AuthenticationRequest request) throws InvalidEmailOrPasswordException {
        final EmailAndPassword emailAndPassword = authenticationMapper.mapToEmailAndPassword(request);
        String jwtToken = authenticateUseCase.authenticate(emailAndPassword);
        return new AuthenticationResponse(jwtToken);
    }
}
