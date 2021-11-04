package org.zerock.sb.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.ReplyDTO;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

    @Autowired
    ReplyService replyService;

    @Test
    public void testRegister() {
        ReplyDTO replyDTO = ReplyDTO.builder()
                .bno(198L)
                .replyText("198댓글입니다.")
                .replyer("Anonymous")
                .build();
        replyService.register(replyDTO);
        //exclude 처리해서 toString 안됨
    }

    @Test
    public void testList() {
        Long bno = 199L;

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()/*.아무것도 안주면 기본값으로 출력됨*/
                .page(-1)
                .build();

        log.info(replyService.getListOfBoard(bno, pageRequestDTO));
    }

}
