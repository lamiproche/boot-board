package org.zerock.sb.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDiaryPicture is a Querydsl query type for DiaryPicture
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QDiaryPicture extends BeanPath<DiaryPicture> {

    private static final long serialVersionUID = 2072686331L;

    public static final QDiaryPicture diaryPicture = new QDiaryPicture("diaryPicture");

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Integer> idx = createNumber("idx", Integer.class);

    public final StringPath savePath = createString("savePath");

    public final StringPath uuid = createString("uuid");

    public QDiaryPicture(String variable) {
        super(DiaryPicture.class, forVariable(variable));
    }

    public QDiaryPicture(Path<? extends DiaryPicture> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDiaryPicture(PathMetadata metadata) {
        super(DiaryPicture.class, metadata);
    }

}

