package com.ficklebytes.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ficklebytes.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @JsonProperty(value = "newPassword", required = true)
    private String newPassword;
    @JsonProperty(value = "token", required = true)
    private String token;

    @Override
    public String toString() {
        return Utils.toString(this);
    }
}
