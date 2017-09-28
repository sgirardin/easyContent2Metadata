package org.alfresco.extension.ec2m.utils;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.alfresco.events.types.DataType.Qname;

public class DataListsResolver {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	private SearchService searchService;
	private NodeService nodeService;
	private StoreRef storeRef = new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");

	public Map<String, String> getValuesByType(NodeRef documentToExtractMetadata){
		List<NodeRef> datalistValues = getExtractorDatalist();
		return getMappingsByConfigurationAndType(datalistValues, documentToExtractMetadata, ConfigurationEnum.VALUE);
	}

	public Map<String, String> getRegexByType(NodeRef documentToExtractMetadata){
		List<NodeRef> datalistValues = getExtractorDatalist();
		return getMappingsByConfigurationAndType(datalistValues, documentToExtractMetadata, ConfigurationEnum.REGEX);
	}

	public Map<String, String> getCoordinatesByType(NodeRef documentToExtractMetadata){
		List<NodeRef> datalistValues = getExtractorDatalist();
		return getMappingsByConfigurationAndType(datalistValues, documentToExtractMetadata,ConfigurationEnum.COORDINATES);
	}

	public Map<String, String> getMappingsByConfigurationAndType(List<NodeRef> datalistValues, NodeRef documentToExtractMetadata,ConfigurationEnum wantedConfiguration){
		Map<String, String> mapConfType = new HashMap<>();
		for (NodeRef extractionDataInformationNode : datalistValues){
			if (fieldNeedsExtraction(extractionDataInformationNode, documentToExtractMetadata, wantedConfiguration)){
				mapConfType.put(nodeService.getProperty(extractionDataInformationNode, Constants.PROP_PROPERTY).toString(),
						nodeService.getProperty(extractionDataInformationNode, Constants.PROP_VALUE).toString());
			}
		}
		return mapConfType;
	}

	public boolean hasMappings(String documentModel){
		List<NodeRef> datalistValues = getExtractorDatalist();
		for (NodeRef nodeRef : datalistValues) {
			if (nodeService.getProperty(nodeRef, Constants.PROP_TYPE).equals(documentModel)) {
				return true;
			}
		}
		return false;
	}

	private List<NodeRef> getExtractorDatalist(){
		String query ="TYPE:\""+ Constants.EXTRACTOR_MAPPING_TYPE_LIST +"\"";
		ResultSet rs = searchService.query(storeRef, SearchService.LANGUAGE_LUCENE, query);

		List<NodeRef> result = rs.getNodeRefs();
		rs.close();

		return result;
	}

	protected boolean fieldNeedsExtraction(NodeRef extractionDataInformationNode, NodeRef documentToExtractMetadata, ConfigurationEnum wantedConfiguration){

		boolean isExtractionActive = isMetaDataExtractionActive(extractionDataInformationNode);
		boolean isSameConfiguration = isSameConfiguration(extractionDataInformationNode, wantedConfiguration);
		boolean isTypeValid = isTypeValid(extractionDataInformationNode, documentToExtractMetadata);

		return isExtractionActive && isSameConfiguration && isTypeValid ;
	}

	protected boolean isTypeValid(NodeRef extractionDataInformationNode, NodeRef documentToExtractMetadata){
		String givenNodeType = nodeService.getProperty(extractionDataInformationNode, Constants.PROP_TYPE).toString();

		boolean isValid = false;
		if(StringUtils.isNotBlank(givenNodeType)) {
			QName givenNodeTypeQname = QName.createQName(givenNodeType);
			if (nodeService.getType(documentToExtractMetadata).equals(givenNodeTypeQname)){
				return true;
			}
		}
		return isValid;
	}

	protected boolean isSameConfiguration(NodeRef extractionDataInformationNode, ConfigurationEnum wantedConfiguration){
		ConfigurationEnum givenNodeConfiguration = ConfigurationEnum.valueOf(nodeService.getProperty(extractionDataInformationNode, Constants.PROP_CONFIGURATION).toString());
		return wantedConfiguration.equals(givenNodeConfiguration);
	}

	protected boolean isMetaDataExtractionActive(NodeRef extractionDataInformationNode){
		return Boolean.valueOf(nodeService.getProperty(extractionDataInformationNode, Constants.ASPECT_ACTIVE).toString());
	}


	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}
	
}
