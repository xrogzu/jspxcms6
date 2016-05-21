package com.jspxcms.core.domaindsl;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.jspxcms.core.domain.Global;
import com.jspxcms.core.domain.Site;
import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QGlobal is a Querydsl query type for Global
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGlobal extends EntityPathBase<Global> {

    private static final long serialVersionUID = -115619903;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QGlobal global = new QGlobal("global");

    public final MapPath<String, String, StringPath> clobs = this.<String, String, StringPath>createMap("clobs", String.class, String.class, StringPath.class);

    public final StringPath contextPath = createString("contextPath");

    public final MapPath<String, String, StringPath> customs = this.<String, String, StringPath>createMap("customs", String.class, String.class, StringPath.class);

    public final StringPath dataVersion = createString("dataVersion");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> port = createNumber("port", Integer.class);

    public final StringPath protocol = createString("protocol");

    public final ListPath<Site, QSite> sites = this.<Site, QSite>createList("sites", Site.class, QSite.class, PathInits.DIRECT);

    public final QPublishPoint uploadsPublishPoint;

    public final BooleanPath withDomain = createBoolean("withDomain");

    public QGlobal(String variable) {
        this(Global.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QGlobal(Path<? extends Global> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGlobal(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGlobal(PathMetadata<?> metadata, PathInits inits) {
        this(Global.class, metadata, inits);
    }

    public QGlobal(Class<? extends Global> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.uploadsPublishPoint = inits.isInitialized("uploadsPublishPoint") ? new QPublishPoint(forProperty("uploadsPublishPoint"), inits.get("uploadsPublishPoint")) : null;
    }

}

