{"documentQNames":[
    <#list documentQNames as qname>
        {"documentName":"${qname.getLocalName()}", "qname":"${qname.toString()}"}
            <#if qname_has_next>,</#if>
    </#list>
]}