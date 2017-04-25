package com.mimacom.alfresco.fillMetadata;

import com.mimacom.alfresco.extractor.Extractor;
import com.mimacom.alfresco.utils.ConfigurationsEnum;
import com.mimacom.alfresco.utils.Constants;
import com.mimacom.alfresco.utils.DataListsResolver;
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
import org.springframework.beans.factory.annotation.Autowired;
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
        ContentServicePolicies.OnContentUpdatePolicy{

    private NodeService nodeService;
    private ContentService contentService;

    private PolicyComponent policyComponent;

    private Behaviour onCreateNode;
    private Behaviour onContentUpdate;

    @Autowired
    private DataListsResolver dataListsResolver;

    @Autowired
    private Extractor extractor;

    public void init() {
        this.onCreateNode = new JavaBehaviour(this, "onCreateNode",
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT);

        this.onContentUpdate = new JavaBehaviour(this, "onContentUpdate",
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT);

        this.policyComponent.bindClassBehaviour(Constants.ON_CREATE_NODE, ContentModel.TYPE_CONTENT, this.onCreateNode);
        this.policyComponent.bindClassBehaviour(Constants.ON_UPDATE_CONTENT, ContentModel.TYPE_CONTENT, this.onContentUpdate);
    }

    @Override
    public void onCreateNode(ChildAssociationRef childAssociationRef) {
        doBehaviourAction(childAssociationRef.getChildRef());
    }

    @Override
    public void onContentUpdate(NodeRef nodeRef, boolean b) {
        doBehaviourAction(nodeRef);
    }

    private void doBehaviourAction(NodeRef nodeRef){
        String nodeType = this.nodeService.getType(nodeRef).toString();
        if (StringUtils.hasText(nodeType)) {
            try {
                Map<QName, Serializable> extractorValues = new HashMap<>();

                // Extract Coordinates
                extractorValues.putAll(extractMapValues(nodeRef, dataListsResolver.getCoordinatesByType(nodeType), ConfigurationsEnum.COORDINATES));

                // Extract Regex
                extractorValues.putAll(extractMapValues(nodeRef, dataListsResolver.getRegexByType(nodeType), ConfigurationsEnum.REGEX));

                // Extract Constants
                extractorValues.putAll(extractMapValues(nodeRef, dataListsResolver.getValuesByType(nodeType), ConfigurationsEnum.VALUE));

                if (extractorValues.size() > 0) {
                    this.nodeService.setProperties(nodeRef, extractorValues);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Map<QName, Serializable> extractMapValues(NodeRef nodeRef, Map<String, String> toMap, String configurationType){
        Map<QName, Serializable> extractorValues = new HashMap<>();
        for (Map.Entry<String, String> entry : toMap.entrySet()) {
            switch (configurationType){
                case ConfigurationsEnum.COORDINATES:
                    extractorValues.put(QName.createQName(entry.getKey()), extractor.extractMetaDataFieldByCoordinate(getInputStream(nodeRef), transformToRectanlge(entry.getValue().split(",")[0], entry.getValue().split(",")[1], entry.getValue().split(",")[2], entry.getValue().split(",")[3])));
                    break;
                case ConfigurationsEnum.REGEX:
                    extractorValues.put(QName.createQName(entry.getKey()), extractor.extractMetaDataFieldByRegex(getInputStream(nodeRef), entry.getValue()));
                    break;
                case ConfigurationsEnum.VALUE:
                    extractorValues.put(QName.createQName(entry.getKey()), entry.getValue());
                    break;
            }
        }
        return extractorValues;
    }

    private InputStream getInputStream(NodeRef nodeRef){
        return contentService.getReader(nodeRef, ContentModel.PROP_CONTENT).getContentInputStream();
    }

    private Rectangle transformToRectanlge(String x1, String y1, String x2, String y2){
        return new Rectangle(Integer.parseInt(x1), Integer.parseInt(y1), Integer.parseInt(x2) - Integer.parseInt(x1), Integer.parseInt(y2) - Integer.parseInt(y1));
    }


    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setPolicyComponent(PolicyComponent policyComponent) {
        this.policyComponent = policyComponent;
    }
}
