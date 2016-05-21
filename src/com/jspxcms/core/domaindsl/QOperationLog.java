package com.jspxcms.core.domaindsl;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.jspxcms.core.domain.OperationLog;
import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QOperationLog is a Querydsl query type for OperationLog
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QOperationLog extends EntityPathBase<OperationLog> {

    private static final long serialVersionUID = -38552069;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QOperationLog operationLog = new QOperationLog("operationLog");

    public final NumberPath<Integer> dataId = createNumber("dataId", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final StringPath name = createString("name");

    public final QSite site;

    public final StringPath text = createString("text");

    public final DateTimePath<java.util.Date> time = createDateTime("time", java.util.Date.class);

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final QUser user;

    public QOperationLog(String variable) {
        this(OperationLog.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QOperationLog(Path<? extends OperationLog> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QOperationLog(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QOperationLog(PathMetadata<?> metadata, PathInits inits) {
        this(OperationLog.class, metadata, inits);
    }

    public QOperationLog(Class<? extends OperationLog> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

