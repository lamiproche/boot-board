package org.zerock.sb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.sb.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findReplyByBoard_BnoOrderByRno(Long rno);

    @Query("select r from Reply r where r.board.bno = :bno")  //파라미터로 넘어온 값 처리할 때 앞에 : 붙임
    Page<Reply> getListByBno(Long bno, Pageable pageable);
    //paremeter가 Pageable이면 return type은 Page

    @Query("SELECT COUNT(r) FROM Reply r WHERE r.board.bno = :bno")
    int getReplyCountOfBoard(Long bno);
    //리밋은 마이너스 안됨

}
