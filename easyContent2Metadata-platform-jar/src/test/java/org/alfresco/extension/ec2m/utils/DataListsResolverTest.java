package org.alfresco.extension.ec2m.utils;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
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
    public void testIsActiveFieldNeedsExtraction() {
        //Given
        NodeRef nodeRefToTest = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, UUID.randomUUID().toString());
        when(mockedNodeService.getProperty(nodeRefToTest, Constants.PROP_TYPE)).thenReturn("type");
        when(mockedNodeService.getProperty(nodeRefToTest, Constants.PROP_CONFIGURATION)).thenReturn(ConfigurationEnum.COORDINATES);
        when(mockedNodeService.getProperty(nodeRefToTest, Constants.ASPECT_ACTIVE)).thenReturn(true);
        dataListsResolver.setNodeService(mockedNodeService);

        //When
        boolean shouldBeTrue = dataListsResolver.fieldNeedsExtraction(nodeRefToTest, ConfigurationEnum.COORDINATES);

        //Then
        //We *want* the value extracted because the item_active aspect is true
        Assert.assertEquals(true, shouldBeTrue);
    }

    @Test
    public void testIsNotActiveFieldExtraction() {
        //Given
        NodeRef nodeRefToTest = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, UUID.randomUUID().toString());
        when(mockedNodeService.getProperty(nodeRefToTest, Constants.PROP_TYPE)).thenReturn("type");
        when(mockedNodeService.getProperty(nodeRefToTest, Constants.PROP_CONFIGURATION)).thenReturn(ConfigurationEnum.COORDINATES);
        when(mockedNodeService.getProperty(nodeRefToTest, Constants.ASPECT_ACTIVE)).thenReturn(false);
        dataListsResolver.setNodeService(mockedNodeService);

        //When
        boolean shouldBeTrue = dataListsResolver.fieldNeedsExtraction(nodeRefToTest, ConfigurationEnum.COORDINATES);

        //Then
        //We *don't want* the value extracted because the item_active aspect is false
        Assert.assertEquals(false, shouldBeTrue);
    }

}
