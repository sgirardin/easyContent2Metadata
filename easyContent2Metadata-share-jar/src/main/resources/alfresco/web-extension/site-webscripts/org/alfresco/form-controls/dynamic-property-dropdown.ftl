<div class="form-field">

    <script type="text/javascript">//<![CDATA[
    console.log("Start" + "${fieldHtmlId}");
    YAHOO.util.Event.onChange("template_x002e_toolbar_x002e_data-lists_x0023_default-createRow_prop_ec2m_type", function(){
        new selectAjax("${fieldHtmlId}");
    });

    function selectAjax(currentValueHtmlId) {
        console.log("Start" + currentValueHtmlId.toString());
        this.currentValueHtmlId = currentValueHtmlId;
        var selectFieldQNames = Dom.get(this.currentValueHtmlId);
        this.register = function () {
// Call webscript
            Alfresco.util.Ajax.jsonGet({
                url: "http://localhost:8080/alfresco/s/e2cm/dropdownlist/retrieveallMetadataFields?qname={http://www.mimacom.com/alfresco/easyContent2Metadata/1.0}easyContent2Metadata",
                successCallback: {
                    fn: this.updateOptions,
                    scope: this
                },
                failureCallback: {
                    fn: function () {
                    },
                    scope: this
                }
            });
        };

// Add options into <select>
        this.updateOptions = function (res) {
            var result = Alfresco.util.parseJSON(res.serverResponse.responseText);
            if (result.fields.length > 0) {
                var fields = result.fields;
                selectFieldQNames.options[selectFieldQNames.options.length] = new Option("", "", true, true);
                for (var i in fields) {
                    var option = new Option(fields[i].field, fields[i].qname);
                    selectFieldQNames.options.add(option);
                }
            }
        };

        this.register();
    }
    //]]></script>

    <label for="${fieldHtmlId}">${field.label?html}:<#if field.mandatory><span class="mandatory-indicator">${msg("form.required.fields.marker")}</span></#if></label>
    <select id="${fieldHtmlId}" name="${field.name}" tabindex="0"
            <#if field.description??>title="${field.description}"</#if>
            <#if field.control.params.size??>size="${field.control.params.size}"</#if>
            <#if field.control.params.styleClass??>class="${field.control.params.styleClass}"</#if>
            <#if field.control.params.style??>style="${field.control.params.style}"</#if>
            <#if field.disabled  && !(field.control.params.forceEditable?? && field.control.params.forceEditable == "true")>disabled="true"</#if>>
    </select>
</div>