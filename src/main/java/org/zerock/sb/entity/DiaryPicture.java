package org.zerock.sb.entity;

import lombok.*;

import javax.persistence.Embeddable;

//이게 걸리면 @ElementCollection 처리한다는 것
//이걸 걸어야만 @ElementCollection 처리 가능함
//PK 필요XXXX
@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "uuid")
public class DiaryPicture implements Comparable<DiaryPicture> {
    //not entity! 값객체

    private String uuid;

    //파일이름
    private String fileName;

    //대표사진 설정 위해 순번 부여
    private int idx;

    //저장경로
    private String savePath;

    @Override
    public int compareTo(DiaryPicture o) {
        //정렬 시 순서 정해주는 역할
        return this.idx - o.idx;  //int로 반환
        //this.idx - o.idx : asc
        //o.idx - this.idx : desc
    }
}
