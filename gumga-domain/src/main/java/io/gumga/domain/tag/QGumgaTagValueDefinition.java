package io.gumga.domain.tag;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import io.gumga.domain.QGumgaModel;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

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

    public QGumgaTagValueDefinition(PathMetadata<?> metadata) {
        super(GumgaTagValueDefinition.class, metadata);
    }

}
