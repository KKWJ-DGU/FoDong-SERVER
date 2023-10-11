package Fodong.serverdong.domain.memberToken;


import Fodong.serverdong.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static Fodong.serverdong.domain.memberToken.LonginStatus.LOGIN;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Builder
@AllArgsConstructor
public class MemberToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_token_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "access_token" , nullable = false)
    private String accessToken;

    @Column(name = "access_expiration" , nullable = false)
    private LocalDateTime accessExpiration;

    @Column(name = "refresh_token" , nullable = false)
    private String refreshToken;

    @Column(name = "refresh_expiration" , nullable = false)
    private LocalDateTime refreshExpiration;

    @Column(name = "login_status" , columnDefinition = "LOGIN")
    @Enumerated(EnumType.STRING)
    private LonginStatus loginStatus;

    public void updateTokens(String newAccessToken, LocalDateTime newAccessExpiration, String newRefreshToken, LocalDateTime newRefreshExpiration) {
        this.accessToken = newAccessToken;
        this.accessExpiration = newAccessExpiration;
        this.refreshToken = newRefreshToken;
        this.refreshExpiration = newRefreshExpiration;
    }

}