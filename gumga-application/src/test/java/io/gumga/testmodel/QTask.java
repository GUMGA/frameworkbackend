package io.gumga.testmodel;


import io.gumga.domain.QGumgaModel;
import io.gumga.domain.domains.GumgaOi;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;
/**
 * Created by rafael on 08/04/15.
 */
public class QTask extends EntityPathBase<Task> {

    private static final long serialVersionUID = -1527314424L;

    public static final QTask task = new QTask("task");

    public final QGumgaModel _super = new QGumgaModel(this);

    public final StringPath name = createString("name");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    public final NumberPath<java.math.BigDecimal> valor = createNumber("valor", java.math.BigDecimal.class);

    public QTask(String variable) {
        super(Task.class, forVariable(variable));
    }

    public QTask(Path<? extends Task> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTask(PathMetadata metadata) {
        super(Task.class, metadata);
    }

}