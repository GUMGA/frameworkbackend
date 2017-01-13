package io.gumga.application;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import io.gumga.domain.QGumgaModel;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QCoisa is a Querydsl query type for Coisa
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCar extends EntityPathBase<Car> {

    private static final long serialVersionUID = -1527314424L;

    public static final QCar car = new QCar("car");

    public final QGumgaModel _super = new QGumgaModel(this);

    public final StringPath color = createString("color");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath oi = createString("oi");

    public final NumberPath<java.math.BigDecimal> valor = createNumber("valor", java.math.BigDecimal.class);

    public QCar(String variable) {
        super(Car.class, forVariable(variable));
    }

    public QCar(Path<? extends Car> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCar(PathMetadata<?> metadata) {
        super(Car.class, metadata);
    }

}

