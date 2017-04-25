package com.mimacom.alfresco.fillMetadata;

import com.mimacom.alfresco.utils.DataListsResolver;
import org.alfresco.service.cmr.repository.NodeRef;

/**
 * Created by davidanton on 22/4/17.
 */
public class FillMetadataAction {

    private DataListsResolver dataListsResolver;

    public void fillMetadataFromCoordinates(NodeRef nodeRef){
        // TODO: Get DataListsResolver coordinates
        // TODO: Fill metadata properties with PDFBox
    }

    public void fillMetadataFromRegex(NodeRef nodeRef){

    }

    public void fillMetadataFromValues(NodeRef nodeRef){

    }

    public DataListsResolver getDataListsResolver() {
        return dataListsResolver;
    }

    public void setDataListsResolver(DataListsResolver dataListsResolver) {
        this.dataListsResolver = dataListsResolver;
    }
}
