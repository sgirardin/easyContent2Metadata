package org.alfresco.extension.ec2m.filler;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class FillMetadataBehaviourTest {

    FillMetadataBehaviour fillMetadataBehaviour = new FillMetadataBehaviour();

    @Test
    public void testTransformToRectangleWithStandardValue(){
        //Given
        String x1 = "50";
        String x2 = "100";

        String y1 = "100";
        String y2 = "150";

        int intx1 = Integer.valueOf(x1);
        int intx2 = Integer.valueOf(x2);

        int inty1 = Integer.valueOf(y1);
        int inty2 = Integer.valueOf(y2);

        //When
        Rectangle rectangleToTest = fillMetadataBehaviour.transformToRectanlge(x1, y1, x2, y2);

        //Then
        Assert.assertEquals(Double.valueOf(intx1), Double.valueOf(rectangleToTest.getX()));
        Assert.assertEquals(Double.valueOf(inty1), Double.valueOf(rectangleToTest.getY()));
        Assert.assertEquals(Double.valueOf(intx2 - intx1), Double.valueOf(rectangleToTest.getWidth()));
        Assert.assertEquals(Double.valueOf(inty2 - inty1), Double.valueOf(rectangleToTest.getHeight()));
    }
}
