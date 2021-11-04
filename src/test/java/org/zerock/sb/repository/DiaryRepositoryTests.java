package org.zerock.sb.repository;

import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.sb.dto.DiaryDTO;
import org.zerock.sb.entity.Diary;
import org.zerock.sb.entity.DiaryPicture;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
public class DiaryRepositoryTests {

    @Autowired
    DiaryRepository diaryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Test
    public void testInsert() {
        //dummy diary insert
        IntStream.rangeClosed(1, 100).forEach(i -> {

            Set<String> tags = IntStream.rangeClosed(1, 3).mapToObj(j -> i+"_tag_"+j).collect(Collectors.toSet());
            //tags를 Object화 해야하고 숫자를 string화 해야하므로 mapToObj 사용

            Set<DiaryPicture> pictures = IntStream.rangeClosed(1, 3).mapToObj(j -> {
               DiaryPicture picture = DiaryPicture.builder()
                       .uuid(UUID.randomUUID().toString())
                       .savePath("2021/10/18")
                       .fileName("img" + j + ".jpg")
                       .idx(j)
                       .build();
               return picture;
            }).collect(Collectors.toSet());

            Diary diary = Diary.builder()
                    .title("Sample..." + i)
                    .content("sample..." + i)
                    .writer("user00")
                    .tags(tags)
                    .pictures(pictures)
                    .build();

            diaryRepository.save(diary);
            //현재 테이블이 두개로 나눠져있으므로
       });
    }

    @Test
    public void testSelectOne() {
        Long dno = 1L;

        Optional<Diary> optionalDiary = diaryRepository.findById(dno);

        Diary diary = optionalDiary.orElseThrow();  //예외를 던져줌

        log.info("-------------------------");
        log.info(diary);
        log.info("-------------------------");
        log.info(diary.getTags());
        log.info("-------------------------");
        log.info(diary.getPictures());

    }

    @Transactional
    @Test
    public void testPaging1() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("dno"));

        Page<Diary> result = diaryRepository.findAll(pageable);

        result.get().forEach(diary -> {
            log.info(diary);
            log.info("-----");
            log.info(diary.getTags());
            log.info("-----");
            log.info(diary.getPictures());
            log.info("===========================================================================");

            //Join fetch
        });
    }

    @Test
    public void testSelectOne2() {
        Long dno = 1L;

        Optional<Diary> optionalDiary = diaryRepository.findById(dno);

        Diary diary = optionalDiary.orElseThrow();  //예외를 던져줌

        DiaryDTO dto = modelMapper.map(diary, DiaryDTO.class);

        log.info(dto);

    }

    @Test
    public void testSearchTag() {

        String tag = "1";

        Pageable pageable = PageRequest.of(0,10, Sort.by("dno").descending());

        Page<Diary> result = diaryRepository.searchTags(tag, pageable);

        result.get().forEach(diary -> {
            log.info(diary);
            log.info(diary.getTags());
            log.info(diary.getPictures());
            log.info("--------------------------------------");
        });
    }

    @Test
    public void testDelete() {
        Long dno = 205L;

        diaryRepository.deleteById(dno);
    }

    @Test
    @Transactional
    @Commit
    public void testUpdate() {
        //내용물이 변경되는 것이 아니라 삭제된 후 재삽입된다
        Set<String> updateTags
                = Sets.newHashSet("aaa", "bbb", "ccc");

        Set<DiaryPicture> updatePictures
                = IntStream.rangeClosed(10,15).mapToObj(i -> {
            DiaryPicture picture = DiaryPicture.builder()
                    .uuid(UUID.randomUUID().toString())
                    .savePath("2021/10/19")
                    .fileName("Test" + i + ".jpg")
                    .idx(i)
                    .build();

            return picture;
        }).collect(Collectors.toSet());

        Optional<Diary> optionalDiary = diaryRepository.findById(204L);

        Diary diary = optionalDiary.orElseThrow();

        diary.setTitle("Updated title 204");
        diary.setContent("Updated content 204");
        diary.setTags(updateTags);
        diary.setPictures(updatePictures);

        diaryRepository.save(diary);
    }

    @Test
    public void testWithFavorite() {

        Pageable pageable = PageRequest.of(0,10, Sort.by("dno").descending());

        Page<Object[]> result = diaryRepository.findWithFavoriteCount(pageable);

        for (Object[] objects : result.getContent()) {
            log.info(Arrays.toString(objects));
        }
        //Object[] = {Diary, fCount}

    }

    @Test
    public void testListHard() {

        Pageable pageable = PageRequest.of(0,10, Sort.by("dno").descending());

        Page<Object[]> result = diaryRepository.getSearchList(pageable);

        log.info(result);
    }

}
