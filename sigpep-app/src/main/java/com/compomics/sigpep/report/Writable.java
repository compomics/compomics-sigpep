package com.compomics.sigpep.report;

import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 06-Aug-2008<br/>
 * Time: 11:28:41<br/>
 */
public interface Writable {

    void write(OutputStream outputStream);

}
