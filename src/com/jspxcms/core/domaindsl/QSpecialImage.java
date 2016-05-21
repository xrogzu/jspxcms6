package com.jspxcms.core.domaindsl;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.jspxcms.core.domain.SpecialImage;
import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QSpecialImage is a Querydsl query type for SpecialImage
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QSpecialImage extends BeanPath<SpecialImage> {

    private static final long serialVersionUID = -1049511488;

    public static final QSpecialImage specialImage = new QSpecialImage("specialImage");

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    public final StringPath text = createString("text");

    public QSpecialImage(String variable) {
        super(SpecialImage.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QSpecialImage(Path<? extends SpecialImage> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    public QSpecialImage(PathMetadata<?> metadata) {
        super(SpecialImage.class, metadata);
    }

}

