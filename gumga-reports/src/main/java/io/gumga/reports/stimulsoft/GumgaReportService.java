package io.gumga.reports.stimulsoft;

import io.gumga.application.GumgaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class GumgaReportService extends GumgaService<GumgaReport, Long> {

    private final static Logger LOG = LoggerFactory.getLogger(GumgaReportService.class);
    private final GumgaReportRepository repository;

    @Autowired
    public GumgaReportService(GumgaReportRepository repository) {
        super(repository);
        this.repository = repository;
    }

}
