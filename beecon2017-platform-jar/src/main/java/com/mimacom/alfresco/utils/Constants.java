package com.mimacom.alfresco.utils;

import org.alfresco.service.namespace.QName;

/**
 * Created by davidanton on 24/4/17.
 */
public class Constants {
    private static final String EXTRACTOR_MAPPING_MODEL = "em";

    public static final String EXTRACTOR_MAPPING_TYPE_LIST = "em:extractorMappingDatalist";

    public static final QName PROP_TYPE          = QName.createQName(EXTRACTOR_MAPPING_MODEL, "type");
    public static final QName PROP_PROPERTY      = QName.createQName(EXTRACTOR_MAPPING_MODEL, "property");
    public static final QName PROP_CONFIGURATION = QName.createQName(EXTRACTOR_MAPPING_MODEL, "configuration");
    public static final QName PROP_VALUE         = QName.createQName(EXTRACTOR_MAPPING_MODEL, "value");
}
