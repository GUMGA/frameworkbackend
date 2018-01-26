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

    public static final QMarketPlace marketPlace = new QMarketPlace("marketPlace");
    public final StringPath nome = createString("nome");

    public final io.gumga.domain.QGumgaModel _super = new io.gumga.domain.QGumgaModel(this);

    public final MapPath<String, String, StringPath> fields = this.<String, String, StringPath>createMap("fields", String.class, String.class, StringPath.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final ComparablePath<GumgaOi> oi = _super.oi;

    public QMarketPlace(String variable) {
        super(MarketPlace.class, forVariable(variable));
    }

    public QMarketPlace(Path<? extends MarketPlace> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMarketPlace(PathMetadata<?> metadata) {
        super(MarketPlace.class, metadata);
    }

}
