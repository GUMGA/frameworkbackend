package io.gumga.presentation.push;

import io.gumga.application.GumgaService;
import io.gumga.domain.GumgaMessage;
import io.gumga.presentation.GumgaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gumgamessage")
public class GumgaMessageApi extends GumgaAPI<GumgaMessage, Long> {

    @Autowired
    public GumgaMessageApi(GumgaService<GumgaMessage, Long> service) {
        super(service);
    }

}
