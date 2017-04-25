package com.mimacom.alfresco.extractor;


import org.alfresco.service.cmr.repository.NodeRef;
import ucar.nc2.dataset.CoordinateAxis2D;

/**
 * Interface for the extractor classes
 * Created by sigi on 25/04/2017.
 */
public interface Extractor {


    String extractMetaDateField(NodeRef node, CoordinateAxis2D top, CoordinateAxis2D bottom);
}
