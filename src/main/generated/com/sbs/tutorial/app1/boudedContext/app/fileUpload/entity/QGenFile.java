package com.sbs.tutorial.app1.boudedContext.app.fileUpload.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGenFile is a Querydsl query type for GenFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGenFile extends EntityPathBase<GenFile> {

    private static final long serialVersionUID = 742467558L;

    public static final QGenFile genFile = new QGenFile("genFile");

    public final com.sbs.tutorial.app1.base.entity.QBaseEntity _super = new com.sbs.tutorial.app1.base.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath fileDir = createString("fileDir");

    public final StringPath fileExt = createString("fileExt");

    public final StringPath fileExtType2Code = createString("fileExtType2Code");

    public final StringPath fileExtTypeCode = createString("fileExtTypeCode");

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Integer> fileNo = createNumber("fileNo", Integer.class);

    public final NumberPath<Integer> fileSize = createNumber("fileSize", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath originFileName = createString("originFileName");

    public final NumberPath<Long> relId = createNumber("relId", Long.class);

    public final StringPath relTypeCode = createString("relTypeCode");

    public final StringPath type2Code = createString("type2Code");

    public final StringPath typeCode = createString("typeCode");

    public QGenFile(String variable) {
        super(GenFile.class, forVariable(variable));
    }

    public QGenFile(Path<? extends GenFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGenFile(PathMetadata metadata) {
        super(GenFile.class, metadata);
    }

}

