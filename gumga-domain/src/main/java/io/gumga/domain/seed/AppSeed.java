package io.gumga.domain.seed;

import java.io.IOException;

/**
 * Interface para ser utilizada para popular inicialmente um banco de dados
 *
 * @author munif
 */
@FunctionalInterface
public interface AppSeed {

    void loadSeed() throws IOException;

}
