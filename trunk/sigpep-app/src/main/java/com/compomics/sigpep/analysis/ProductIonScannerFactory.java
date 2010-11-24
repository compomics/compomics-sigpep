package com.compomics.sigpep.analysis;

import com.compomics.sigpep.Configuration;
import com.compomics.sigpep.analysis.impl.FindFirstMapProductIonScanner;
import com.compomics.sigpep.model.ProductIonType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: mmueller<br/>
 * Date: 05-Aug-2008<br/>
 * Time: 12:20:28<br/>
 */
public class ProductIonScannerFactory {

    private static ProductIonScannerFactory instance = new ProductIonScannerFactory();
    private static Configuration config = Configuration.getInstance();
    private static String findFirstScannerClass = config.getString("sigpep.app.analysis.find.first.signature.transition.finder.class");
    private static String findMinimalScannerClass = config.getString("sigpep.app.analysis.find.minimal.signature.transition.finder.class");
    private static String findAllScannerClass = config.getString("sigpep.app.analysis.find.all.signature.transition.finder.class");


    private ProductIonScannerFactory() {
    }

    public static ProductIonScannerFactory getInstance() {
        return instance;
    }

    public ProductIonScanner createFindFirstProductIonScanner(Set<ProductIonType> targetProductIonTypes,
                                                              Set<ProductIonType> backgroundProductIonTypes,
                                                              Set<Integer> productIonChargeStates,
                                                              double massAccuracy,
                                                              int minimumCombinationSize,
                                                              int maximumCombinationSize) {

        return getScannerInstance(findFirstScannerClass,
                targetProductIonTypes,
                backgroundProductIonTypes,
                productIonChargeStates,
                massAccuracy,
                minimumCombinationSize,
                maximumCombinationSize);

    }

    public ProductIonScanner createFindMinimalProductIonScanner(Set<ProductIonType> targetProductIonTypes,
                                                                Set<ProductIonType> backgroundProductIonTypes,
                                                                Set<Integer> productIonChargeStates,
                                                                double massAccuracy,
                                                                int minimumCombinationSize,
                                                                int maximumCombinationSize) {

        return getScannerInstance(findMinimalScannerClass,
                targetProductIonTypes,
                backgroundProductIonTypes,
                productIonChargeStates,
                massAccuracy,
                minimumCombinationSize,
                maximumCombinationSize);

    }

    public ProductIonScanner createFindAllProductIonScanner(Set<ProductIonType> targetProductIonTypes,
                                                            Set<ProductIonType> backgroundProductIonTypes,
                                                            Set<Integer> productIonChargeStates,
                                                            double massAccuracy,
                                                            int minimumCombinationSize,
                                                            int maximumCombinationSize) {

        return getScannerInstance(findAllScannerClass,
                targetProductIonTypes,
                backgroundProductIonTypes,
                productIonChargeStates,
                massAccuracy,
                minimumCombinationSize,
                maximumCombinationSize);
        
    }

    private ProductIonScanner getScannerInstance(String scannerClass,
                                                 Set<ProductIonType> targetProductIonTypes,
                                                 Set<ProductIonType> backgroundProductIonTypes,
                                                 Set<Integer> productIonChargeStates,
                                                 Double massAccuracy,
                                                 Integer minimumCombinationSize,
                                                 Integer maximumCombinationSize) {

        Object[] intArgs = new Object[6];
        intArgs[0] = targetProductIonTypes;
        intArgs[1] = backgroundProductIonTypes;
        intArgs[2] = productIonChargeStates;
        intArgs[3] = massAccuracy;
        intArgs[4] = minimumCombinationSize;
        intArgs[5] = maximumCombinationSize;

        ProductIonScanner retVal;

        try {

            Constructor c = Class.forName(scannerClass)
                    .getConstructor(targetProductIonTypes.getClass(),
                            backgroundProductIonTypes.getClass(),
                            productIonChargeStates.getClass(),
                            massAccuracy.getClass(),
                            minimumCombinationSize.getClass(),
                            maximumCombinationSize.getClass());

            retVal = (ProductIonScanner) c.newInstance(intArgs);

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return retVal;

    }

}
