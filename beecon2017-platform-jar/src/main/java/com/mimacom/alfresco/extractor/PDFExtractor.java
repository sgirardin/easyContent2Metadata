package com.mimacom.alfresco.extractor;

import org.alfresco.service.cmr.repository.ContentService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by sigi on 25/04/2017.
 */
public class PDFExtractor implements Extractor{

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ContentService contentService;

    private static final String METADATA_REGION = "metadata";


    @Override
    public String extractMetaDataFieldByCoordinate(InputStream documentStream, Rectangle searchArea) {
        PDDocument pdfDocument = null;
        String metadataValue = "";
        try {
            pdfDocument = PDDocument.load(documentStream);

            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition( true );

            stripper.addRegion( METADATA_REGION, searchArea );

            PDPage firstPage = (PDPage)pdfDocument.getDocumentCatalog().getAllPages().get(0);
            stripper.extractRegions( firstPage );

            metadataValue = stripper.getTextForRegion( METADATA_REGION );

        } catch (IOException exception){
            logger.error("Issue extracting metadata");
        }
        return metadataValue;
    }

    @Override
    public String extractMetaDataFieldByRegex(InputStream documentStream, String regex){
        PDDocument pdfDocument = null;
        String metadataValue = "";
        try {
            pdfDocument = PDDocument.load(documentStream);

            PDFTextStripperByArea stripper = new PDFTextStripperByArea();

            PDPage firstPage = (PDPage)pdfDocument.getDocumentCatalog().getAllPages().get(0);
            stripper.getText(pdfDocument);

            String pdfDocumentString = stripper.getText(pdfDocument);
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(pdfDocumentString);
            if (matcher.find())
            {
                metadataValue = matcher.group(1);
            }
        } catch (IOException exception){
            logger.error("Issue extracting metadata");
        }
        return metadataValue;
    }
}
