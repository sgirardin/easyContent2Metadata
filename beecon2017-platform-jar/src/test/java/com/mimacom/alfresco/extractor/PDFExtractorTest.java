package com.mimacom.alfresco.extractor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        Rectangle authorMetaDataZone = new Rectangle(350, 70, 300,20);

        //When
        String placeOfDocument = pdfExtractor.extractMetaDataFieldByCoordinate(pdfFileStream, authorMetaDataZone);
        //Then
        Assert.assertEquals("Returning not the same Value","Zaragoza", placeOfDocument.trim());

    }

    @Test
    public void testDateByCoordinate(){
        //Given
        Rectangle authorMetaDataZone = new Rectangle(350, 90, 300,20);

        //When
        String placeOfDocument= pdfExtractor.extractMetaDataFieldByCoordinate(pdfFileStream, authorMetaDataZone);
        //Then
        Assert.assertEquals("Returning not the same Value","25-04-2017", placeOfDocument.trim());

    }

}
