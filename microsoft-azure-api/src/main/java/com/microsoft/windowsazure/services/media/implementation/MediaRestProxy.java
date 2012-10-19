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

package com.microsoft.windowsazure.services.media.implementation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.microsoft.windowsazure.services.core.ServiceException;
import com.microsoft.windowsazure.services.core.ServiceFilter;
import com.microsoft.windowsazure.services.core.utils.pipeline.ClientFilterAdapter;
import com.microsoft.windowsazure.services.core.utils.pipeline.PipelineHelpers;
import com.microsoft.windowsazure.services.media.MediaContract;
import com.microsoft.windowsazure.services.media.implementation.content.AccessPolicyType;
import com.microsoft.windowsazure.services.media.implementation.content.AssetType;
import com.microsoft.windowsazure.services.media.implementation.content.LocatorRestType;
import com.microsoft.windowsazure.services.media.models.AccessPolicyInfo;
import com.microsoft.windowsazure.services.media.models.AccessPolicyPermission;
import com.microsoft.windowsazure.services.media.models.AssetInfo;
import com.microsoft.windowsazure.services.media.models.CreateAssetOptions;
import com.microsoft.windowsazure.services.media.models.CreateLocatorOptions;
import com.microsoft.windowsazure.services.media.models.ListAccessPolicyOptions;
import com.microsoft.windowsazure.services.media.models.ListAssetsOptions;
import com.microsoft.windowsazure.services.media.models.ListLocatorsOptions;
import com.microsoft.windowsazure.services.media.models.ListLocatorsResult;
import com.microsoft.windowsazure.services.media.models.ListMediaProcessorsOptions;
import com.microsoft.windowsazure.services.media.models.ListMediaProcessorsResult;
import com.microsoft.windowsazure.services.media.models.LocatorInfo;
import com.microsoft.windowsazure.services.media.models.LocatorType;
import com.microsoft.windowsazure.services.media.models.MediaProcessorInfo;
import com.microsoft.windowsazure.services.media.models.UpdateAssetOptions;
import com.microsoft.windowsazure.services.media.models.UpdateLocatorOptions;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

// TODO: Auto-generated Javadoc
/**
 * The Class MediaRestProxy.
 */
public class MediaRestProxy implements MediaContract {

    /** The channel. */
    private Client channel;

    /** The log. */
    static Log log = LogFactory.getLog(MediaContract.class);
    /** The filters. */
    ServiceFilter[] filters;

    /**
     * Instantiates a new media rest proxy.
     * 
     * @param channel
     *            the channel
     * @param authFilter
     *            the auth filter
     * @param redirectFilter
     *            the redirect filter
     * @param versionHeadersFilter
     *            the version headers filter
     */
    @Inject
    public MediaRestProxy(Client channel, OAuthFilter authFilter, RedirectFilter redirectFilter,
            VersionHeadersFilter versionHeadersFilter) {
        this.channel = channel;
        this.filters = new ServiceFilter[0];

        channel.addFilter(redirectFilter);
        channel.addFilter(authFilter);
        channel.addFilter(versionHeadersFilter);
    }

