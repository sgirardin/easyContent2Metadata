package org.alfresco.extension.ec2m;

import org.alfresco.extension.ec2m.utils.ConfigurationEnum;
import org.alfresco.extension.ec2m.utils.Constants;
import org.alfresco.extension.ec2m.utils.DataListsResolver;
import org.alfresco.model.ContentModel;
import org.alfresco.rad.test.AbstractAlfrescoIT;
import org.alfresco.rad.test.AlfrescoTestRunner;
import org.alfresco.repo.model.Repository;
import org.alfresco.repo.nodelocator.CompanyHomeNodeLocator;
import org.alfresco.repo.virtual.config.CompanyHomeContext;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.PropertyMap;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Collections;
import java.util.List;

@RunWith(value = AlfrescoTestRunner.class)
public class MetadataExtractionIT extends AbstractAlfrescoIT {

    @Test
    public void testTextPdfExtraction() {
        //Create FieldExtractors
        List<ChildAssociationRef> test = createTestDataListExtractorObjects();

        //Write new document
        ChildAssociationRef createdDocument = getServiceRegistry().getNodeService().createNode(
                getServiceRegistry().getNodeLocatorService().getNode(CompanyHomeNodeLocator.NAME, null, null),
                ContentModel.ASSOC_CONTAINS,
                QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, "testFile"),
                ContentModel.TYPE_CONTENT

        );

        File testFile = new File("BeeconFile2.pdf");
        getServiceRegistry().getFileFolderService().getWriter(createdDocument.getChildRef()).putContent(testFile);
        //Test extraction on new document


        // Delete FieldExtractors and generated documents
//        getServiceRegistry().getFileFolderService().delete(createdDocument.getChildRef());
/*        for (ChildAssociationRef nodeToDelete : test
             ) {
            getServiceRegistry().getNodeService().deleteNode(nodeToDelete.getChildRef());
        }*/

    }

    private List<ChildAssociationRef> createTestDataListExtractorObjects(){


        NodeRef dataListSiteNode = returnDataListSiteNode();

        PropertyMap creatorMetadataProperties = new PropertyMap(3);
        creatorMetadataProperties.put(Constants.ASPECT_ACTIVE, "true");
        creatorMetadataProperties.put(Constants.PROP_TYPE, ConstantsForTests.TEST_CONTENT_MODEL);
        creatorMetadataProperties.put(Constants.PROP_CONFIGURATION, ConfigurationEnum.COORDINATES);
        creatorMetadataProperties.put(Constants.PROP_PROPERTY, "true");
        creatorMetadataProperties.put(Constants.PROP_VALUE, "350,50,650,70");

        ChildAssociationRef creatorChildAssociationRef = getServiceRegistry().getNodeService().createNode(
                dataListSiteNode,
                QName.createQName("assocQName"),
                QName.createQName(""),
                QName.createQName(Constants.EXTRACTOR_MAPPING_TYPE_LIST),
                creatorMetadataProperties);

        return Collections.singletonList(creatorChildAssociationRef);
    }

    private NodeRef returnDataListSiteNode(){
        ResultSet dataListResultSet= getServiceRegistry().getSearchService().query(
                StoreRef.STORE_REF_WORKSPACE_SPACESSTORE,
                SearchService.LANGUAGE_XPATH,
                "/app:company_home/st:sites/cm:metadata-extraction-information/cm:dataLists/");

        if(dataListResultSet.getNumberFound()>0){
            return dataListResultSet.getNodeRef(0);
        } else {
            throw new IllegalStateException("No DataList for metadata extraction found");
        }
    }

}