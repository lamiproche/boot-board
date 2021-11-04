package org.zerock.sb.entity;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_diary")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString (exclude = {"tags", "pictures"})
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dno;

    private String title;

    private String content;

    private String writer;

    @CreationTimestamp
    private LocalDateTime regDate;

    @UpdateTimestamp
    private LocalDateTime modDate;

    @ElementCollection
    @CollectionTable(name = "tbl_diary_tag")
    @Fetch(value = FetchMode.JOIN)
    @BatchSize(size = 50)  //50개까지는 한번에 가져올 수 있음
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    //원래 collection type 잡을때는 List보단 Set으로 잡는다
    //      그냥 Collection(java.util.collection)이라고 줘도됨
    //왜 Set? - 순서 상관없고 중복 불가능
    // TreeSet

    @ElementCollection
    @CollectionTable(name = "tbl_diary_picture")
    @Fetch(value = FetchMode.JOIN)
    @BatchSize(size = 50)
    private Set<DiaryPicture> pictures;


    //원래 엔티티는 setter를 넣지 않지만 수정위해서 필요한 것만 따로 Setter 설정

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void setPictures(Set<DiaryPicture> pictures) {
        this.pictures = pictures;
    }
}
