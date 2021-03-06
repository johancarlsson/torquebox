package org.torquebox.core.datasource.db;

import java.util.Map;

import org.jboss.jca.common.api.metadata.ds.DsSecurity;
import org.jboss.jca.common.api.metadata.ds.Validation;
import org.jboss.jca.common.api.validator.ValidateException;
import org.torquebox.core.datasource.DatabaseMetaData;

public interface Adapter {
    
    String getId();
    String getRequirePath();
    String[] getNames(); 
    
    String getDriverClassName();
    String getDataSourceClassName();
    
    DsSecurity getSecurityFor(DatabaseMetaData dbMeta) throws ValidateException;
    Map<String,String> getPropertiesFor(DatabaseMetaData dbMeta);
    Validation getValidationFor(DatabaseMetaData dbMeta) throws ValidateException;
    

}
