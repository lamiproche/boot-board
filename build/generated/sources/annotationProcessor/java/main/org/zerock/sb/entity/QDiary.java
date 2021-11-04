package org.zerock.sb.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDiary is a Querydsl query type for Diary
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDiary extends EntityPathBase<Diary> {

    private static final long serialVersionUID = -1460880253L;

    public static final QDiary diary = new QDiary("diary");

    public final StringPath content = createString("content");

    public final NumberPath<Long> dno = createNumber("dno", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modDate = createDateTime("modDate", java.time.LocalDateTime.class);

    public final SetPath<DiaryPicture, QDiaryPicture> pictures = this.<DiaryPicture, QDiaryPicture>createSet("pictures", DiaryPicture.class, QDiaryPicture.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final SetPath<String, StringPath> tags = this.<String, StringPath>createSet("tags", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final StringPath writer = createString("writer");

    public QDiary(String variable) {
        super(Diary.class, forVariable(variable));
    }

    public QDiary(Path<? extends Diary> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDiary(PathMetadata metadata) {
        super(Diary.class, metadata);
    }

}

