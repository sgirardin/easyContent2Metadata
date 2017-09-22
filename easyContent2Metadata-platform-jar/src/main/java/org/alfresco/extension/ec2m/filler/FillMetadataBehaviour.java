package org.alfresco.extension.ec2m.filler;

import org.alfresco.extension.ec2m.extractor.Extractor;
import org.alfresco.extension.ec2m.utils.ConfigurationEnum;
import org.alfresco.extension.ec2m.utils.Constants;
import org.alfresco.extension.ec2m.utils.DataListsResolver;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.ContentServicePolicies;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by davidanton on 22/4/17.
 */
public class FillMetadataBehaviour
        implements NodeServicePolicies.OnCreateNodePolicy,
        ContentServicePolicies.OnContentUpdatePolicy,
        NodeServicePolicies.OnSetNodeTypePolicy {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private NodeService nodeService;
    private ContentService contentService;

    private PolicyComponent policyComponent;

    private Behaviour onCreateNode;
    private Behaviour onContentUpdate;
    private Behaviour onSetNodeType;

    private DataListsResolver dataListsResolver;
    private Extractor extractor;

    public void init() {
        this.onCreateNode = new JavaBehaviour(this, "onCreateNode",
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT);

        this.onContentUpdate = new JavaBehaviour(this, "onContentUpdate",
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT);

        this.onSetNodeType = new JavaBehaviour(this, "onSetNodeType",
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT);

        this.policyComponent.bindClassBehaviour(Constants.ON_CREATE_NODE, ContentModel.TYPE_CONTENT, this.onCreateNode);
        this.policyComponent.bindClassBehaviour(Constants.ON_UPDATE_CONTENT, ContentModel.TYPE_CONTENT, this.onContentUpdate);
        this.policyComponent.bindClassBehaviour(Constants.ON_SET_NODE_TYPE, ContentModel.TYPE_CONTENT, this.onSetNodeType);
    }

    @Override
    public void onCreateNode(ChildAssociationRef childAssociationRef) {
        logger.debug("FillMetadataBehaviour: onCreateNode start");
        doBehaviourAction(childAssociationRef.getChildRef());
        logger.debug("FillMetadataBehaviour: onCreateNode end");
    }

    @Override
    public void onContentUpdate(NodeRef nodeRef, boolean b) {
        logger.debug("FillMetadataBehaviour: onUpdateNode start");
        doBehaviourAction(nodeRef);
        logger.debug("FillMetadataBehaviour: onUpdateNode end");
    }

    @Override
    public void onSetNodeType(NodeRef nodeRef, QName qName, QName qName1) {
        logger.debug("FillMetadataBehaviour: onSetNodeType start");
        doBehaviourAction(nodeRef);
        logger.debug("FillMetadataBehaviour: onSetNodeType end");
    }


    private void doBehaviourAction(NodeRef nodeRef) {
        try {
            Map<QName, Serializable> nodeProperties = this.nodeService.getProperties(nodeRef);

            // Extract Coordinates
            nodeProperties.putAll(extractMapValues(nodeRef, dataListsResolver.getCoordinatesByType(nodeRef), ConfigurationEnum.COORDINATES));

            // Extract Regex
            nodeProperties.putAll(extractMapValues(nodeRef, dataListsResolver.getRegexByType(nodeRef), ConfigurationEnum.REGEX));

            // Extract Constants
            //nodeProperties.putAll(extractMapValues(nodeRef, dataListsResolver.getValuesByType(), ConfigurationEnum.VALUE));

            if (nodeProperties.size() > 0) {
                this.nodeService.setProperties(nodeRef, nodeProperties);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    private Map<QName, Serializable> extractMapValues(NodeRef nodeRef, Map<String, String> metadataToExtract, ConfigurationEnum configurationType) {
        String value;
        //TODO Not reload all properties SIGI 27.04.2017
        Map<QName, Serializable> extractorValues = new HashMap<>();
        for (Map.Entry<String, String> metadataInfoLine : metadataToExtract.entrySet()) {
            switch (configurationType) {
                case COORDINATES:
                    String[] coordinates = metadataInfoLine.getValue().split(",");
                    Rectangle selectionZone = transformToRectanlge(coordinates[0].trim(), coordinates[1].trim(), coordinates[2].trim(), coordinates[3].trim());
                    value = extractor.extractMetaDataFieldByCoordinate(getInputStream(nodeRef), selectionZone);

                    String textCleanup = value.replace("\t", " ");
                    logger.error("Metadata value: " + textCleanup + " at: " + selectionZone.toString() + " on field " + metadataInfoLine.getKey());
                    if (StringUtils.hasText(textCleanup)) {
                        extractorValues.put(QName.createQName(metadataInfoLine.getKey()), textCleanup);
                    }
                    break;
                case REGEX:
                    value = extractor.extractMetaDataFieldByRegex(getInputStream(nodeRef), metadataInfoLine.getValue());
                    if (StringUtils.hasText(value)) {
                        extractorValues.put(QName.createQName(metadataInfoLine.getKey()), value);
                    }
                    break;
                case VALUE:
                    extractorValues.put(QName.createQName(metadataInfoLine.getKey()), metadataInfoLine.getValue());
                    break;
            }
        }
        return extractorValues;
    }

    protected Rectangle transformToRectanlge(String x1, String y1, String x2, String y2) {
        return new Rectangle(Integer.parseInt(x1), Integer.parseInt(y1), Integer.parseInt(x2) - Integer.parseInt(x1), Integer.parseInt(y2) - Integer.parseInt(y1));
    }

    private InputStream getInputStream(NodeRef nodeRef) {
        return contentService.getReader(nodeRef, ContentModel.PROP_CONTENT).getContentInputStream();
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setExtractor(Extractor extractor) {
        this.extractor = extractor;
    }

    public void setDataListsResolver(DataListsResolver dataListsResolver) {
        this.dataListsResolver = dataListsResolver;
    }

    public void setPolicyComponent(PolicyComponent policyComponent) {
        this.policyComponent = policyComponent;
    }
}
