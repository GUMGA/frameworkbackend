/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application;

import io.gumga.domain.GumgaLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author munif
 */
@Service
public class GumgaLogService extends GumgaService<GumgaLog, Long> {

    private GumgaLogRepository repository;

    @Autowired
    public GumgaLogService(GumgaLogRepository repository) {
        super(repository);
        this.repository = repository;
    }

}
