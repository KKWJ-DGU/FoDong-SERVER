package Fodong.serverdong.domain.member.controller;

import Fodong.serverdong.domain.member.service.MemberService;
import Fodong.serverdong.domain.memberToken.dto.response.ResponseMemberTokenDto;
import Fodong.serverdong.global.auth.adapter.MemberAdapter;
import Fodong.serverdong.global.config.ApiDocumentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Tag(name="MemberController",description = "회원 API")

public class MemberController {

    private final MemberService memberService;


    /**
     * 소셜로그인 (KAKAO, APPLE)
     */
    @ApiDocumentResponse
    @Operation(summary = "소셜 로그인",description = "소셜 로그인을 진행합니다.")
    @PostMapping(value = "/login/oauth/{socialType}")
    public ResponseEntity<ResponseMemberTokenDto> oauthLogin(
            @PathVariable(name = "socialType") String socialType,
            @RequestHeader("Authorization") String authorization) {

        ResponseMemberTokenDto responseMemberTokenDto = memberService.socialUserInfo(socialType, authorization);

        return new ResponseEntity<>(responseMemberTokenDto, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<?> testMethod(@AuthenticationPrincipal MemberAdapter memberAdapter) {
        return ResponseEntity.ok("User details received " + memberAdapter.getUsername());
    }

    @ApiDocumentResponse
    @Operation(summary = "닉네임 설정", description = "닉네임이 사용 가능한 경우 설정합니다.")
    @PatchMapping("/nickname/{nickname}")
    public ResponseEntity<String> setNickname(@PathVariable String nickname, @AuthenticationPrincipal MemberAdapter memberAdapter) {
        memberService.setNickname(memberAdapter.getUsername(), nickname);
        return ResponseEntity.ok("닉네임이 성공적으로 설정되었습니다.");
    }



}
