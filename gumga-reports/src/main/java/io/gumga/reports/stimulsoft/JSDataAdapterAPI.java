package io.gumga.reports.stimulsoft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/genericreport/reportconnection")
public class JSDataAdapterAPI {

    private JSDataAdapterService jsDataAdapterService;

    @Autowired
    public JSDataAdapterAPI(JSDataAdapterService jsDataAdapterService) {
        this.jsDataAdapterService = jsDataAdapterService;
    }

    @RequestMapping(value = "/publichello", method = RequestMethod.GET)
    protected String hello(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        return "HELLO! From " + this.getClass().getCanonicalName();
    }

    @RequestMapping(method = RequestMethod.GET)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        process(req, resp);
    }

    @RequestMapping(method = RequestMethod.POST)
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String result = jsDataAdapterService.process(req.getInputStream());
//            System.out.println("----->RESULT--->"+result);
            resp.setHeader("Access-Control-Allow-Origin", "*");
            resp.setHeader("Content-Type", "application/json;charset=UTF-8");
            resp.setHeader("Cache-Control", "no-cache");
            resp.getOutputStream().write(result.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
