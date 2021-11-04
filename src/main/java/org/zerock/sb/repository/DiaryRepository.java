package org.zerock.sb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.sb.entity.Diary;
import org.zerock.sb.repository.search.DiarySearch;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long>, DiarySearch {

    //from 뒤에 나오는 애는 무조건 Entity
    //where d.tag.~~ 안되니까 join 사용
    //원래 join 문 사용하면 on 으로 조건을 줘야함
    //      근데 tags 는 그냥 문자열 not column = on 걸 수 없음!
    //      where dt like (:tag)
    @Query("select d from Diary d left outer join d.tags dt where dt like concat('%', :tag, '%') ")
    Page<Diary> searchTags(String tag, Pageable pageable);

    @Query("select d, coalesce(sum(f.score),0) from Diary d left join Favorite f on f.diary = d group by d ")
    Page<Object[]> findWithFavoriteCount(Pageable pageable);
    //오늘은 Diary 내에 Favorite가 없기때문에 on 조건이 필수 (on f.diary = d)
    //entity 간에 포함관계일때만 on 조건 생략

}