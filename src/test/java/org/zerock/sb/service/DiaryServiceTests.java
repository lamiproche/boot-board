package org.zerock.sb.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.sb.dto.*;
import org.zerock.sb.entity.DiaryPicture;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class DiaryServiceTests {

    @Autowired
    DiaryService diaryService;

    @Test
    public void testRegister() {

        List<String> tags = IntStream.rangeClosed(1, 3).mapToObj(j -> "tag_"+j).collect(Collectors.toList());
        //tags를 Object화 해야하고 숫자를 string화 해야하므로 mapToObj 사용

        List<DiaryPictureDTO> pictures = IntStream.rangeClosed(1, 3).mapToObj(j -> {
            DiaryPictureDTO picture = DiaryPictureDTO.builder()
                    .uuid(UUID.randomUUID().toString())
                    .savePath("2021/10/18")
                    .fileName("img" + j + ".jpg")
                    .idx(j)
                    .build();
            return picture;
        }).collect(Collectors.toList());

        DiaryDTO dto = DiaryDTO.builder()
                .title("title...")
                .content("content...")
                .writer("writer...")
                .tags(tags)
                .pictures(pictures)
                .build();

        diaryService.register(dto);
    }

    @Transactional(readOnly = true)
    @Test
    public void testRead() {

        Long dno = 101L;

        DiaryDTO dto = diaryService.read(dno);

        log.info(dto);

        log.info(dto.getPictures().size( ));

        dto.getPictures().forEach(diaryPictureDTO -> log.info(diaryPictureDTO));
    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<DiaryDTO> pageResponseDTO =
                diaryService.getList(pageRequestDTO);

        log.info(pageResponseDTO);
        log.info("-------------------------");

        pageResponseDTO.getDtoList().forEach(dto -> {
            log.info(dto);
            log.info(dto.getTags());
            log.info(dto.getPictures());
            log.info("-------------------------");
        });
    }

    @Test
    public void testList2() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<DiaryListDTO> pageResponseDTO =
                diaryService.getListWithFavorite(pageRequestDTO);

        log.info(pageResponseDTO);
        log.info("-------------------------");
    }

}
