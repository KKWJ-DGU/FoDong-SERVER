package Fodong.serverdong.domain.memberToken.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static Fodong.serverdong.domain.member.QMember.member;
import static Fodong.serverdong.domain.memberToken.QMemberToken.memberToken;

@Repository
public class MemberTokenQueryRepositoryImpl implements MemberTokenQueryRepository {

    private final JPAQueryFactory query;

    public MemberTokenQueryRepositoryImpl(EntityManager em){
        this.query = new JPAQueryFactory(em);
    }

    /**
     * 리프레시 토큰으로 회원 이메일 조회
     */
    @Override
    public String findEmailByRefreshToken(String refreshToken) {
        return query.select(member.email)
                .from(memberToken)
                .join(memberToken.member, member)
                .where(memberToken.refreshToken.eq(refreshToken))
                .fetchOne();
    }
}
