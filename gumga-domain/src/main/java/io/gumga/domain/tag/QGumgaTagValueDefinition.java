package io.gumga.domain.tag;

import io.gumga.domain.QGumgaModel;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.processing.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;
/**
 * QGumgaTagValueDefinition is a Querydsl query type for GumgaTagValueDefinition
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGumgaTagValueDefinition extends EntityPathBase<GumgaTagValueDefinition> {

    private static final long serialVersionUID = 1597724325L;

    public static final QGumgaTagValueDefinition gumgaTagValueDefinition = new QGumgaTagValueDefinition("gumgaTagValueDefinition");

    public final QGumgaModel _super = new QGumgaModel(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QGumgaTagValueDefinition(String variable) {
        super(GumgaTagValueDefinition.class, forVariable(variable));
    }

    public QGumgaTagValueDefinition(Path<? extends GumgaTagValueDefinition> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGumgaTagValueDefinition(PathMetadata metadata) {
        super(GumgaTagValueDefinition.class, metadata);
    }

}
