package org;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.Tool.MathUtil;
import org.gameStart.multiDimensional.objects.Hypercube;
import org.gameStart.multiDimensional.objects.LinearGeometry;
import org.gameStart.multiDimensional.objects.Point;
import org.gameStart.multiDimensional.objects.Vector;

import java.util.Arrays;

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
        double[] a = new double[]{0,1,0};
        Point point = new Point(0,0,0);
        Vector vector = new Vector(0,0,1);
        System.out.println(Arrays.toString(MathUtil.revolve3D(a, point, vector, 90)));
    }
}
