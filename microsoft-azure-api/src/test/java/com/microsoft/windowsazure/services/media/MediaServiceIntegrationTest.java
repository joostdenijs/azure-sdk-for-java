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

package com.microsoft.windowsazure.services.media;

import static org.junit.Assert.*;

import org.junit.Test;

import com.microsoft.windowsazure.services.core.ServiceException;
import com.microsoft.windowsazure.services.media.models.ListMediaProcessorsOptions;
import com.microsoft.windowsazure.services.media.models.ListMediaProcessorsResult;

public class MediaServiceIntegrationTest extends IntegrationTestBase {

    @Test
    public void listMediaProcessorsSuccess() throws ServiceException {
        // Arrange 

        // Act
        ListMediaProcessorsResult listMediaProcessorsResult = service.listMediaProcessors();

        // Assert
        assertNotNull(listMediaProcessorsResult);
        assertTrue(listMediaProcessorsResult.getMediaProcessorInfos().size() > 0);
    }

    @Test
    public void listMediaProcessorWithOptionSuccess() throws ServiceException {
        // Arrange
        ListMediaProcessorsOptions listMediaProcessorsOptions = new ListMediaProcessorsOptions();

        // Act
        ListMediaProcessorsResult listMediaProcessorsResult = service.listMediaProcessors(listMediaProcessorsOptions);

        // Assert
        assertNotNull(listMediaProcessorsResult);
        assertTrue(listMediaProcessorsResult.getMediaProcessorInfos().size() > 0);
    }

}