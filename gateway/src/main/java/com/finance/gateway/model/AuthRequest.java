package com.finance.gateway.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

}

