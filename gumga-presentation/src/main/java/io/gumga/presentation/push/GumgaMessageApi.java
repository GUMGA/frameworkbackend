package io.gumga.presentation.push;

import io.gumga.application.GumgaService;
import io.gumga.domain.GumgaMessage;
import io.gumga.presentation.GumgaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API para manipulação e acesso de mensagens entre usuários
 * {@link GumgaMessage}
 */
@RestController
@RequestMapping("/api/gumgamessage")
public class GumgaMessageApi extends GumgaAPI<GumgaMessage, Long> {
    /**
     * Construtor com a injeção do módulo Service para acesso aos serviços do Framework
     * @param service Objeto Service a ser injetado
     */
    @Autowired
    public GumgaMessageApi(GumgaService<GumgaMessage, Long> service) {
        super(service);
    }

}
