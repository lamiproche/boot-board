package org.zerock.sb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.dto.ReplyDTO;
import org.zerock.sb.service.ReplyService;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/list/{bno}")
    public PageResponseDTO<ReplyDTO> getListOfBoard(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO) {
        log.info("----------- c getListOfBoard -----------");
        log.info("----------- c getListOfBoard -----------");
        log.info("----------- c getListOfBoard -----------");

        return replyService.getListOfBoard(bno, pageRequestDTO);
    }

    @PostMapping("") /*마지막(최신) 페이지 리턴*/
    public PageResponseDTO<ReplyDTO> register(@RequestBody ReplyDTO replyDTO ) {
        log.info("----------- c register -----------");
        log.info("----------- c register -----------");
        log.info("----------- c register -----------");

        //rno
        replyService.register(replyDTO);

        //PageRequestDTO 생성
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(-1).build();

        //게시물의 댓글 list 출력
        return replyService.getListOfBoard(replyDTO.getBno(), pageRequestDTO);
    }

    @DeleteMapping("/{bno}/{rno}")
    public PageResponseDTO<ReplyDTO> remove( @PathVariable("bno") Long bno, @PathVariable("rno") Long rno, PageRequestDTO pageRequestDTO ){
        log.info("----------- c remove -----------");
        log.info("----------- c remove -----------");
        log.info("----------- c remove -----------");

        log.info("bno: " + bno);
        log.info("rno: " + rno);

        return replyService.remove(bno, rno, pageRequestDTO);
    }

    @PutMapping("/{bno}/{rno}")
    public PageResponseDTO<ReplyDTO> modify(@PathVariable("bno") Long bno, @PathVariable("rno") Long rno,
                                            @RequestBody ReplyDTO replyDTO, PageRequestDTO pageRequestDTO ){
        log.info("----------- c modify -----------");
        log.info("----------- c modify -----------");
        log.info("----------- c modify -----------");

        log.info("bno: " + bno);
        log.info("rno: " + rno);
        log.info("replyDTO: " + replyDTO);

        return replyService.modify(replyDTO, pageRequestDTO);
    }

}
