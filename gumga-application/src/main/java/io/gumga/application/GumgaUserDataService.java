package io.gumga.application;

import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.GumgaUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe para manipulação de informações adicionais de usuários do sistema
 */
@Service
public class GumgaUserDataService extends GumgaService<GumgaUserData, Long> {

    private GumgaUserDataRepository repository;

    @Autowired
    public GumgaUserDataService(GumgaUserDataRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public void beforeSave(GumgaUserData entity) {
        entity.setUserLogin(GumgaThreadScope.login.get());
    }

    /**
     * Encontra GumgaUserData baseado no atributo key da classe {@link GumgaUserData}
     * @param prefix Chave do atributo que foi criado para o usuário
     * @return dados da pesquisa
     */
    public SearchResult<GumgaUserData> searchByKeyPrefix(String prefix) {
        QueryObject qo = new QueryObject();
        qo.setAq(String.format("obj.userLogin='%s' and obj.key like '%s%%'", GumgaThreadScope.login.get(), prefix));
        return super.pesquisa(qo);
    }

    /**
     * Encontrar GumgaUserData baseado no atributo userLogin e key da classe @{@link GumgaUserData}
     * @param userLogin E-mail do usuário
     * @param key do atributo que foi criado para o usuário
     * @return dados da pesquisa
     */
    public GumgaUserData findByUserLoginAndKey(String userLogin, String key) {
        return repository.findByUserLoginAndKey(userLogin,key);
    }

}
