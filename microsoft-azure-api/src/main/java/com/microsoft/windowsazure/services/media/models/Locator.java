/**
 * Copyright 2012 Microsoft Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microsoft.windowsazure.services.media.models;

import java.util.Date;

import javax.ws.rs.core.MultivaluedMap;

import com.microsoft.windowsazure.services.media.implementation.content.LocatorRestType;
import com.microsoft.windowsazure.services.media.implementation.entities.DefaultDeleteOperation;
import com.microsoft.windowsazure.services.media.implementation.entities.DefaultGetOperation;
import com.microsoft.windowsazure.services.media.implementation.entities.DefaultListOperation;
import com.microsoft.windowsazure.services.media.implementation.entities.EntityCreationOperation;
import com.microsoft.windowsazure.services.media.implementation.entities.EntityDeleteOperation;
import com.microsoft.windowsazure.services.media.implementation.entities.EntityGetOperation;
import com.microsoft.windowsazure.services.media.implementation.entities.EntityListOperation;
import com.microsoft.windowsazure.services.media.implementation.entities.EntityOperationBase;
import com.microsoft.windowsazure.services.media.implementation.entities.EntityOperationSingleResultBase;
import com.microsoft.windowsazure.services.media.implementation.entities.EntityUpdateOperation;
import com.sun.jersey.api.client.GenericType;

/**
 * Implementation of Locator entity.
 */
public class Locator {

    /** The Constant ENTITY_SET. */
    private final static String ENTITY_SET = "Locators";

    /**
     * Instantiates a new locator.
     */
    private Locator() {
    }

    /**
     * Create an operation to create a new locator entity.
     * 
     * @param accessPolicyId
     *            id of access policy for locator
     * @param assetId
     *            id of asset for locator
     * @param locatorType
     *            locator type
     * @return the operation
     */
    public static Creator create(String accessPolicyId, String assetId, LocatorType locatorType) {
        return new Creator(accessPolicyId, assetId, locatorType);
    }

    /**
     * The Class Creator.
     */
    public static class Creator extends EntityOperationSingleResultBase<LocatorInfo> implements
            EntityCreationOperation<LocatorInfo> {

        /** The access policy id. */
        private final String accessPolicyId;

        /** The asset id. */
        private final String assetId;

        /** The base uri. */
        private String baseUri;

        /** The content access token. */
        private String contentAccessComponent;

        /** The expiration date time. */
        private Date expirationDateTime;

        /** The locator type. */
        private final LocatorType locatorType;

        /** The path. */
        private String path;

        /** The start date time. */
        private Date startDateTime;

        /** The id. */
        private String id;

        /**
         * Instantiates a new creator.
         * 
         * @param accessPolicyId
         *            the access policy id
         * @param assetId
         *            the asset id
         * @param locatorType
         *            the locator type
         */
        protected Creator(String accessPolicyId, String assetId, LocatorType locatorType) {
            super(ENTITY_SET, LocatorInfo.class);
            this.accessPolicyId = accessPolicyId;
            this.assetId = assetId;
            this.locatorType = locatorType;
        }

        /* (non-Javadoc)
         * @see com.microsoft.windowsazure.services.media.implementation.entities.EntityCreationOperation#getRequestContents()
         */
        @Override
        public Object getRequestContents() {
            return new LocatorRestType().setId(id).setAccessPolicyId(accessPolicyId).setAssetId(assetId)
                    .setStartTime(startDateTime).setExpirationDateTime(expirationDateTime)
                    .setType(locatorType.getCode()).setBaseUri(baseUri)
                    .setContentAccessComponent(contentAccessComponent).setPath(path);
        }

        /**
         * Sets the base uri.
         * 
         * @param baseUri
         *            the base uri
         * @return the creator
         */
        public Creator setBaseUri(String baseUri) {
            this.baseUri = baseUri;
            return this;
        }

        /**
         * Sets the path.
         * 
         * @param path
         *            the path
         * @return the creator
         */
        public Creator setPath(String path) {
            this.path = path;
            return this;
        }

        /**
         * Set the date and time for when the locator starts to be available.
         * 
         * @param startDateTime
         *            The date/time
         * @return The creator instance (for function chaining)
         */
        public Creator setStartDateTime(Date startDateTime) {
            this.startDateTime = startDateTime;
            return this;
        }

        /**
         * Set the date and time at which the locator will expire.
         * 
         * @param expirationDateTime
         *            Expiration date and time
         * @return The creator instance (for function chaining)
         */
        public Creator setExpirationDateTime(Date expirationDateTime) {
            this.expirationDateTime = expirationDateTime;
            return this;
        }

        /**
         * Sets the content access component.
         * 
         * @param contentAccessComponent
         *            the content access component
         * @return The creator instance
         */
        public Creator setContentAccessComponent(String contentAccessComponent) {
            this.contentAccessComponent = contentAccessComponent;
            return this;
        }

        /**
         * Sets the id.
         * 
         * @param id
         *            the id
         * @return the entity creation operation
         */
        public EntityCreationOperation<LocatorInfo> setId(String id) {
            this.id = id;
            return this;
        }
    }

