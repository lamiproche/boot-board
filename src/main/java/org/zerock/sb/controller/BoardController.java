package org.zerock.sb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.sb.dto.BoardDTO;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.service.BoardService;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/board/*")
public class BoardController {

    private final BoardService boardService;

    @GetMapping(value = {"", "/list"})
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("----------- c list -----------");
        log.info("----------- c list -----------");
        log.info("----------- c list -----------");

//        model.addAttribute("responseDTO", boardService.getList(pageRequestDTO));
        model.addAttribute("responseDTO", boardService.getListWithReplyCount(pageRequestDTO));
        //N+1 문제 : 게시물 하나 당 쿼리가 계속해서 실행되어야할 때 발생
        //      subquery 날리는 방법
        //      JPA경우엔 LAZY 처리해둬서 left outer join 처리
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/register")
    public void register() {
        log.info("----------- c register -----------");
        log.info("----------- c register -----------");
        log.info("----------- c register -----------");
    }

    @PostMapping("/register")
    public String registerPost(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {

        Long bno = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }

    @GetMapping("/read")
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
        log.info("----------- c read -----------");
        log.info("----------- c read -----------");
        log.info("----------- c read -----------");

        model.addAttribute("dto", boardService.read(bno));
    }



}
