package com.github.oasis.craftprotect.config;

import lombok.Data;

@Data
public class SQLDatabaseConfig {

    private String url;
    private String database;
    private String username;
    private String password;

}
