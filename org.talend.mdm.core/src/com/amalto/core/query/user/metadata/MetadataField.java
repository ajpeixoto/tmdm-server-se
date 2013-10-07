/*
 * Copyright (C) 2006-2013 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement
 * along with this program; if not, write to Talend SA
 * 9 rue Pages 92150 Suresnes, France
 */

package com.amalto.core.query.user.metadata;

import com.amalto.core.query.user.TypedExpression;

/**
 * Represents a "metadata" field: mostly a technical field, a metadata field is not user defined but associated with the
 * MDM record.
 * @see MetadataField.Factory#getMetadataField(String)
 * @see MetadataField#SUPPORTED_METADATA
 */
public interface MetadataField {

    /**
     * Lists all metadata fields supported by this MDM.
     */
    MetadataField[] SUPPORTED_METADATA = new MetadataField[]{
            StagingError.INSTANCE,
            StagingSource.INSTANCE,
            StagingStatus.INSTANCE,
            TaskId.INSTANCE,
            Timestamp.INSTANCE
    };

    /**
     * @param path A XPath-like string.
     * @return <code>true</code> if path is used with this metadata field.
     */
    boolean matches(String path);

    /**
     * @return <code>true</code> if metadata field is read only, <code>false</code> otherwise.
     */
    boolean isReadOnly();

    /**
     * @return A {@link TypedExpression} that can be used as a left part of a {@link com.amalto.core.query.user.BinaryLogicOperator}
     * for instance.
     * @see com.amalto.core.query.user.UserQueryBuilder#where(com.amalto.core.query.user.Condition)
     */
    TypedExpression getConditionExpression();

    /**
     * @return A {@link TypedExpression} that can be used in a {@link com.amalto.core.query.user.Select}.
     * @see com.amalto.core.query.user.UserQueryBuilder#select(com.amalto.core.query.user.TypedExpression)
     */
    TypedExpression getProjectionExpression();

    class Factory {
        public static MetadataField getMetadataField(String path) {
            for (MetadataField metadataField : SUPPORTED_METADATA) {
                if (metadataField.matches(path)) {
                    return metadataField;
                }
            }
            return null;
        }
    }

}
