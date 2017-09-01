package io.gumga.reports.stimulsoft;

import io.gumga.application.GumgaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
import io.gumga.presentation.GumgaAPI;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/gumgareport")
@Transactional
public class GumgaReportAPI extends GumgaAPI<GumgaReport, Long> {

    @Autowired
    public GumgaReportAPI(GumgaService<GumgaReport, Long> service) {
        super(service);
    }

}
