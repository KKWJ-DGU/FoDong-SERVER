package Fodong.serverdong.domain.member.controller;

import Fodong.serverdong.domain.member.service.MemberService;
import Fodong.serverdong.global.auth.dto.response.ResponseMemberTokenDto;
import Fodong.serverdong.global.auth.oauth.KakaoSocialLogin;
import Fodong.serverdong.global.config.ApiDocumentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/member")
@Tag(name="MemberController",description = "회원 API")

public class MemberController {

    private final MemberService memberService;


    /**
     * 소셜로그인 (KAKAO, APPLE)
     */
    @ApiDocumentResponse
    @Operation(summary = "소셜 로그인",description = "소셜 로그인을 진행합니다.")
    @GetMapping(value = "/login/oauth/{socialType}")
    public ResponseMemberTokenDto oauthLogin (@PathVariable(name = "socialType") String socialType, @RequestParam String code) {

        return memberService.socialUserInfo(socialType,code);

    }
}