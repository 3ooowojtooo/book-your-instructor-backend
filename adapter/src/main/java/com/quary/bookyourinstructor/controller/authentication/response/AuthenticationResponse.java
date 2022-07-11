package com.quary.bookyourinstructor.controller.authentication.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AuthenticationResponse {

    private final String jwtToken;
}
