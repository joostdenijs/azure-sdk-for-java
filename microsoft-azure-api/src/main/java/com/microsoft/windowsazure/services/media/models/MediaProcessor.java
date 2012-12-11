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

import javax.ws.rs.core.MultivaluedMap;

import com.microsoft.windowsazure.services.media.implementation.entities.DefaultListOperation;
import com.microsoft.windowsazure.services.media.implementation.entities.EntityListOperation;
import com.sun.jersey.api.client.GenericType;

/**
 * Entity operations for Media processors
 * 
 */
public class MediaProcessor {

    private static final String ENTITY_SET = "MediaProcessors";

    private MediaProcessor() {
    }

    /**
     * Create an operation to list all Media processors
     * 
     * @return the list operation
     */
    public static EntityListOperation<MediaProcessorInfo> list() {
        return new DefaultListOperation<MediaProcessorInfo>(ENTITY_SET,
                new GenericType<ListResult<MediaProcessorInfo>>() {
                });
    }

    /**
     * Create an operation to list all media processors that satisfy the given query parameters
     * 
     * @param queryParameters
     *            query parameters to pass with the request
     * @return the list operation
     */
    public static EntityListOperation<MediaProcessorInfo> list(MultivaluedMap<String, String> queryParameters) {
        return new DefaultListOperation<MediaProcessorInfo>(ENTITY_SET,
                new GenericType<ListResult<MediaProcessorInfo>>() {
                }, queryParameters);
    }
}