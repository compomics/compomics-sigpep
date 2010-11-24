package com.compomics.sigpep.web.client;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 04-Mar-2008<br/>
 * Time: 19:24:37<br/>
 */
public class SigPepQueryServiceTest {

    public static void main(String[] args) {

        SigPepQueryImplService service = new SigPepQueryImplService();

        SigPepQueryService query = service.getSigPepQueryImplPort();

    }

}
