package com.cos.security1.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String,Object> attributes; // oauth2User.getAttributes()
    private Map<String,Object> kakaoAccountAttributes;
    private Map<String,Object> profileAttributes;



    public KakaoUserInfo(Map<String,Object> attributes) {

        this.attributes=attributes;
        this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
        this.profileAttributes = (Map<String, Object>) kakaoAccountAttributes.get("profile");
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return kakaoAccountAttributes.get("email").toString();
    }

    @Override
    public String getName() {
        return (String)profileAttributes.get("nickname");
    }
}
