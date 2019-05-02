package org.openstack.android.summit.common;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.openstack.android.summit.common.DTOs.NamedDTO;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by Claudio Redi on 1/19/2016.
 */
@RunWith(RobolectricTestRunner.class)
public class NavigationParametersStoreTests {
    @Test
    public void put_stringValue_popReturnsCorrectInstance() {
        // Arrange
        NavigationParametersStore navigationParametersStore = new NavigationParametersStore();
        String key = "test_key";
        String value = "test_value";

        // Act
        navigationParametersStore.put(key, value);
        String valueFromStore = navigationParametersStore.pop(key, String.class);

        // Assert
        Assert.assertEquals(value, valueFromStore);
    }

    @Test
    public void put_NamedDTOInstance_popReturnsCorrectInstance() {
        // Arrange
        NavigationParametersStore navigationParametersStore = new NavigationParametersStore();
        String key = "test_key";
        NamedDTO value = new NamedDTO();

        // Act
        navigationParametersStore.put(key, value);
        NamedDTO valueFromStore = navigationParametersStore.pop(key, NamedDTO.class);

        // Assert
        Assert.assertEquals(value, valueFromStore);
    }

    @Test
    public void put_NamedDTOInstanceAnThenOverrideWithAnotherValue_popReturnsInstancePutOnSecondCall() {
        // Arrange
        NavigationParametersStore navigationParametersStore = new NavigationParametersStore();
        String key = "test_key";
        NamedDTO value1 = new NamedDTO();
        NamedDTO value2 = new NamedDTO();

        // Act
        navigationParametersStore.put(key, value1);
        navigationParametersStore.put(key, value2);
        NamedDTO valueFromStore = navigationParametersStore.pop(key, NamedDTO.class);

        // Assert
        Assert.assertEquals(value2, valueFromStore);
    }

    @Test
    public void get_stringValue_keyIsRemovedFromStoreAfterPop() {
        // Arrange
        NavigationParametersStore navigationParametersStore = new NavigationParametersStore();
        String key = "test_key";
        String value = "test_value";

        // Act
        navigationParametersStore.put(key, value);
        String valueFromStore = navigationParametersStore.pop(key, String.class);
        valueFromStore = navigationParametersStore.pop(key, String.class);

        // Assert
        Assert.assertNull(valueFromStore);
    }
}
