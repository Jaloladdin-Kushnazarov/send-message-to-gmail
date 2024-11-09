package org.example;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User{
    private String userName;
    private String email;
    private String password;
    private boolean active = false;
    private Integer code;
}
