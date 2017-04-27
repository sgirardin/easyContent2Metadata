package com.mimacom.alfresco.utils;

import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;

/**
 * Created by davidanton on 24/4/17.
 */
public class Constants {
    private static final String EXTRACTOR_MAPPING_MODEL = "http://www.mimacom.com/alfresco/beecon/1.0";

    public static final String EXTRACTOR_MAPPING_TYPE_LIST = "be:extractorMappingDatalist";

    public static final QName PROP_TYPE          = QName.createQName(EXTRACTOR_MAPPING_MODEL, "type");
    public static final QName PROP_PROPERTY      = QName.createQName(EXTRACTOR_MAPPING_MODEL, "property");
    public static final QName PROP_CONFIGURATION = QName.createQName(EXTRACTOR_MAPPING_MODEL, "configuration");
    public static final QName PROP_VALUE         = QName.createQName(EXTRACTOR_MAPPING_MODEL, "value");

    public static final QName ON_CREATE_NODE = QName.createQName(NamespaceService.ALFRESCO_URI, "onCreateNode");
    public static final QName ON_UPDATE_CONTENT = QName.createQName(NamespaceService.ALFRESCO_URI, "onUpdateContent");
    public static final QName ON_SET_NODE_TYPE = QName.createQName(NamespaceService.ALFRESCO_URI, "onSetNodeType");
}
