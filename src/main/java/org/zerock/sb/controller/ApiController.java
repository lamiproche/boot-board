package org.zerock.sb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.sb.dto.BoardDTO;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.service.BoardService;

import java.util.Map;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Log4j2
public class ApiController {

    //Controller 계층에서는 service계층만 봐야함
    //      조합, 가공, 연산 진행
    private final BoardService boardService;

    @GetMapping("/delay")
    public String delay() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hyunn";
    }

    @GetMapping("/board/list")
    public PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO) {
        //pageRequestDTO가 JSON형식으로 들어오니까 RequestBody 걸어줘야함
        log.info("pageRequestDTO: "+pageRequestDTO);
        
        try {
            Thread.sleep(1000);
            //모든 결과가 1초 뒤에와서 비동기 체감하기 좋음
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return boardService.getList(pageRequestDTO);
    }

    @PostMapping("/board/register")
    public Map<String, Long> register(@RequestBody BoardDTO boardDTO) {
        //json으로 data를 받기때문에 RequestBody 사용

        log.info("----------------------------");
        log.info(boardDTO);
        log.info("----------------------------");

        Long bno = boardService.register(boardDTO);

        return Map.of("result", bno);

    }

}
