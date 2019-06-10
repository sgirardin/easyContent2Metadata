package org.alfresco.extension.ec2m.extractor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
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

    private static final String METADATA_REGION = "metadata";
    private static final Logger logger = LoggerFactory.getLogger(PDFExtractor.class.getName());

    @Override
    public String extractMetaDataFieldByCoordinate(InputStream documentStream, Rectangle searchArea){

        String metadataValue = "";
        try (PDDocument pdfDocument = PDDocument.load(documentStream)){

            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition( true );
            stripper.addRegion( METADATA_REGION, searchArea );

            PDPage firstPage = (PDPage)pdfDocument.getDocumentCatalog().getPages().get(0);
            stripper.extractRegions( firstPage );

            metadataValue = stripper.getTextForRegion( METADATA_REGION).trim();

            logger.info("Extracted coordinate metadata: {} at: {}",metadataValue, searchArea);

        } catch (IOException exception){
            logger.error("Issue extracting metadata with coordinate: {}", searchArea.toString());
        }

        return metadataValue;
    }

    @Override
    public String extractMetaDataFieldByRegex(InputStream documentStream, String regex) {
        String metadataValue = "";
        try (PDDocument pdfDocument = PDDocument.load(documentStream)) {

            PDFTextStripper stripper = new PDFTextStripper();
            String pdfDocumentString = stripper.getText(pdfDocument);

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(pdfDocumentString);
            if (matcher.find()) {
                metadataValue = matcher.group(0);
            }

            logger.info("Extracted regex metadata: {} at: {}",metadataValue, regex);
        } catch (IOException exception) {
            logger.error("Issue extracting metadata with regex: {} ", regex);
        }
        return metadataValue;
    }
}
