package com.mimacom.alfresco.fillMetadata;

import org.alfresco.repo.content.ContentServicePolicies;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;

/**
 * Created by davidanton on 22/4/17.
 */
public class FillMetadataBehaviour
        implements NodeServicePolicies.OnCreateNodePolicy,
        ContentServicePolicies.OnContentUpdatePolicy{

    private NodeService nodeService;
    private PolicyComponent policyComponent;

    private Behaviour onCreateNode;
    private Behaviour onDeleteNode;

    public void init() {
        this.onCreateNode = new JavaBehaviour(this, "onCreateNode",
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT);

        this.onDeleteNode = new JavaBehaviour(this, "onDeleteNode",
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT);

        // TODO: Bind new behaviours
        // TODO: Call ExtractorMappingResolver to get the different types
    }

    @Override
    public void onCreateNode(ChildAssociationRef childAssociationRef) {
        // TODO: Call to FillMetadataAction
    }

    @Override
    public void onContentUpdate(NodeRef nodeRef, boolean b) {
        // TODO: Call to FillMetadataAction
    }


    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setPolicyComponent(PolicyComponent policyComponent) {
        this.policyComponent = policyComponent;
    }
}
