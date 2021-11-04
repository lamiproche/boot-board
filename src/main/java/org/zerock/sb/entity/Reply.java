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
@ToString(exclude = "board")  //board는 ToString에서 제외하도록 설정
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String replyText;

    private String replyer;  //외래키 댓글-게시글 ManyToOne

    //FK 설정 시 bno를 걸지않고 객체를 걸어줌
    //어떤 관계인지 서술해줘야 에러가 발생하지 않음
    //  fetch - 쓸데없이 Board를 가져오면 안되니까 기본적으로 LAZY 걸어줘야함
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;
    //ToString 찍으면 Board도 찍히기때문에 연관관계는 ToString 안되도록 설정해줘야함

    @CreationTimestamp
    private LocalDateTime replyDate;

    //댓글 수정 시 값을 받아와야하므로 따로 setter 설정
    public void setText(String text) {
        this.replyText = text;
    }
}
