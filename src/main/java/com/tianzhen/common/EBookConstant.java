package com.tianzhen.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class EBookConstant {

    @Value("${yuedu.loginUrl}")
    private String loginUrl;

    @Value("${yuedu.searchUrl}")
    private String searchUrl;

    @Value("${yuedu.username}")
    private String username;

    @Value("${yuedu.password}")
    private String password;

    @Value("${yuedu.grant_type}")
    private String grantType;

    @Value("${yuedu.fetchDownloadUrl}")
    private String fetchDownloadUrl;

}
