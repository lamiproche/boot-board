package org.zerock.sb.entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member {
    //User라고 사용하면 Database의 키워드랑 겹쳐서 사용하지 않는 것을 추천
    //예약어는 되도록 사용하지 말자

    @Id
    private String mid;
    //요즘은 소셜계정으로 로그인 늘어남. id가 이메일일수도 있다고 가정하고 진행

    private String mpw;

    private String mname;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<MemberRole> roleSet = new HashSet<>();

    //회원정보에서 password는 수정가능해야함
    public void changePassword(String password) {
        this.mpw = password;
    }

}
