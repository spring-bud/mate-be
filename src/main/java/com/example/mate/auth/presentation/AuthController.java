package com.example.mate.auth.presentation;

import com.example.mate.auth.application.AuthService;
import com.example.mate.auth.application.dto.SocialDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/{socialType}")
    public ResponseEntity login(@RequestParam("code") String code, HttpSession session, Model model,
                                @PathVariable("socialType") String socialType) throws Exception {

        System.out.println("code : " + code);
        System.out.println("socialType : " + socialType);
        String accessToken = null;
        accessToken = authService.getAccessToken(code);

        System.out.println("accessToken : " + accessToken);

        SocialDto socialDto = null;

        socialDto = authService.getInfo(accessToken);

        //아이디 생성 또는 로그인

        String token = authService.loginOrRegister(socialDto);
        //String tokenResponse = authService.loginOrRegister(socialType);

        //ResponseCookie cookie = cookieHandler.createCookie(COOKIE_REFRESH_TOKEN, tokenResponse.refreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        HttpSession session = request.getSession(false);
        String kakaoAccessToken = (String) session.getAttribute("kakaoToken");

        authService.disconnect(kakaoAccessToken);

        session.invalidate();

        return "redirect:/";
    }

    //todo민경준 : 테스트용도
    @GetMapping("/")
    public String home(Model model) throws Exception {

        String url = authService.loginUrl();

        String html = "<a href=" + url + ">카카오 로그인</a><hr/><br>";
        model.addAttribute("url", url);
        return html;
    }

}
