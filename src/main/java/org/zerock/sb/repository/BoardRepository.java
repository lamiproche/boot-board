package org.zerock.sb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.sb.entity.Board;
import org.zerock.sb.repository.search.BoardSearch;

//JpaRepository<Entity, Entity PK의 data type>
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    //count는 집합함수 -> group by로 grouping 해줘야함
    //무플인경우 대비해서 inner join이 아닌 left(outer) join 사용한 것
    //댓글이 없어도 게시물은 불러와져야하니까
    //,,로 구분되는 것은 무조건 Object의 배열로만 받을 수 있음
    //여기서는 쿼리문이 문자열로 처리되기때문에 오타에 특별히 유의해야함
    @Query("select b, count(r) from Board b left join Reply r on r.board = b group by b")
    Page<Object[]> ex1(Pageable pageable);
    //페이징처리가 되어야하므로 리턴타입은 Page, 파라미터는 Pageable

    //리턴타입 규칙 주의
    //1. 파라미터가 뭔지
    //      파라미터가 Pageable일 경우 리턴타입은 무조건 Page
    //2. Select를 여러개 할 경우
    //      무조건 Object의 배열 <Object[]>

}