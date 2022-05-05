package com.quary.bookyourinstructor.controller.authentication.request;

import lombok.Data;

@Data
public class CredentialsAuthenticationRequest {

    private String email;
    private String password;
}
