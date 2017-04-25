package com.mimacom.alfresco.extractor;


import org.alfresco.service.cmr.repository.NodeRef;
import ucar.nc2.dataset.CoordinateAxis2D;

import java.awt.*;
import java.io.InputStream;

/**
 * Interface for the extractor classes
 * Created by sigi on 25/04/2017.
 */
public interface Extractor {


    String extractMetaDataFieldByCoordinate(InputStream node, Point top, Point bottom);

    String extractMetaDataFieldByRegex(InputStream node, String regex);
}
