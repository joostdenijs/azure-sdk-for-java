package com.microsoft.windowsazure.services.media;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import com.microsoft.windowsazure.services.core.Configuration;
import com.microsoft.windowsazure.services.media.models.AccessPolicyInfo;
import com.microsoft.windowsazure.services.media.models.AssetInfo;
import com.microsoft.windowsazure.services.media.models.ListLocatorsResult;
import com.microsoft.windowsazure.services.media.models.LocatorInfo;

public abstract class IntegrationTestBase {
    protected static MediaContract service;
    protected static Configuration config;

    protected static final String testAssetPrefix = "testAsset";
    protected static final String testPolicyPrefix = "testPolicy";

    protected static final String validButNonexistAssetId = "nb:cid:UUID:00000000-0000-4a00-0000-000000000000";
    protected static final String validButNonexistAccessPolicyId = "nb:pid:UUID:bce3863e-830b-49f5-9199-7cfaff52935f";

    protected static final String invalidId = "notAValidId";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setup() throws Exception {
        config = Configuration.getInstance();
        overrideWithEnv(config, MediaConfiguration.URI);
        overrideWithEnv(config, MediaConfiguration.OAUTH_URI);
        overrideWithEnv(config, MediaConfiguration.OAUTH_CLIENT_ID);
        overrideWithEnv(config, MediaConfiguration.OAUTH_CLIENT_SECRET);
        overrideWithEnv(config, MediaConfiguration.OAUTH_SCOPE);

        service = MediaService.create(config);

        cleanupEnvironment();
    }

    protected static void overrideWithEnv(Configuration config, String key) {
        String value = System.getenv(key);
        if (value == null)
            return;

        config.setProperty(key, value);
    }

    @AfterClass
    public static void cleanup() throws Exception {
        cleanupEnvironment();
    }

    private static void cleanupEnvironment() {
        // TODO: This should be removed once cascade delete is implemented for Assets.
        // But for now, trying to delete an asset with fail if there are any 
        // existing Locators associated with it.
        removeAllTestLocators();
        removeAllTestAssets();
        removeAllTestAccessPolicies();
    }

    private static void removeAllTestAccessPolicies() {
        try {
            List<AccessPolicyInfo> policies = service.listAccessPolicies();
            for (AccessPolicyInfo policy : policies) {
                if (policy.getName().startsWith(testPolicyPrefix)) {
                    service.deleteAccessPolicy(policy.getId());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void removeAllTestAssets() {
        try {
            List<AssetInfo> listAssetsResult = service.listAssets();
            for (AssetInfo assetInfo : listAssetsResult) {
                if (assetInfo.getName().startsWith(testAssetPrefix)) {
                    service.deleteAsset(assetInfo.getId());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void removeAllTestLocators() {
        try {
            ListLocatorsResult listLocatorsResult = service.listLocators();
            for (LocatorInfo locatorInfo : listLocatorsResult.getLocatorInfos()) {
                AssetInfo ai = service.getAsset(locatorInfo.getAssetId());
                if (ai.getName().startsWith(testAssetPrefix)) {
                    service.deleteLocator(locatorInfo.getId());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface ComponentDelegate {
        void verifyEquals(String message, Object expected, Object actual);
    }

    protected <T> void verifyListResultContains(List<T> expectedInfos, Collection<T> actualInfos,
            ComponentDelegate delegate) {
        verifyListResultContains("", expectedInfos, actualInfos, delegate);
    }

    protected <T> void verifyListResultContains(String message, List<T> expectedInfos, Collection<T> actualInfos,
            ComponentDelegate delegate) {
        assertNotNull(message + ": actualInfos", actualInfos);
        assertTrue(message + ": actual size should be same size or larger than expected size",
                actualInfos.size() >= expectedInfos.size());

        List<T> orderedAndFilteredActualInfo = new ArrayList<T>();
        try {
            for (T expectedInfo : expectedInfos) {
                Method getId = expectedInfo.getClass().getMethod("getId");
                for (T actualInfo : actualInfos) {
                    if (((String) getId.invoke(actualInfo)).equals(getId.invoke(expectedInfo))) {
                        orderedAndFilteredActualInfo.add(actualInfo);
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            // Don't worry about problems here.
            e.printStackTrace();
        }

        assertEquals(message + ": actual filtered size should be same as expected size", expectedInfos.size(),
                orderedAndFilteredActualInfo.size());

        if (delegate != null) {
            for (int i = 0; i < expectedInfos.size(); i++) {
                delegate.verifyEquals(message + ": orderedAndFilteredActualInfo " + i, expectedInfos.get(i),
                        orderedAndFilteredActualInfo.get(i));
            }
        }
    }
}