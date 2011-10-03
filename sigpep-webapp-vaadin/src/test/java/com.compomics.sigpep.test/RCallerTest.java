package com.compomics.sigpep.test;

import org.junit.*;
import com.compomics.acromics.config.RCallerConfiguration;

/**
 * This class is a
 */
public class RCallerTest {

    @Test
    public void testRCallerLocation() {
        RCallerConfiguration.setRscriptLocation("new_location");
        String lRscriptLocation = RCallerConfiguration.getRscriptLocation();
        Assert.assertEquals(lRscriptLocation, "new_location");
    }
}
