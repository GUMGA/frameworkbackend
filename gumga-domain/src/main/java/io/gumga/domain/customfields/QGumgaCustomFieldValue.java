package io.gumga.domain.customfields;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;
import io.gumga.domain.QGumgaModel;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

/**
 * QGumgaCustomFieldValue is a Querydsl query type for GumgaCustomFieldValue
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGumgaCustomFieldValue extends EntityPathBase<GumgaCustomFieldValue> {

    private static final long serialVersionUID = -714441694L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGumgaCustomFieldValue gumgaCustomFieldValue = new QGumgaCustomFieldValue("gumgaCustomFieldValue");

    public final QGumgaModel _super = new QGumgaModel(this);

    public final DateTimePath<java.util.Date> dateValue = createDateTime("dateValue", java.util.Date.class);

    public final QGumgaCustomField field;

    public final NumberPath<Long> gumgaModelId = createNumber("gumgaModelId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath logicValue = createBoolean("logicValue");

    public final NumberPath<java.math.BigDecimal> numberValue = createNumber("numberValue", java.math.BigDecimal.class);

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    public final StringPath textValue = createString("textValue");

    public QGumgaCustomFieldValue(String variable) {
        this(GumgaCustomFieldValue.class, forVariable(variable), INITS);
    }

    public QGumgaCustomFieldValue(Path<? extends GumgaCustomFieldValue> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGumgaCustomFieldValue(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGumgaCustomFieldValue(PathMetadata<?> metadata, PathInits inits) {
        this(GumgaCustomFieldValue.class, metadata, inits);
    }

    public QGumgaCustomFieldValue(Class<? extends GumgaCustomFieldValue> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.field = inits.isInitialized("field") ? new QGumgaCustomField(forProperty("field")) : null;
    }

}
