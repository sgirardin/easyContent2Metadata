var documentQName = args.qname;

if (documentQName == null || documentQName == ""){
    logger.log("Invalid given QName");
    status.code = 404;
    status.message = "Invalid given QName";
    status.redirect = true;
}
model.documentMetaDataFieldsQnames = dropDownListService.retrieveAllMetadataFieldsForQName(documentQName.toString());