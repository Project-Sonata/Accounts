package com.odeyalo.sonata.account.service.password;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class PasswordContainer {
    String oldPassword;
    String newPassword;
}
