package org.zerock.sb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.sb.dto.*;
import org.zerock.sb.entity.Diary;
import org.zerock.sb.repository.DiaryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService{

    private final ModelMapper modelMapper;

    private final DiaryRepository diaryRepository;

    @Override
    public Long register(DiaryDTO dto) {
        log.info("---------- s register ----------");
        log.info("---------- s register ----------");
        log.info("---------- s register ----------");

        Diary diary = modelMapper.map(dto, Diary.class);

        log.info(diary);
        log.info(diary.getTags());
        log.info(diary.getPictures());
        log.info("--------------------------------------");

        diaryRepository.save(diary);
        return diary.getDno();
    }

    @Override
    public DiaryDTO read(Long dno) {

        Optional<Diary> optionalDiary = diaryRepository.findById(dno);

        Diary diary = optionalDiary.orElseThrow();

        DiaryDTO dto = modelMapper.map(diary, DiaryDTO.class);

        return dto;
    }

    @Override
    public PageResponseDTO<DiaryDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("dno").descending());

        Page<Diary> result = diaryRepository.findAll(pageable);

        long totalCount = result.getTotalElements();

        List<DiaryDTO> dtoList = result.stream().map(
                        diary -> modelMapper.map(diary, DiaryDTO.class))
                .collect(Collectors.toList());

        return new PageResponseDTO<>(pageRequestDTO, (int)totalCount, dtoList);
    }

    @Override
    public PageResponseDTO<DiaryListDTO> getListWithFavorite(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("dno").descending());

        Page<Object[]> result = diaryRepository.findWithFavoriteCount(pageable);

        long totalCount = result.getTotalElements();

        //result.stream().map(diary -> modelMapper.map(diary, DiaryDTO.class))~~
        //이전은 일치하는 entity가 존재했기때문에 modelMapper 이용했지만 지금은 사용할 수 없음

        List<DiaryListDTO> dtoList = result.get().map(objects -> {
            //Object[0] => Diary / Object[0] => favorite score
            Object[] arr = (Object[]) objects;

            Diary diary = (Diary) arr[0];
            long totalScore = (long) arr[1];  //long값으로 넘어옴

//            log.info("--------------------------------------------------");
//            log.info(diary);
//            log.info(totalScore);

            DiaryListDTO diaryListDTO = modelMapper.map(diary, DiaryListDTO.class);
            //일단 diary에서 필요한 것 불러오고
            diaryListDTO.setTotalScore((int) totalScore);
            //favorite의 개수 추가

//            log.info(diaryListDTO);
//            log.info("--------------------------------------------------");

            return diaryListDTO;

        }).collect(Collectors.toList());

        return new PageResponseDTO<>(pageRequestDTO, (int) totalCount, dtoList);
    }
}
