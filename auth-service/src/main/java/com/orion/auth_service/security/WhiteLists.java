package com.orion.auth_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WhiteLists {

    public static String[] WHITELISTS_URLS;
    @Autowired
    public void setWhitelistsUrl(@Value("${api.base.path}") String apiBasePath, @Value("${swagger.base.path}") String swaggerPath){
        WhiteLists.WHITELISTS_URLS = new String[]{
                apiBasePath + "/public/**",
                swaggerPath + "/**",
                "/v3/api-docs/**",
                "/swagger-ui/**"
        };
    }
}
