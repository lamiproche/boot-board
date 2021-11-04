package org.zerock.sb.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.sb.entity.Board;
import org.zerock.sb.entity.QBoard;
import org.zerock.sb.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{
    //QuerydslRepositorySupport - JPA와 함께 Querydsl 사용하는 방법 => JPA의 Repository 확장
    //  1. 미리 인터페이스 객체(BoardSearch)를 만들고 구현할 클래스 생성(BoardSearchImpl)
    //  2. 구현 클래스는 QuerydslRepositorySupport 를 상속받아야함
    //  3. 구현 클래스는 부모 생성자에 도메인클래스를 인자로 넘겨줘야함 (super(Board.class);)

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(char[] typeArr, String keyword, Pageable pageable) {
        log.info("--------------search1");

        QBoard board = QBoard.board;

        JPQLQuery<Board> jpqlQuery = from(board);

        //검색조건이 있다면
        if (typeArr != null && typeArr.length > 0) {
            BooleanBuilder condition = new BooleanBuilder();

            for (char type : typeArr) {
                if (type == 'T') {
                    condition.or(board.title.contains(keyword));
                } else if (type == 'C') {
                    condition.or(board.content.contains(keyword));
                } else if (type == 'W') {
                    condition.or(board.writer.contains(keyword));
                }
            }
            jpqlQuery.where(condition);
        }

        jpqlQuery.where(board.bno.gt(0L));  //greater than = bno > 0

        JPQLQuery<Board> pagingQuery =
                this.getQuerydsl().applyPagination(pageable, jpqlQuery);

        List<Board> boardList = pagingQuery.fetch();

        long totalCount = pagingQuery.fetchCount();

        return new PageImpl<>(boardList, pageable, totalCount);
    }

    @Override
    public Page<Object[]> searchWithReplyCount(char[] typeArr, String keyword, Pageable pageable) {
        log.info("----- searchWithReplyCount -----");

        //쿼리 생성 방법
        //1. getEntityManager 이용
        //      this.getEntityManager().createQuery()
        //2. getQuerydsl 이용
        //     this.getQuerydsl().createQuery()

        //Query 만들 때는 Q도메인 -- 값 뽑을 때는 엔티티타입 / 값
        //그래서 left join을 하는 현재 상황에서는 Q도메인이 필요한 것
        QBoard qBoard  = QBoard.board;
        QReply qReply = QReply.reply;

        // Board b left join Reply r on r.board.bno = b.bno

        JPQLQuery<Board> query = from(qBoard);
        query.leftJoin(qReply).on(qReply.board.eq(qBoard));  //on ~~
//        query.where(qBoard.bno.eq(200L));  //where 절
        query.groupBy(qBoard);  //group by


        //검색조건이 있다면
        if (typeArr != null && typeArr.length > 0) {
            BooleanBuilder condition = new BooleanBuilder();

            for (char type : typeArr) {
                if (type == 'T') {
                    condition.or(qBoard.title.contains(keyword));
                } else if (type == 'C') {
                    condition.or(qBoard.content.contains(keyword));
                } else if (type == 'W') {
                    condition.or(qBoard.writer.contains(keyword));
                }
            }
            query.where(condition);
        }



        //order by, paging 필요
        log.info("-------------------");
//        log.info(query);
//        log.info("-------------------");

        //select를 통해 원하는 값만 추출할 수 있음
        JPQLQuery<Tuple> selectQuery = query.select(qBoard.bno, qBoard.title, qBoard.writer, qBoard.regDate, qReply.count());
        //위에서는 type이 JPQLQuery<Board> 였지만 이제 JPQLQuery<Tuple>로 바뀌게됨

        //Paging
        this.getQuerydsl().applyPagination(pageable, selectQuery);


        log.info(selectQuery);
        log.info("-------------------");

        List<Tuple> tupleList = selectQuery.fetch();  //진짜 sql문 실행
        long totalCount = selectQuery.fetchCount();
        //page type으로 만들기(interface)

        /*
        select board.bno, board.title, board.writer, board.regDate, count(reply)
        from Board board
        left join Reply reply with reply.board = board
        where board.bno = ?1
        group by board
        이렇게 작성하면 편하기야 하지만 정석적인 방법도 아니고 나중에 에러 발생할 가능성 큼
         */

        List<Object[]> arr = tupleList.stream()
                .map(tuple -> tuple.toArray()).collect(Collectors.toList());

        return new PageImpl<>(arr, pageable, totalCount);
    }

}
