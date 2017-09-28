package org.alfresco.extension.ec2m.utils;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.namespace.QName;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Created by sigi on 09/07/2017.
 */
public class DataListsResolverTest {

    @Mock(name="nodeService")
    protected NodeService mockedNodeService;
    @Mock(name="applicationContext")
    protected ApplicationContext mockedApplicationContext;

    private DataListsResolver dataListsResolver = new DataListsResolver();

    @Before
    public void before() throws Exception{
        MockitoAnnotations.initMocks(this);
        doReturn(mockedNodeService).when(mockedApplicationContext).getBean("dbNodeService");
    }

    @Test
    public void testIsActiveIsIdentical() {
        //Given
        boolean identicalIsActiveFlag = true;
        NodeRef metadataNodeRefToTest = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, UUID.randomUUID().toString());
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.PROP_TYPE)).thenReturn("type");
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.PROP_CONFIGURATION)).thenReturn(ConfigurationEnum.COORDINATES);
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.ASPECT_ACTIVE)).thenReturn(identicalIsActiveFlag);
        dataListsResolver.setNodeService(mockedNodeService);

        //When
        boolean isActive = dataListsResolver.isMetaDataExtractionActive(metadataNodeRefToTest);

        //Then
        //We *want* the value extracted because the item_active aspect is true
        Assert.assertEquals(identicalIsActiveFlag, isActive);
    }


    @Test
    public void testConfigurationIsIdentical() {
        //Given
        ConfigurationEnum identicalConfiguration = ConfigurationEnum.COORDINATES;
        NodeRef metadataNodeRefToTest = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, UUID.randomUUID().toString());
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.PROP_TYPE)).thenReturn("type");
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.PROP_CONFIGURATION)).thenReturn(identicalConfiguration);
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.ASPECT_ACTIVE)).thenReturn(true);
        dataListsResolver.setNodeService(mockedNodeService);

        //When
        boolean isSameConfiguration = dataListsResolver.isSameConfiguration(metadataNodeRefToTest, identicalConfiguration);

        //Then
        //We *want* the value extracted because the configuration aspect is true
        Assert.assertEquals(true, isSameConfiguration);
    }

    @Test
    public void testNodeTypesAreIdentical() {
        //Given
        String identicalNodeType = "type";
        NodeRef metadataNodeRefToTest = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, UUID.randomUUID().toString());
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.PROP_TYPE)).thenReturn(identicalNodeType);
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.PROP_CONFIGURATION)).thenReturn(ConfigurationEnum.COORDINATES);
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.ASPECT_ACTIVE)).thenReturn(true);

        NodeRef nodeRefDocumentTobeExtracted = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, UUID.randomUUID().toString());
        when(mockedNodeService.getType(nodeRefDocumentTobeExtracted)).thenReturn(QName.createQName(identicalNodeType));

        dataListsResolver.setNodeService(mockedNodeService);

        //When
        boolean isTypeIdentical = dataListsResolver.isTypeValid(metadataNodeRefToTest, nodeRefDocumentTobeExtracted);

        //Then
        //We *want* the value extracted because the configuration aspect is true
        Assert.assertEquals(true, isTypeIdentical);
    }

    @Test
    public void testIsActiveIsNotIdentical() {
        //Given
        NodeRef nodeRefToTest = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, UUID.randomUUID().toString());
        when(mockedNodeService.getProperty(nodeRefToTest, Constants.PROP_TYPE)).thenReturn("type");
        when(mockedNodeService.getProperty(nodeRefToTest, Constants.PROP_CONFIGURATION)).thenReturn(ConfigurationEnum.COORDINATES);
        when(mockedNodeService.getProperty(nodeRefToTest, Constants.ASPECT_ACTIVE)).thenReturn(false);
        dataListsResolver.setNodeService(mockedNodeService);

        //When
        boolean isActive = dataListsResolver.isMetaDataExtractionActive(nodeRefToTest);

        //Then
        //We *don't want* the value extracted because the item_active aspect is false
        Assert.assertEquals(false, isActive);
    }

    @Test
    public void testConfigurationIsNotIdentical() {
        //Given
        NodeRef metadataNodeRefToTest = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, UUID.randomUUID().toString());
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.PROP_TYPE)).thenReturn("type");
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.PROP_CONFIGURATION)).thenReturn(ConfigurationEnum.COORDINATES);
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.ASPECT_ACTIVE)).thenReturn(true);
        dataListsResolver.setNodeService(mockedNodeService);

        //When
        boolean isSameConfiguration = dataListsResolver.isSameConfiguration(metadataNodeRefToTest, ConfigurationEnum.REGEX);

        //Then
        //We *want* the value extracted because the configuration aspect is true
        Assert.assertEquals(false, isSameConfiguration);
    }

    @Test
    public void testNodeTypesAreNotIdentical() {
        //Given
        NodeRef metadataNodeRefToTest = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, UUID.randomUUID().toString());
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.PROP_TYPE)).thenReturn("type");
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.PROP_CONFIGURATION)).thenReturn(ConfigurationEnum.COORDINATES);
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.ASPECT_ACTIVE)).thenReturn(true);

        NodeRef nodeRefDocumentTobeExtracted = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, UUID.randomUUID().toString());
        when(mockedNodeService.getType(nodeRefDocumentTobeExtracted)).thenReturn(QName.createQName("anotherType"));

        dataListsResolver.setNodeService(mockedNodeService);

        //When
        boolean isTypeIdentical = dataListsResolver.isTypeValid(metadataNodeRefToTest, nodeRefDocumentTobeExtracted);

        //Then
        //We *want* the value extracted because the configuration aspect is true
        Assert.assertEquals(false, isTypeIdentical);
    }

    @Test
    public void testNodeTypeIsNotSet() {
        //Given
        NodeRef metadataNodeRefToTest = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, UUID.randomUUID().toString());
        when(mockedNodeService.getProperty(metadataNodeRefToTest, Constants.PROP_TYPE)).thenReturn("");
        NodeRef nodeRefDocumentTobeExtracted = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, UUID.randomUUID().toString());

        dataListsResolver.setNodeService(mockedNodeService);

        //When
        boolean isTypeSet = dataListsResolver.isTypeValid(metadataNodeRefToTest, nodeRefDocumentTobeExtracted);

        //Then
        Assert.assertEquals(false, isTypeSet);
    }
}
