package org.alfresco.extension.ec2m.extractor;


import java.awt.*;
import java.io.InputStream;

/**
 * Interface for the extractor classes
 * Created by sigi on 25/04/2017.
 */
public interface Extractor {


    String extractMetaDataFieldByCoordinate(InputStream node, Rectangle rectangle);

    String extractMetaDataFieldByRegex(InputStream node, String regex);
}
