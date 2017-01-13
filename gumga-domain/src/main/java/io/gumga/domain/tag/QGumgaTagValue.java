package io.gumga.domain.tag;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;
import io.gumga.domain.QGumgaModel;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

/**
 * QGumgaTagValue is a Querydsl query type for GumgaTagValue
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGumgaTagValue extends EntityPathBase<GumgaTagValue> {

    private static final long serialVersionUID = 1720302514L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGumgaTagValue gumgaTagValue = new QGumgaTagValue("gumgaTagValue");

    public final QGumgaModel _super = new QGumgaModel(this);

    public final QGumgaTagValueDefinition definition;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    public final StringPath value = createString("value");

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QGumgaTagValue(String variable) {
        this(GumgaTagValue.class, forVariable(variable), INITS);
    }

    public QGumgaTagValue(Path<? extends GumgaTagValue> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGumgaTagValue(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGumgaTagValue(PathMetadata<?> metadata, PathInits inits) {
        this(GumgaTagValue.class, metadata, inits);
    }

    public QGumgaTagValue(Class<? extends GumgaTagValue> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.definition = inits.isInitialized("definition") ? new QGumgaTagValueDefinition(forProperty("definition")) : null;
    }

}
