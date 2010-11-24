package com.compomics.sigpep.web.client;

import java.util.List;


/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Mar-2008<br/>
 * Time: 11:05:48<br/>
 */
public class SigPepClient {

    private static SigPepQueryImplService service = new SigPepQueryImplService();

    public static void main(String[] args) {
        try {

            SigPepClient client = new SigPepClient();
            SigPepQueryService query = client.createQuery();
            String response = query.sayHello("Michael");
            System.out.println(response);

            

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public SigPepQueryService createQuery(){
        return service.getSigPepQueryImplPort();
    }


}
