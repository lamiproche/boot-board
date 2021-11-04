package org.zerock.sb.service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.dto.ReplyDTO;

@Transactional
public interface ReplyService {

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO /*페이징*/);

    //등록
    Long register(ReplyDTO replyDTO);

    //삭제
    PageResponseDTO<ReplyDTO> remove(Long bno, Long rno, PageRequestDTO pageRequestDTO);

    //수정
    PageResponseDTO<ReplyDTO> modify(ReplyDTO replyDTO, PageRequestDTO pageRequestDTO);

}
