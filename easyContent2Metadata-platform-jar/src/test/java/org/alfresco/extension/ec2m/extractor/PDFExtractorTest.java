package org.alfresco.extension.ec2m.extractor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * Created by sigi on 25/04/2017.
 */
public class PDFExtractorTest{

    PDFExtractor pdfExtractor = new PDFExtractor();

    InputStream pdfFileStream = null;

    @Before
    public void fileSetup()throws FileNotFoundException{
        File initialFile = new File("src/test/resources/BeeconFile2.pdf");

        pdfFileStream = new FileInputStream(initialFile);
    }

    @Test
    public void testNameByCoordinate(){
        //Given
        Rectangle authorMetaDataZone = new Rectangle(350, 50, 300,20);

        //When
        String authorOfDocument = pdfExtractor.extractMetaDataFieldByCoordinate(pdfFileStream, authorMetaDataZone);
        //Then
        Assert.assertEquals("Returning not the same Value","Simon\tGirardin", authorOfDocument.trim());

    }

    @Test
    public void testPlaceByCoordinate(){
        //Given
        Rectangle placeMetaDataZone = new Rectangle(350, 70, 300,20);

        //When
        String placeDocument = pdfExtractor.extractMetaDataFieldByCoordinate(pdfFileStream, placeMetaDataZone);
        //Then
        Assert.assertEquals("Returning not the same Value","Zaragoza", placeDocument.trim());

    }

    @Test
    public void testDateByCoordinate(){
        //Given
        Rectangle dateMetaDataZone = new Rectangle(350, 90, 300,20);

        //When
        String dateOfDocument= pdfExtractor.extractMetaDataFieldByCoordinate(pdfFileStream, dateMetaDataZone);
        //Then
        Assert.assertEquals("Returning not the same Value","25-04-2017", dateOfDocument.trim());

    }

    @Test
    public void testDateByRegex(){
        //Given
        String dateRegex = "\\d{2}(\\.|-)\\d{2}(\\.|-)\\d{4}";

        //When
        String dateOfDocument= pdfExtractor.extractMetaDataFieldByRegex(pdfFileStream, dateRegex);
        //Then
        Assert.assertEquals("Returning not the same Value","25-04-2017", dateOfDocument.trim());

    }

}
