package org.alfresco.extension.ec2m.utils;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataListsResolver {

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	private SearchService searchService;
	private NodeService nodeService;
	private StoreRef storeRef = new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");

	public Map<String, String> getValuesByType() throws Exception {
		List<NodeRef> datalistValues = getExtractorDatalist();
		return getMappingsByConfigurationAndType(datalistValues, ConfigurationEnum.VALUE);
	}

	public Map<String, String> getRegexByType() throws Exception {
		List<NodeRef> datalistValues = getExtractorDatalist();
		return getMappingsByConfigurationAndType(datalistValues, ConfigurationEnum.REGEX);
	}

	public Map<String, String> getCoordinatesByType() throws Exception {
		List<NodeRef> datalistValues = getExtractorDatalist();
		return getMappingsByConfigurationAndType(datalistValues, ConfigurationEnum.COORDINATES);
	}

	public Map<String, String> getMappingsByConfigurationAndType(List<NodeRef> datalistValues, ConfigurationEnum wantedConfiguration){
		Map<String, String> mapConfType = new HashMap<>();
		for (NodeRef extractionDataInformationNode : datalistValues){
			if (fieldNeedsExtraction(extractionDataInformationNode, wantedConfiguration)){
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

	protected boolean fieldNeedsExtraction(NodeRef extractionDataInformationNode, ConfigurationEnum wantedConfiguration){
		String givenNodeType = nodeService.getProperty(extractionDataInformationNode, Constants.PROP_TYPE).toString();
		ConfigurationEnum givenNodeConfiguration = ConfigurationEnum.valueOf(nodeService.getProperty(extractionDataInformationNode, Constants.PROP_CONFIGURATION).toString());

		boolean isActive =  Boolean.valueOf(nodeService.getProperty(extractionDataInformationNode, Constants.ASPECT_ACTIVE).toString());
		boolean isSameConfiguration = wantedConfiguration.equals(givenNodeConfiguration);

		return isActive && isSameConfiguration && givenNodeType != null ;
	}


	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}
	
}
