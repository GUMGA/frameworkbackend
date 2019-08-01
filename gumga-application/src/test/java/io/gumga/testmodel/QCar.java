package io.gumga.testmodel;


import io.gumga.domain.QGumgaModel;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;
import static com.querydsl.core.types.PathMetadataFactory.forVariable;

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

    public QCar(PathMetadata metadata) {
        super(Car.class, metadata);
    }

}

