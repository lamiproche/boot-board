package org.zerock.sb.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.zerock.sb.entity.Board;
import org.zerock.sb.entity.Reply;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    ReplyRepository replyRepository;

    @Test
    public void insert200() {
        IntStream.rangeClosed(1, 200).forEach(i -> {

            Long bno = (long) (200 - (i % 5));  //200, 199, 198, 197, 196

            int replyCount = (i % 5);  //0, 1, 2, 3, 4

            IntStream.rangeClosed(0, replyCount).forEach(j -> {

                Board board = Board.builder().bno(bno).build();

                Reply reply = Reply.builder()
                        .replyText("Reply...")
                        .replyer("replyer...")
                        .board(board) /*단방향참조니까 얘만 바꿔줘도 되는 것임임*/
                        .build();

                replyRepository.save(reply);
            });  //inner loop

        });  //outer loop

    }

    @Test
    public void testRead() {

        Long rno = 1L;

        Reply reply = replyRepository.findById(rno).get();

        log.info(reply);

    }

    @Test
    public void testByBno() {
        Long bno = 200L;

        List<Reply> replyList =
                replyRepository.findReplyByBoard_BnoOrderByRno(bno);

        replyList.forEach(reply -> log.info(reply));

    }

    @Test
    public void testListOfBoard() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());

        Page<Reply> result = replyRepository.getListByBno(199L, pageable);

        log.info(result.getTotalElements());

        result.get().forEach(reply -> log.info(reply));
    }

    @Test
    public void testCountOfBoard() {
        Long bno = 195L;

        //120개
       int count = replyRepository.getReplyCountOfBoard(bno);

//       if(count == 0 ){
//           Page<Reply> temp = new PageImpl<>() ??// 빈페이지 출력하게인데 안한데 ㅠ
//       }



       int lastPage = (int)(Math.ceil(count/10.0));
       //120/10.0 => 12* 10 130  110~120 (마지막 페이지 계산해서 -1페이지 해주기)
        //limit 110,120
//        int lastpageNum = (int)Math.ceil(count/(double) 10);
//
//        int lastEnd = lastpageNum * 10 ; //120
//
//        int lastStart = lastEnd -10; //110
//
//        log.info(lastStart + " :" +lastEnd);

        // 페이지 번호값 들어감, 0부터 시작 함
        // 페이지번호, 사이즈 , 소트 가 들어감 => 따라서 시작끝 페이지 계산할 필요가 없음, 올릴 필요도 없고, 이 로직 없이 간단하게 ~
        // 우리가 원하는 값은 11, 12 임
        //120 -> size 나누면 12 // 여기서 -1 해주면 11!
        // Sort.by생 략가능 -> asc 정렬

        // 0보다 작으면 0출력 해주고 아니면 -1 해주면된다!


        //방법 1  삼항연산자 처리
        //  Pageable pageable = PageRequest.of(lastPage < 0 ? 0 : -1, 10);

        //방법2
        if(lastPage == 0 ){
            lastPage =1;
        }

        Pageable pageable = PageRequest.of(lastPage -1, 10);


      //  Page<Reply> result = replyRepository.findAll(pageable); // 전체 댓글 갯수
        Page<Reply> result = replyRepository.getListByBno(bno,pageable); // 해당 게시글 댓글 갯수

        log.info("total : " + result.getTotalElements());

        log.info("...." + result.getTotalPages());

        result.get().forEach(reply ->{
            log.info(reply);
        });
    }

}
