package io.gumga.testmodel;


import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

@Generated("com.mysema.query.codegen.EntitySerializer")
public class QStock extends EntityPathBase<Stock> {

    private static final long serialVersionUID = 1937548501L;


    public static final QStock stock = new QStock("stock");

    public final StringPath nome = createString("nome");

    public final io.gumga.domain.QGumgaModel _super = new io.gumga.domain.QGumgaModel(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<MarketPlace, QMarketPlace> marketPlaces = this.<MarketPlace, QMarketPlace>createList("marketPlaces", MarketPlace.class, QMarketPlace.class, PathInits.DIRECT2);

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    public QStock(String variable) {
        super(Stock.class, forVariable(variable));
    }

    public QStock(Path<? extends Stock> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStock(PathMetadata<?> metadata) {
        super(Stock.class, metadata);
    }

}
