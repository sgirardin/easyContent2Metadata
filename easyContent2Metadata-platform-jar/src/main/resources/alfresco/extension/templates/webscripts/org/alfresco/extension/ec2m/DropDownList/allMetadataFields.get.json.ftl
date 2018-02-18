{"fields":[
    <#list documentMetaDataFieldsQnames as qname>
        {"field":"${qname.getLocalName()}", "qname":"${qname.toString()}"}
        <#if qname_has_next>,</#if>
    </#list>
]}