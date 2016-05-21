package com.jspxcms.core.domaindsl;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.jspxcms.core.domain.Special;
import com.jspxcms.core.domain.SpecialFile;
import com.jspxcms.core.domain.SpecialImage;
import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QSpecial is a Querydsl query type for Special
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSpecial extends EntityPathBase<Special> {

    private static final long serialVersionUID = -1418788773;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QSpecial special = new QSpecial("special");

    public final QSpecialCategory category;

    public final MapPath<String, String, StringPath> clobs = this.<String, String, StringPath>createMap("clobs", String.class, String.class, StringPath.class);

    public final DateTimePath<java.util.Date> creationDate = createDateTime("creationDate", java.util.Date.class);

    public final QUser creator;

    public final MapPath<String, String, StringPath> customs = this.<String, String, StringPath>createMap("customs", String.class, String.class, StringPath.class);

    public final ListPath<SpecialFile, QSpecialFile> files = this.<SpecialFile, QSpecialFile>createList("files", SpecialFile.class, QSpecialFile.class, PathInits.DIRECT);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<SpecialImage, QSpecialImage> images = this.<SpecialImage, QSpecialImage>createList("images", SpecialImage.class, QSpecialImage.class, PathInits.DIRECT);

    public final StringPath largeImage = createString("largeImage");

    public final StringPath metaDescription = createString("metaDescription");

    public final StringPath metaKeywords = createString("metaKeywords");

    public final QModel model;

    public final BooleanPath recommend = createBoolean("recommend");

    public final NumberPath<Integer> refers = createNumber("refers", Integer.class);

    public final QSite site;

    public final StringPath smallImage = createString("smallImage");

    public final StringPath specialTemplate = createString("specialTemplate");

    public final StringPath title = createString("title");

    public final StringPath video = createString("video");

    public final NumberPath<Long> videoLength = createNumber("videoLength", Long.class);

    public final StringPath videoName = createString("videoName");

    public final StringPath videoTime = createString("videoTime");

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public final BooleanPath withImage = createBoolean("withImage");

    public QSpecial(String variable) {
        this(Special.class, forVariable(variable), INITS);
    }

    @SuppressWarnings("all")
    public QSpecial(Path<? extends Special> path) {
        this((Class)path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSpecial(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSpecial(PathMetadata<?> metadata, PathInits inits) {
        this(Special.class, metadata, inits);
    }

    public QSpecial(Class<? extends Special> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QSpecialCategory(forProperty("category"), inits.get("category")) : null;
        this.creator = inits.isInitialized("creator") ? new QUser(forProperty("creator"), inits.get("creator")) : null;
        this.model = inits.isInitialized("model") ? new QModel(forProperty("model"), inits.get("model")) : null;
        this.site = inits.isInitialized("site") ? new QSite(forProperty("site"), inits.get("site")) : null;
    }

}