    /**
     * Create an operation to get the given locator.
     * 
     * @param locatorId
     *            id of locator to retrieve
     * @return the get operation
     */
    public static EntityGetOperation<LocatorInfo> get(String locatorId) {
        return new DefaultGetOperation<LocatorInfo>(ENTITY_SET, locatorId, LocatorInfo.class);
    }

    /**
     * Create an operation to list all locators.
     * 
     * @return the list operation
     */
    public static EntityListOperation<LocatorInfo> list() {
        return new DefaultListOperation<LocatorInfo>(ENTITY_SET, new GenericType<ListResult<LocatorInfo>>() {
        });
    }

    /**
     * Create an operation to list all locators matching the given query parameters.
     * 
     * @param queryParameters
     *            query parameters to send with the request
     * @return the list operation
     */
    public static EntityListOperation<LocatorInfo> list(MultivaluedMap<String, String> queryParameters) {
        return new DefaultListOperation<LocatorInfo>(ENTITY_SET, new GenericType<ListResult<LocatorInfo>>() {
        }, queryParameters);
    }

    /**
     * Create an operation to update the given locator.
     * 
     * @param locatorId
     *            id of locator to update
     * @return the update operation
     */
    public static Updater update(String locatorId) {
        return new Updater(locatorId);
    }

    /**
     * The Class Updater.
     */
    public static class Updater extends EntityOperationBase implements EntityUpdateOperation {

        /** The start date time. */
        private Date startDateTime;

        /** The expiration date time. */
        private Date expirationDateTime;

        /**
         * Instantiates a new updater.
         * 
         * @param locatorId
         *            the locator id
         */
        public Updater(String locatorId) {
            super(new EntityOperationBase.EntityIdUriBuilder(ENTITY_SET, locatorId));
        }

        /* (non-Javadoc)
         * @see com.microsoft.windowsazure.services.media.implementation.entities.EntityUpdateOperation#getRequestContents()
         */
        @Override
        public Object getRequestContents() {
            return new LocatorRestType().setStartTime(startDateTime).setExpirationDateTime(expirationDateTime);
        }

        /**
         * Set when the locator will become available.
         * 
         * @param startDateTime
         *            the date & time
         * @return Updater instance
         */
        public Updater setStartDateTime(Date startDateTime) {
            this.startDateTime = startDateTime;
            return this;
        }

        /**
         * Set when the locator will expire.
         * 
         * @param expirationDateTime
         *            the expiration date & time
         * @return Updater instance
         */
        public Updater setExpirationDateTime(Date expirationDateTime) {
            this.expirationDateTime = expirationDateTime;
            return this;
        }

    }

    /**
     * Create an operation to delete the given locator.
     * 
     * @param locatorId
     *            id of locator to delete
     * @return the operation
     */
    public static EntityDeleteOperation delete(String locatorId) {
        return new DefaultDeleteOperation(ENTITY_SET, locatorId);
    }
}