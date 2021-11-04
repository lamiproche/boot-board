package org.zerock.sb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.dto.ReplyDTO;
import org.zerock.sb.entity.Board;
import org.zerock.sb.entity.Reply;
import org.zerock.sb.repository.ReplyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ModelMapper modelMapper;

    private final ReplyRepository replyRepository;

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        log.info("---------- s getListOfBoard ----------");
        log.info("---------- s getListOfBoard ----------");
        log.info("---------- s getListOfBoard ----------");

        Pageable pageable = null;
        // 0 -1 이라 -1 값 출력  0보다 작으므로 오류
        //      ==> 해결법 -1 일경우 0이 출력되도록 해주면 됨

        if (pageRequestDTO.getPage() == -1) {  // 식별하고싶어서 -1써줌
            // -1 댓글이 없는 경우, 숫자 마지막 댓글 페이지
            int lastPage = calcLastPage(bno, pageRequestDTO.getSize());

            if(lastPage <= 0){
                lastPage = 1;
            }

            pageRequestDTO.setPage(lastPage);
            // -1 일경우만 페이지를 lastPage로 지정
            //dto는 getter, setter가 자유로우니까 음수가 되지않도록 설정해준 것
        }

        pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        Page<Reply> result = replyRepository.getListByBno(bno, pageable);

        List<ReplyDTO> dtoList = result.get()/* get() -> reply의 stream 생성 */
                .map(reply -> modelMapper.map(reply, ReplyDTO.class))
                .collect(Collectors.toList());

        dtoList.forEach(replyDTO -> log.info(replyDTO));

        return new PageResponseDTO<>(pageRequestDTO, (int) result.getTotalElements(), dtoList);
    }

    @Override
    public Long register(ReplyDTO replyDTO) {
        log.info("---------- s register ----------");
        log.info("---------- s register ----------");
        log.info("---------- s register ----------");

        //원래 ReplyDTO에는 Long 값으로 bno만 받아옴
        // but Entity 객체인 Reply를 보면 Board type 존재
        //여기서도 Board 만들어야함
        //Board board = Board.builder().bno(replyDTO.getBno()).build();
        //      -> 수동으로 만들어주는 것
        
        //ReplyDTO -> Reply 변환
        //변환 시 bno 값 때문에 에러가 발생할 수도 있기때문에 테스트 필수
        Reply reply = modelMapper.map(replyDTO, Reply.class);
        //Long bno 뿐이기때문에 Board 변환 불가능할거라 예상
        //      -> 위에 Board  주석처리하고 자동으로 되는지 확인

        //bno를 제외한 다른 값은 null 뜨지만 Board가 생성되긴함

        log.info(reply);
        log.info(reply.getBoard());

        replyRepository.save(reply);
        
        return reply.getRno();
    }

    @Override
    public PageResponseDTO<ReplyDTO> remove(Long bno, Long rno, PageRequestDTO pageRequestDTO) {
        log.info("---------- s remove ----------");
        log.info("---------- s remove ----------");
        log.info("---------- s remove ----------");

        //PK 값 이용해서 삭제
        replyRepository.deleteById(rno);
        //삭제 후 리스트 불러오기
        return getListOfBoard(bno, pageRequestDTO);
    }

    @Override
    public PageResponseDTO<ReplyDTO> modify(ReplyDTO replyDTO, PageRequestDTO pageRequestDTO) {
        log.info("---------- s modify ----------");
        log.info("---------- s modify ----------");
        log.info("---------- s modify ----------");

        //원본 가져오기 - findById
        Reply reply = replyRepository.findById(replyDTO.getRno()).orElseThrow()/*->없으면 RunTimeException 발생*/;

        reply.setText(replyDTO.getReplyText());

        replyRepository.save(reply);

        return getListOfBoard(replyDTO.getBno(), pageRequestDTO);
    }

    private int calcLastPage(Long bno, double size) {
        log.info("---------- s calcLastPage ----------");
        log.info("---------- s calcLastPage ----------");
        log.info("---------- s calcLastPage ----------");

        //Pageable 반환해주려 했으나 우리에게 진짜 필요한건 lastpage

        int count = replyRepository.getReplyCountOfBoard(bno);

        int lastPage = (int) (Math.ceil(count / size));

        return lastPage;
    }

}
