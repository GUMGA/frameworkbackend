package io.gumga.reports.stimulsoft;

import io.gumga.core.GumgaIdable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Interface para possibilitar implementação de outros tipos de classe para manipulação de relatórios
 * Created by willian on 09/01/18.
 */
@NoRepositoryBean
public interface IGumgaReport extends GumgaIdable<Long> {

    public String getName();

    public void setName(String name);

    public String getDefinition();

    public void setDefinition(String definition);
}
