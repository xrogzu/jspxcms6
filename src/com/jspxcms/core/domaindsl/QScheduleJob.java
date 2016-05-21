package com.jspxcms.core.domaindsl;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.jspxcms.core.domain.ScheduleJob;
import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QScheduleJob is a Querydsl query type for ScheduleJob
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QScheduleJob extends EntityPathBase<ScheduleJob> {

    private static final long serialVersionUID = -1617488824;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QScheduleJob scheduleJob = new QScheduleJob("scheduleJob");

    public final StringPath code = createString("code");

    public final StringPath cronExpression = createString("cronExpression");

    public final NumberPath<Integer> cycle = createNumber("cycle", Integer.class);

    public final StringPath data = createString("data");

    public final StringPath description = createString("description");

    public final DateTimePath<java.util.Date> endTime = createDateTime("endTime", java.util.Date.class);

    public final StringPath group = createString("group");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> repeatInterval = createNumber("repeatInterval", Long.class);

    public final QSite site;

    public final NumberPath<Long> startDelay = createNumber("startDelay", Long.class);

    public final DateTimePath<java.util.Date> startTime = createDateTime("startTime", java.util.Date.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final NumberPath<Integer> unit = createNumber("unit", Integer.class);

    public final QUser user;

    public QScheduleJob(String variable) {
        this(ScheduleJob.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QScheduleJob(Path<? extends ScheduleJob> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QScheduleJob(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QScheduleJob(PathMetadata<?> metadata, PathInits inits) {
        this(ScheduleJob.class, metadata, inits);
    }

    public QScheduleJob(Class<? extends ScheduleJob> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

