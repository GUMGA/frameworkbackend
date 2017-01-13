package io.gumga.domain.shared;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;
import io.gumga.domain.QGumgaModel;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QGumgaSharedModel is a Querydsl query type for GumgaSharedModel
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QGumgaSharedModel extends EntityPathBase<GumgaSharedModel<? extends java.io.Serializable>> {

    private static final long serialVersionUID = -1157887249L;

    public static final QGumgaSharedModel gumgaSharedModel = new QGumgaSharedModel("gumgaSharedModel");

    public final QGumgaModel _super = new QGumgaModel(this);

    public final StringPath gumgaOrganizations = createString("gumgaOrganizations");

    public final StringPath gumgaUsers = createString("gumgaUsers");

    //inherited
    public final SimplePath<java.io.Serializable> id = _super.id;

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    @SuppressWarnings("all")
    public QGumgaSharedModel(String variable) {
        super((Class)GumgaSharedModel.class, forVariable(variable));
    }

    @SuppressWarnings("all")
    public QGumgaSharedModel(Path<? extends GumgaSharedModel> path) {
        super((Class)path.getType(), path.getMetadata());
    }

    @SuppressWarnings("all")
    public QGumgaSharedModel(PathMetadata<?> metadata) {
        super((Class)GumgaSharedModel.class, metadata);
    }

}

