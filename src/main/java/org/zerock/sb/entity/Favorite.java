package org.zerock.sb.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"diary", "member"})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;  //넘버링

    //연관관계는 항상 LAZY 걸고 ToString exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Diary diary;
    //관계를 주지 않으면 에러가 발생함

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    //좋아요 or 싫어요 / 평점 / 별점
    //점수로하면 쉬워짐 - 좋아요 +1 / 싫어요 -1
    private int score;

    @CreationTimestamp
    private LocalDateTime regDate;

}