    /**
     * Instantiates a new media rest proxy.
     * 
     * @param channel
     *            the channel
     * @param filters
     *            the filters
     */
    public MediaRestProxy(Client channel, ServiceFilter[] filters) {
        this.channel = channel;
        this.filters = filters;
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.core.FilterableService#withFilter(com.microsoft.windowsazure.services.core.ServiceFilter)
     */
    @Override
    public MediaContract withFilter(ServiceFilter filter) {
        ServiceFilter[] newFilters = Arrays.copyOf(filters, filters.length + 1);
        newFilters[filters.length] = filter;
        return new MediaRestProxy(channel, newFilters);
    }

    /**
     * Gets the channel.
     * 
     * @return the channel
     */
    public Client getChannel() {
        return channel;
    }

    /**
     * Sets the channel.
     * 
     * @param channel
     *            the new channel
     */
    public void setChannel(Client channel) {
        this.channel = channel;
    }

    /**
     * Gets the resource.
     * 
     * @param entityName
     *            the entity name
     * @return the resource
     */
    private WebResource getResource(String entityName) {
        WebResource resource = getChannel().resource(entityName);
        for (ServiceFilter filter : filters) {
            resource.addFilter(new ClientFilterAdapter(filter));
        }
        return resource;
    }

    /**
     * Gets the resource.
     * 
     * @param entityType
     *            the entity type
     * @param entityId
     *            the entity id
     * @return the resource
     * @throws ServiceException
     *             the service exception
     */
    private WebResource getResource(String entityType, String entityId) throws ServiceException {
        String escapedEntityId = null;
        try {
            escapedEntityId = URLEncoder.encode(entityId, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new ServiceException(e);
        }
        String entityPath = String.format("%s(\'%s\')", entityType, escapedEntityId);

        return getResource(entityPath);
    }

    /**
     * Merge request.
     * 
     * @param <T>
     *            the generic type
     * @param entityType
     *            the entity type
     * @param entityId
     *            the entity id
     * @param c
     *            the c
     * @param requestEntity
     *            the request entity
     * @return the t
     * @throws ServiceException
     *             the service exception
     */
    private <T> T mergeRequest(String entityType, String entityId, java.lang.Class<T> c, java.lang.Object requestEntity)
            throws ServiceException {
        WebResource resource = getResource(entityType, entityId);
        WebResource.Builder builder = resource.getRequestBuilder();
        builder = builder.type(MediaType.APPLICATION_ATOM_XML).accept(MediaType.APPLICATION_ATOM_XML)
                .header("X-HTTP-Method", "MERGE");
        return builder.post(c, requestEntity);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#createAsset()
     */
    @Override
    public AssetInfo createAsset() {
        return this.createAsset(null);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#createAsset(java.lang.String, com.microsoft.windowsazure.services.media.models.CreateAssetOptions)
     */
    @Override
    public AssetInfo createAsset(CreateAssetOptions createAssetOptions) {
        WebResource resource = getResource("Assets");
        AssetType assetTypeForSubmission = new AssetType();
        if (createAssetOptions != null) {
            assetTypeForSubmission.setName(createAssetOptions.getName());
            assetTypeForSubmission.setAlternateId(createAssetOptions.getAlternateId());

            if (createAssetOptions.getOptions() != null) {
                assetTypeForSubmission.setOptions(createAssetOptions.getOptions().getCode());
            }
            if (createAssetOptions.getState() != null) {
                assetTypeForSubmission.setState(createAssetOptions.getState().getCode());
            }
        }
        return resource.type(MediaType.APPLICATION_ATOM_XML).accept(MediaType.APPLICATION_ATOM_XML)
                .post(AssetInfo.class, assetTypeForSubmission);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#getAsset(java.lang.String)
     */
    @Override
    public AssetInfo getAsset(String assetId) throws ServiceException {
        WebResource resource = getResource("Assets", assetId);
        return resource.type(MediaType.APPLICATION_ATOM_XML).accept(MediaType.APPLICATION_ATOM_XML)
                .get(AssetInfo.class);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#listAssets(com.microsoft.windowsazure.services.media.models.ListAssetsOptions)
     */
    @Override
    public List<AssetInfo> listAssets(ListAssetsOptions listAssetsOptions) {
        WebResource resource = getResource("Assets");
        if ((listAssetsOptions != null) && (listAssetsOptions.getQueryParameters() != null)) {
            resource = resource.queryParams(listAssetsOptions.getQueryParameters());
        }

        return resource.type(MediaType.APPLICATION_ATOM_XML).accept(MediaType.APPLICATION_ATOM_XML)
                .get(new GenericType<List<AssetInfo>>() {
                });
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#listAssets()
     */
    @Override
    public List<AssetInfo> listAssets() {
        ListAssetsOptions listAssetsOptions = new ListAssetsOptions();
        return listAssets(listAssetsOptions);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#updateAsset(com.microsoft.windowsazure.services.media.models.AssetInfo)
     */
    @Override
    public void updateAsset(String assetId, UpdateAssetOptions updateAssetOptions) throws ServiceException {

        AssetType updatedAssetType = new AssetType();
        updatedAssetType.setAlternateId(updateAssetOptions.getAlternateId());
        updatedAssetType.setName(updateAssetOptions.getName());
        if (updateAssetOptions.getOptions() != null) {
            updatedAssetType.setOptions(updateAssetOptions.getOptions().getCode());
        }

        if (updateAssetOptions.getState() != null) {
            updatedAssetType.setState(updateAssetOptions.getState().getCode());
        }

        ClientResponse clientResponse = mergeRequest("Assets", assetId, ClientResponse.class, updatedAssetType);
        PipelineHelpers.ThrowIfNotSuccess(clientResponse);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#deleteAsset(java.lang.String)
     */
    @Override
    public void deleteAsset(String assetId) throws ServiceException {
        getResource("Assets", assetId).delete();
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#createAccessPolicy(double, com.microsoft.windowsazure.services.media.models.CreateAccessPolicyOptions)
     */
    @Override
    public AccessPolicyInfo createAccessPolicy(String accessPolicyName, double durationInMinutes,
            EnumSet<AccessPolicyPermission> permissions) throws ServiceException {

        AccessPolicyType requestData = new AccessPolicyType().setDurationInMinutes(durationInMinutes)
                .setName(accessPolicyName).setPermissions(AccessPolicyPermission.bitsFromPermissions(permissions));

        WebResource resource = getResource("AccessPolicies");

        return resource.type(MediaType.APPLICATION_ATOM_XML).accept(MediaType.APPLICATION_ATOM_XML)
                .post(AccessPolicyInfo.class, requestData);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#getAccessPolicy(java.lang.String)
     */
    @Override
    public AccessPolicyInfo getAccessPolicy(String accessPolicyId) throws ServiceException {
        WebResource resource = getResource("AccessPolicies", accessPolicyId);
        return resource.type(MediaType.APPLICATION_ATOM_XML).accept(MediaType.APPLICATION_ATOM_XML)
                .get(AccessPolicyInfo.class);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#deleteAccessPolicy(java.lang.String)
     */
    @Override
    public void deleteAccessPolicy(String accessPolicyId) throws ServiceException {
        getResource("AccessPolicies", accessPolicyId).delete();
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#listAccessPolicies()
     */
    @Override
    public List<AccessPolicyInfo> listAccessPolicies() throws ServiceException {
        return listAccessPolicies(null);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#listAccessPolicies()
     */
    @Override
    public List<AccessPolicyInfo> listAccessPolicies(ListAccessPolicyOptions options) throws ServiceException {
        WebResource resource = getResource("AccessPolicies");

        return resource.type(MediaType.APPLICATION_ATOM_XML).accept(MediaType.APPLICATION_ATOM_XML)
                .get(new GenericType<List<AccessPolicyInfo>>() {
                });
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#createLocator(java.lang.String, java.lang.String, com.microsoft.windowsazure.services.media.models.LocatorType)
     */
    @Override
    public LocatorInfo createLocator(String accessPolicyId, String assetId, LocatorType locatorType) {
        return this.createLocator(accessPolicyId, assetId, locatorType, null);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#createLocator(java.lang.String, java.lang.String, com.microsoft.windowsazure.services.media.models.LocatorType, com.microsoft.windowsazure.services.media.models.CreateLocatorOptions)
     */
    @Override
    public LocatorInfo createLocator(String accessPolicyId, String assetId, LocatorType locatorType,
            CreateLocatorOptions createLocatorOptions) {

        LocatorRestType locatorRestType = new LocatorRestType();
        locatorRestType.setAccessPolicyId(accessPolicyId);
        locatorRestType.setAssetId(assetId);
        locatorRestType.setType(locatorType.getCode());
        if (createLocatorOptions != null) {
            if (createLocatorOptions.getExpirationDateTime() != null) {
                locatorRestType.setExpirationDateTime(createLocatorOptions.getExpirationDateTime());
            }

            if (createLocatorOptions.getStartTime() != null) {
                locatorRestType.setStartTime(createLocatorOptions.getStartTime());
            }
        }

        WebResource resource = getResource("Locators");

        return resource.type(MediaType.APPLICATION_ATOM_XML).accept(MediaType.APPLICATION_ATOM_XML)
                .post(LocatorInfo.class, locatorRestType);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#getLocator(java.lang.String)
     */
    @Override
    public LocatorInfo getLocator(String locatorId) throws ServiceException {
        WebResource resource = getResource("Locators", locatorId);
        return resource.type(MediaType.APPLICATION_ATOM_XML).accept(MediaType.APPLICATION_ATOM_XML)
                .get(LocatorInfo.class);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#listLocators()
     */
    @Override
    public ListLocatorsResult listLocators() {
        return listLocators(null);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#listLocators(com.microsoft.windowsazure.services.media.models.ListLocatorsOptions)
     */
    @Override
    public ListLocatorsResult listLocators(ListLocatorsOptions listLocatorOptions) {
        WebResource resource = getResource("Locators");

        List<LocatorInfo> locatorInfoList = resource.type(MediaType.APPLICATION_ATOM_XML)
                .accept(MediaType.APPLICATION_ATOM_XML).get(new GenericType<List<LocatorInfo>>() {
                });
        ListLocatorsResult listLocatorsResult = new ListLocatorsResult();
        listLocatorsResult.setLocatorInfos(locatorInfoList);
        return listLocatorsResult;

    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#deleteLocator(java.lang.String)
     */
    @Override
    public void deleteLocator(String locatorId) throws UniformInterfaceException, ServiceException {
        getResource("Locators", locatorId).delete();
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#updateLocator(java.lang.String, com.microsoft.windowsazure.services.media.models.UpdateLocatorOptions)
     */
    @Override
    public void updateLocator(String locatorId, UpdateLocatorOptions updateLocatorOptions) throws ServiceException {
        LocatorRestType updatedLocatorRestType = new LocatorRestType();

        updatedLocatorRestType.setId(locatorId);
        if (updateLocatorOptions != null) {
            if (updateLocatorOptions.getExpirationDateTime() != null) {
                updatedLocatorRestType.setExpirationDateTime(updateLocatorOptions.getExpirationDateTime());
            }
            if (updateLocatorOptions.getStartTime() != null) {
                updatedLocatorRestType.setStartTime(updateLocatorOptions.getStartTime());
            }
        }
        ClientResponse clientResponse = mergeRequest("Locators", locatorId, ClientResponse.class,
                updatedLocatorRestType);
        PipelineHelpers.ThrowIfNotSuccess(clientResponse);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#listMediaProcessors()
     */
    @Override
    public ListMediaProcessorsResult listMediaProcessors() {
        return this.listMediaProcessors(null);
    }

    /* (non-Javadoc)
     * @see com.microsoft.windowsazure.services.media.MediaContract#listMediaProcessors(com.microsoft.windowsazure.services.media.models.ListMediaProcessorsOptions)
     */
    @Override
    public ListMediaProcessorsResult listMediaProcessors(ListMediaProcessorsOptions listMediaProcessorsOptions) {
        WebResource resource = getResource("MediaProcessors");

        if ((listMediaProcessorsOptions != null) && (listMediaProcessorsOptions.getQueryParameters() != null)) {
            resource = resource.queryParams(listMediaProcessorsOptions.getQueryParameters());
        }

        List<MediaProcessorInfo> mediaProcessorInfoList = resource.type(MediaType.APPLICATION_ATOM_XML)
                .accept(MediaType.APPLICATION_ATOM_XML).get(new GenericType<List<MediaProcessorInfo>>() {
                });
        ListMediaProcessorsResult listMediaProcessorsResult = new ListMediaProcessorsResult();
        listMediaProcessorsResult.setMediaProcessorInfos(mediaProcessorInfoList);
        return listMediaProcessorsResult;
    }

}