package Fodong.serverdong.domain.memberToken.repository;

public interface MemberTokenQueryRepository {
    String findEmailByRefreshToken(String refreshToken);
}

