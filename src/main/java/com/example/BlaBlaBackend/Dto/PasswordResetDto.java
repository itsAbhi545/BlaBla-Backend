package com.example.BlaBlaBackend.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetDto {
//     "reset_password_token": "CxGAxz3GdCeVJozHmZd8",
//             "password": "Khushie@32!",
//             "password_confirmation": "Khushie@32!"
    @NotNull(message = "Reset Password Token Cant be Null")
    String reset_password_token;
    @NotNull(message = "Password Cant be Null")
    String password;
    @NotNull(message = "Confirmation Password Cant be Null")
    String password_confirmation;

}
