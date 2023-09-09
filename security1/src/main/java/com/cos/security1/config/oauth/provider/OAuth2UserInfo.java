package com.cos.security1.config.oauth.provider;

public interface OAuth2UserInfo {

    String getProviderId(); // sub , id
    String getProvider();  // google , facebook
    String getEmail();
    String getName();

}
