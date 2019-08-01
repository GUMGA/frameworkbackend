package io.gumga.reports.stimulsoft;

/**
 * Created by willian on 09/01/18.
 */


import io.gumga.domain.QGumgaModel;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;
/**
 * QGumgaReport is a Querydsl query type for GumgaReport
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QGumgaReport extends EntityPathBase<GumgaReport> {

    private static final long serialVersionUID = -501273345L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGumgaReport gumgaReport = new QGumgaReport("gumgaReport");

    public final QGumgaModel _super = new QGumgaModel(this);

    public StringPath definition = createString("definition");

    public final StringPath name = createString("name");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> objectId = createNumber("objectId", Long.class);

    public final StringPath objectType = createString("objectType");

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    public final ListPath<GumgaReport, QGumgaReport> values = this.<GumgaReport, QGumgaReport>createList("values", GumgaReport.class, QGumgaReport.class, PathInits.DIRECT2);

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QGumgaReport(String variable) {
        this(GumgaReport.class, forVariable(variable), INITS);
    }

    public QGumgaReport(Path<? extends GumgaReport> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGumgaReport(PathMetadata metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QGumgaReport(PathMetadata metadata, PathInits inits) {
        this(GumgaReport.class, metadata, inits);
    }

    public QGumgaReport(Class<? extends GumgaReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
//        this.definition = inits.isInitialized("definition") ? new StringPath(forProperty("definition")) : null;
    }

}