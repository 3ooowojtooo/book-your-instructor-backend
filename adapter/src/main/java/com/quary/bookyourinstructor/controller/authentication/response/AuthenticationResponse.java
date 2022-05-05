package com.quary.bookyourinstructor.controller.authentication.response;

import lombok.*;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AuthenticationResponse {

    private final String jwtToken;
}
