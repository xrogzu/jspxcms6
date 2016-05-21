package com.jspxcms.core.domaindsl;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.jspxcms.core.domain.AttachmentRef;
import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QAttachmentRef is a Querydsl query type for AttachmentRef
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAttachmentRef extends EntityPathBase<AttachmentRef> {

    private static final long serialVersionUID = 1501245074;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QAttachmentRef attachmentRef = new QAttachmentRef("attachmentRef");

    public final QAttachment attachment;

    public final NumberPath<Integer> fid = createNumber("fid", Integer.class);

    public final StringPath ftype = createString("ftype");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QSite site;

    public QAttachmentRef(String variable) {
        this(AttachmentRef.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QAttachmentRef(Path<? extends AttachmentRef> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAttachmentRef(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAttachmentRef(PathMetadata<?> metadata, PathInits inits) {
        this(AttachmentRef.class, metadata, inits);
    }

    public QAttachmentRef(Class<? extends AttachmentRef> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attachment = inits.isInitialized("attachment") ? new QAttachment(forProperty("attachment"), inits.get("attachment")) : null;
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
    }

}

