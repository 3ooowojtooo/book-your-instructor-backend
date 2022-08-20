package com.quary.bookyourinstructor.controller.authentication.request;

import com.quary.bookyourinstructor.model.user.UserType;
import lombok.Data;

@Data
public class FacebookAuthenticationRequest {

    private String accessToken;
    private UserType userType;
}
