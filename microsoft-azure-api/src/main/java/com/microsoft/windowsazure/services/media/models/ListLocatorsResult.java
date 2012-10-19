/**
 * Copyright 2011 Microsoft Corporation
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

import java.util.List;

/**
 * The Class ListLocatorsResult.
 */
public class ListLocatorsResult {

    /** The locator infos. */
    private List<LocatorInfo> locatorInfos;

    /**
     * Gets the locator infos.
     * 
     * @return the locator infos
     */
    public List<LocatorInfo> getLocatorInfos() {
        return locatorInfos;
    }

    /**
     * Sets the locator infos.
     * 
     * @param locatorInfos
     *            the locator infos
     * @return the list locators result
     */
    public ListLocatorsResult setLocatorInfos(List<LocatorInfo> locatorInfos) {
        this.locatorInfos = locatorInfos;
        return this;
    }

}