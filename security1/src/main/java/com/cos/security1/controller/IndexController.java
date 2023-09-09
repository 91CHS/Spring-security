package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.KakaoProfile;
import com.cos.security1.model.OAuthToken;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Controller  // view를 리턴하겠다!!
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //auth : userDetails
    @GetMapping("/test/login")
    public @ResponseBody String testLogin(
            Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails userDetails){
        System.out.println("/test/login ===================");
        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
        System.out.println("1.authentication = " + principalDetails.getUser());

        System.out.println("2.userDetails = " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    //oauth (구글) : OAuth2User
    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth){
        System.out.println("/test/oauth/login ===================");
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        System.out.println("1.authentication = " + oAuth2User.getAttributes());

        System.out.println("2.oAuth2User = " + oAuth2User.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }
/*
    @GetMapping("/auth/kakao/callback")
    public @ResponseBody String kakaoCallback(String code){
        // POST방식으로 key=value 카카오쪽으로 데이터를 요청 : 다양한 라이브러리 존재. RestTemplate씀.
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        //HttpBody 오브젝트생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id","949a9813521d3ef3de2541801817522c");
        params.add("redirect_uri","http://localhost:8080/auth/kakao/callback");
        params.add("code",code);  // 변수에 저장해서 쓰는게 더 좋다.
        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = new HttpEntity<>(params,headers);

        // Http 요청하기 - Post방식으로 - response 변수로 응답 받음
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);

        // Gson, Json Simple, ObjectMapper 라이브러리 등 : json을 오브젝트로 맵핑하기
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Access_token = " + oAuthToken.getAccess_token());

        //엑세스 토큰으로 사용자 정보 가져오기
        RestTemplate rt2 = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization","Bearer "+oAuthToken.getAccess_token());
        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest = new HttpEntity<>(headers2);
        // Http 요청하기 - Post방식으로 - response 변수로 응답 받음
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class);

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("카카오 아이디 = " + kakaoProfile.getId());
        System.out.println("이메일 = " + kakaoProfile.getKakao_account().getEmail());

        System.out.println("우리서버 유저네임 = " + kakaoProfile.kakao_account.getEmail()+"_"+kakaoProfile.getId());
        System.out.println("우리서버 이메일 = " + kakaoProfile.getKakao_account().getEmail());
        System.out.println("비밀번호 = "  );



        return response2.getBody();
    }
*/


    // localhost:8080
    // localhost:8080/
    @GetMapping({"","/"})
    public String index() {
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정 : templates (prefix), .mustache (suffix) 생략가능
        return "index"; // src/main/resources/templates/index.mustache
    }

    @GetMapping("/user")
    public @ResponseBody String user(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails = " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin"; }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager"; }

    //login의 경우 시큐리티 페이지가 기본설정임., SecurityConfig 파일 생성 후엔 사용가능.
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm"; }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm"; }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println("user = " + user);
        user.setRole("ROLE_USER"); //예시로 강제로 넣어줬다.
        String rawPassword = user.getPassword(); // 암호화 과정 추가
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); // 회원가입 잘됨. 비밀번호 : 1234 => 시큐리티로 로그인을 할 수 없음. 암호화필요.
        return "redirect:/loginForm"; }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }

}
