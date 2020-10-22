package io.gumga.domain.customfields;


import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import io.gumga.domain.QGumgaModel;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.processing.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QGumgaCustomField is a Querydsl query type for GumgaCustomField
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGumgaCustomField extends EntityPathBase<GumgaCustomField> {

    private static final long serialVersionUID = 671344271L;

    public static final QGumgaCustomField gumgaCustomField = new QGumgaCustomField("gumgaCustomField");

    public final QGumgaModel _super = new QGumgaModel(this);

    public final BooleanPath active = createBoolean("active");

    public final StringPath clazz = createString("clazz");

    public final StringPath defaultValueScript = createString("defaultValueScript");

    public final StringPath description = createString("description");

    public final StringPath fieldGroup = createString("fieldGroup");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    public final StringPath options = createString("options");

    public final EnumPath<CustomFieldType> type = createEnum("type", CustomFieldType.class);

    public final StringPath validationDescription = createString("validationDescription");

    public final StringPath validationScript = createString("validationScript");

    public final NumberPath<Double> visualizationOrder = createNumber("visualizationOrder", Double.class);

    public QGumgaCustomField(String variable) {
        super(GumgaCustomField.class, forVariable(variable));
    }

    public QGumgaCustomField(Path<? extends GumgaCustomField> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGumgaCustomField(PathMetadata metadata) {
        super(GumgaCustomField.class, metadata);
    }

}
