// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.tests;

import org.junit.runner.Result;
import org.junit.runner.Request;
import org.junit.runner.JUnitCore;

public final class TestRunner
{
    private static final int MAX_POINTS_ON_PUBLIC_TEST = 10;
    private static final Class[] UNIT_TEST_CLASSES;
    private static final Class[] MODULE_TEST_CLASSES;
    
    private static double runUnitTestsPublic() {
        double numberOfSuccessfulCases = 0.0;
        double numberOfAllCases = 0.0;
        double points = 0.0;
        final JUnitCore jUnitCore = new JUnitCore();
        for (final Class testClass : TestRunner.UNIT_TEST_CLASSES) {
            //System.out.println(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, testClass.getName()));
            System.out.println(testClass.getName());
            final Request request = Request.aClass(testClass);
            final Result result = jUnitCore.run(request);
            //System.out.println(invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, result.getRunCount() - result.getFailureCount()));
            //System.out.println(invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, result.getRunCount()));
            System.out.println("Successful: " + (result.getRunCount() - result.getFailureCount()));
            System.out.println("All: " + result.getRunCount());
            numberOfAllCases = result.getRunCount();
            numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
            points += numberOfSuccessfulCases / numberOfAllCases;
        }
        return points;
    }
    
    private static double runModuleTestsPublic() {
        double numberOfSuccessfulCases = 0.0;
        double numberOfAllCases = 0.0;
        double points = 0.0;
        final JUnitCore jUnitCore = new JUnitCore();
        for (final Class testClass : TestRunner.MODULE_TEST_CLASSES) {
            //System.out.println(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, testClass.getName()));
            System.out.println(testClass.getName());
            final Request request = Request.aClass(testClass);
            final Result result = jUnitCore.run(request);
            //System.out.println(invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, result.getRunCount() - result.getFailureCount()));
            //System.out.println(invokedynamic(makeConcatWithConstants:(I)Ljava/lang/String;, result.getRunCount()));
            System.out.println("Successful: " + (result.getRunCount() - result.getFailureCount()));
            System.out.println("All: " + result.getRunCount());
            numberOfAllCases = result.getRunCount();
            numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
            points += numberOfSuccessfulCases / numberOfAllCases;
        }
        return points;
    }
    
    private static double runPublic() {
        double res = 0.0;
        res += runUnitTestsPublic() * 2.0;
        res += runModuleTestsPublic() * 2.0;
        return res;
    }
    
    public static void runTests() {
        final double resultsPublic = runPublic();
        System.out.println("Points won on public test is: " + resultsPublic);
    }
    
    static {
        UNIT_TEST_CLASSES = new Class[] { CityOperationsTest.class, DistrictOperationsTest.class, UserOperationsTest.class, VehicleOperationsTest.class };
        MODULE_TEST_CLASSES = new Class[] { PublicModuleTest.class };
    }
}
