package com.quary.bookyourinstructor.controller.authentication.request;

import com.quary.bookyourinstructor.model.authentication.NewUserType;
import lombok.Data;

@Data
public class NewUserRegistrationCredentialsRequest {

    private String email;
    private String password;
    private String name;
    private String surname;
    private NewUserType type;
}
