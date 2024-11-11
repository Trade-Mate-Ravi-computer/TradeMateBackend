package com.trademate.project.Model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtRequest {
    private String email;
    private String password;
    private int otp;


}
