package io.gumga.security;

import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
public class GumgaSecurityIntegration {

    public void verifyAndCreateOperations(GumgaOperationTO[] gotos) {
        System.out.println("-------->"+Arrays.asList(gotos));
        System.out.println("-------->"+SecurityHelper.listMyOperations(""));
        
    }
    
    
}
