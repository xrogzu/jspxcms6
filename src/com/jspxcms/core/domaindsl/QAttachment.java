package com.jspxcms.core.domaindsl;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.jspxcms.core.domain.Attachment;
import com.jspxcms.core.domain.AttachmentRef;
import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QAttachment is a Querydsl query type for Attachment
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAttachment extends EntityPathBase<Attachment> {

    private static final long serialVersionUID = -1328619967;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QAttachment attachment = new QAttachment("attachment");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath ip = createString("ip");

    public final NumberPath<Long> length = createNumber("length", Long.class);

    public final StringPath name = createString("name");

    public final SetPath<AttachmentRef, QAttachmentRef> refs = this.<AttachmentRef, QAttachmentRef>createSet("refs", AttachmentRef.class, QAttachmentRef.class, PathInits.DIRECT);

    public final QSite site;

    public final DateTimePath<java.util.Date> time = createDateTime("time", java.util.Date.class);

    public final QUser user;

    public QAttachment(String variable) {
        this(Attachment.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QAttachment(Path<? extends Attachment> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAttachment(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAttachment(PathMetadata<?> metadata, PathInits inits) {
        this(Attachment.class, metadata, inits);
    }

    public QAttachment(Class<? extends Attachment> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

