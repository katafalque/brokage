package com.example.brokage.data.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignupRequest {
    private String email;
    private String password;
    @JsonProperty("fist_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String role;
}
