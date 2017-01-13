package io.gumga.domain.tag;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;
import io.gumga.domain.QGumgaModel;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

/**
 * QGumgaTagDefinition is a Querydsl query type for GumgaTagDefinition
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGumgaTagDefinition extends EntityPathBase<GumgaTagDefinition> {

    private static final long serialVersionUID = -532591694L;

    public static final QGumgaTagDefinition gumgaTagDefinition = new QGumgaTagDefinition("gumgaTagDefinition");

    public final QGumgaModel _super = new QGumgaModel(this);

    public final ListPath<GumgaTagDefinition, QGumgaTagDefinition> attributes = this.<GumgaTagDefinition, QGumgaTagDefinition>createList("attributes", GumgaTagDefinition.class, QGumgaTagDefinition.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QGumgaTagDefinition(String variable) {
        super(GumgaTagDefinition.class, forVariable(variable));
    }

    public QGumgaTagDefinition(Path<? extends GumgaTagDefinition> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGumgaTagDefinition(PathMetadata<?> metadata) {
        super(GumgaTagDefinition.class, metadata);
    }

}
