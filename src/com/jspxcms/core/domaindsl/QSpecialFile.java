package com.jspxcms.core.domaindsl;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.jspxcms.core.domain.SpecialFile;
import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QSpecialFile is a Querydsl query type for SpecialFile
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QSpecialFile extends BeanPath<SpecialFile> {

    private static final long serialVersionUID = 1628619895;

    public static final QSpecialFile specialFile = new QSpecialFile("specialFile");

    public final NumberPath<Integer> downloads = createNumber("downloads", Integer.class);

    public final StringPath file = createString("file");

    public final NumberPath<Long> length = createNumber("length", Long.class);

    public final StringPath name = createString("name");

    public QSpecialFile(String variable) {
        super(SpecialFile.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QSpecialFile(Path<? extends SpecialFile> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QSpecialFile(PathMetadata<?> metadata) {
        super(SpecialFile.class, metadata);
    }

}

