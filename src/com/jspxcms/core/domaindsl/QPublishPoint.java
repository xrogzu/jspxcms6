package com.jspxcms.core.domaindsl;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.jspxcms.core.domain.PublishPoint;
import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QPublishPoint is a Querydsl query type for PublishPoint
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPublishPoint extends EntityPathBase<PublishPoint> {

    private static final long serialVersionUID = -1642049697;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QPublishPoint publishPoint = new QPublishPoint("publishPoint");

    public final StringPath description = createString("description");

    public final StringPath displayPath = createString("displayPath");

    public final StringPath ftpHostname = createString("ftpHostname");

    public final StringPath ftpPassword = createString("ftpPassword");

    public final NumberPath<Integer> ftpPort = createNumber("ftpPort", Integer.class);

    public final StringPath ftpUsername = createString("ftpUsername");

    public final QGlobal global;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> method = createNumber("method", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> seq = createNumber("seq", Integer.class);

    public final StringPath storePath = createString("storePath");

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public QPublishPoint(String variable) {
        this(PublishPoint.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QPublishPoint(Path<? extends PublishPoint> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPublishPoint(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPublishPoint(PathMetadata<?> metadata, PathInits inits) {
        this(PublishPoint.class, metadata, inits);
    }

    public QPublishPoint(Class<? extends PublishPoint> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.global = inits.isInitialized("global") ? new QGlobal(forProperty("global"), inits.get("global")) : null;
    }

}

