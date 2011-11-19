package com.microsoft.windowsazure.services.blob.client;

/**
 * RESERVED FOR INTERNAL USE. Describes actions that may be used for writing to a page blob or clearing a set of pages.
 * 
 * Copyright (c)2011 Microsoft. All rights reserved.
 */
enum PageOperationType {
    /**
     * Update the page with new data.
     */
    UPDATE,

    /**
     * Clear the page.
     */
    CLEAR
}