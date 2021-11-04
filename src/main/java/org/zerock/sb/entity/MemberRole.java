package org.zerock.sb.entity;

public enum MemberRole {
    //enum - 제한된 수의 INSTANSE 만드는 것이 목적
    //enum으로 생성된 테이블은 기본적으로 int 값을 가져간다
    //  |member_mid|role_set|
    //  |user00    |       1|

    USER, STORE, ADMIN;

}
