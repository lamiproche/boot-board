package org.zerock.sb.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.sb.entity.Diary;
import org.zerock.sb.entity.Favorite;
import org.zerock.sb.entity.Member;

import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
public class FavoriteRepositoryTests {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Test
    public void insertDummies() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Long dno = (long) (200 + (i % 5));

            String mid = "user" + i;

            Diary diary = Diary.builder().dno(dno).build();

            Member member = Member.builder().mid(mid).build();

            Favorite favorite = Favorite.builder()
                    .member(member)
                    .diary(diary)
                    .score(1)  //좋아요
                    .build();

            favoriteRepository.save(favorite);
        });
    }

}
