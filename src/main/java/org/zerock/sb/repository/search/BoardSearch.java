package org.zerock.sb.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.sb.entity.Board;

public interface BoardSearch {

    //char[] typeArr, String keyword 검색조건

    Page<Board> search1(char[] typeArr, String keyword, Pageable pageable);

    Page<Object[]> searchWithReplyCount(char[] typeArr, String keyword, Pageable pageable);

    //나중에 댓글내용 검색도 가능함

}