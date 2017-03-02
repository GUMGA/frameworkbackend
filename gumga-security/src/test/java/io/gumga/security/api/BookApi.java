package io.gumga.security.api;

import io.gumga.application.GumgaService;
import io.gumga.presentation.GumgaAPI;
import io.gumga.security.GumgaOperationKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/test")
public class BookApi extends GumgaAPI<Book, Long> {
    
    
    @Autowired
    public BookApi(GumgaService<Book, Long> service) {
        super(service);
    }

    @RequestMapping(value = "one", method = {RequestMethod.GET})
    @GumgaOperationKey(basic = false, value = "OPERATION_ONE")
    public String operationOne() {
        return "operationOne";
    }

    @RequestMapping(value = "two", method = {RequestMethod.GET})
    @GumgaOperationKey(basic = true, value = "OPERATION_TWO")
    public String operationTwo() {
        return "operationTwo";
    }

}
