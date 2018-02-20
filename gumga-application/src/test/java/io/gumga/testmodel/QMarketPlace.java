package io.gumga.testmodel;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.*;
import io.gumga.domain.domains.GumgaOi;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

/**
 * Created by gumgait on 25/01/18.
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMarketPlace extends EntityPathBase<MarketPlace> {

    private static final long serialVersionUID = 1378794666L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMarketPlace marketPlace = new QMarketPlace("marketPlace");

    public final io.gumga.domain.QGumgaModel _super = new io.gumga.domain.QGumgaModel(this);

    public final MapPath<String, String, StringPath> fields = this.<String, String, StringPath>createMap("fields", String.class, String.class, StringPath.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nome = createString("nome");

    //inherited
    public final ComparablePath<io.gumga.domain.domains.GumgaOi> oi = _super.oi;

    public final QStock stock;

    public QMarketPlace(String variable) {
        this(MarketPlace.class, forVariable(variable), INITS);
    }

    public QMarketPlace(Path<? extends MarketPlace> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMarketPlace(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMarketPlace(PathMetadata<?> metadata, PathInits inits) {
        this(MarketPlace.class, metadata, inits);
    }

    public QMarketPlace(Class<? extends MarketPlace> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.stock = inits.isInitialized("stock") ? new QStock(forProperty("stock")) : null;
    }

}
//@Generated("com.mysema.query.codegen.EntitySerializer")
//public class QMarketPlace extends EntityPathBase<MarketPlace> {
//
//    private static final long serialVersionUID = 1378794666L;
//
//    public static final QMarketPlace marketPlace = new QMarketPlace("marketPlace");
//    public final StringPath nome = createString("nome");
//
//    public final io.gumga.domain.QGumgaModel _super = new io.gumga.domain.QGumgaModel(this);
//
//    public final MapPath<String, String, StringPath> fields = this.<String, String, StringPath>createMap("fields", String.class, String.class, StringPath.class);
//
//    public final NumberPath<Long> id = createNumber("id", Long.class);
//
//    //inherited
//    public final ComparablePath<GumgaOi> oi = _super.oi;
//
//    public QMarketPlace(String variable) {
//        super(MarketPlace.class, forVariable(variable));
//    }
//
//    public QMarketPlace(Path<? extends MarketPlace> path) {
//        super(path.getType(), path.getMetadata());
//    }
//
//    public QMarketPlace(PathMetadata<?> metadata) {
//        super(MarketPlace.class, metadata);
//    }
//
//
//}
