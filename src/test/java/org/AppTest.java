package org;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.gameStart.multiDimensional.objects.Hypercube;
import org.gameStart.multiDimensional.objects.LinearGeometry;
import org.gameStart.multiDimensional.objects.Point;

/**
 * Unit test for simple App.
 */
public class AppTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        Point[] points = new Point[16];
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    for (int l = 0; l < 2; l++) {
                        points[index++] = new Point(i,j,k,l);
                    }
                }
            }
        }
        Hypercube hypercube = new Hypercube(4,points);
        hypercube.generateLowDimensionalLinearGeometry();
        System.out.println(hypercube);
    }
}
