package Fodong.serverdong.domain.memberToken;

public enum LonginStatus {

    LOGIN("로그인 상태"),LOGOUT("로그아웃 상태");

    private String name;

    LonginStatus(String name) {
        this.name = name;
    }
}
