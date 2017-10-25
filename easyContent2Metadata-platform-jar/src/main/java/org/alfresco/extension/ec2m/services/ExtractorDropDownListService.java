package org.alfresco.extension.ec2m.services;

import org.alfresco.repo.processor.BaseProcessorExtension;
import org.alfresco.service.cmr.dictionary.DictionaryService;
import org.alfresco.service.namespace.QName;
import org.springframework.extensions.webscripts.DeclarativeWebScript;

import java.util.ArrayList;
import java.util.Collection;


public class ExtractorDropDownListService extends BaseProcessorExtension {

    private DictionaryService dictionaryService;

    public Collection<QName> retrieveAllDocumentModelQName(){
        return dictionaryService.getAllModels();
    }

    public Collection<QName> retrievePropertiesForQName(QName documentModelQNames){
        return dictionaryService.getAllProperties(documentModelQNames);
    }

    public Collection<QName> retrieveAllMetadataFieldsForQName(QName documentModelQNames){
        Collection<QName> metadataQNames;

        if (dictionaryService.getAllModels().contains(documentModelQNames)) {
            metadataQNames = dictionaryService.getAllProperties(documentModelQNames);
            metadataQNames.addAll(dictionaryService.getAspects(documentModelQNames));
        } else {
            metadataQNames = new ArrayList<>();
        }

        return metadataQNames;
    }

    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }
}
