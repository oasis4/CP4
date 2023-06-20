package com.github.oasis.craftprotect.config;

import lombok.Data;

@Data
public class SQLDatabaseConfig {

    private boolean enabled;
    private String url;
    private String username;
    private String password;

}
