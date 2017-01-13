/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation.api;

import io.gumga.application.GumgaService;
import io.gumga.domain.GumgaLog;
import io.gumga.presentation.GumgaAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author munif
 */
@RestController
@RequestMapping("/public/gumgalog")
public class GumgaLogAPI extends GumgaAPI<GumgaLog, Long> {

    @Autowired
    public GumgaLogAPI(GumgaService<GumgaLog, Long> service) {
        super(service);
    }

}
