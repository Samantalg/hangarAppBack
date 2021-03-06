package com.myApp.security.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class UserAppDto {

    private long id;
    @NotNull
    private String username;
    @NotNull
    private String password;
}
