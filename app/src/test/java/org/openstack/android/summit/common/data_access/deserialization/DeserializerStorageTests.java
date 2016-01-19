package org.openstack.android.summit.common.data_access.deserialization;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openstack.android.summit.BuildConfig;
import org.openstack.android.summit.common.data_access.MockSupport;
import org.openstack.android.summit.common.entities.Company;
import org.openstack.android.summit.common.entities.EventType;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Claudio Redi on 11/11/2015.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest({Realm.class})
public class DeserializerStorageTests {

    Realm mockRealm;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setup() {
        mockRealm = MockSupport.mockRealm();
    }

    @Test
    public void add_company_storageContainsAddedCompany() {
        //Arrange
        IDeserializerStorage deserializerStorage = new DeserializerStorage(mockRealm);
        int companyId = 1;
        Company company = new Company();
        company.setId(companyId);
        Company companyFromStorage;

        //Act
        deserializerStorage.add(company, Company.class);
        companyFromStorage = deserializerStorage.get(companyId, Company.class);

        //Assert
        Assert.assertEquals(company.getId(), companyFromStorage.getId());
    }

    @Test
    public void add_companyAndEventType_storageContainsBothAddedEntities() {
        //Arrange
        IDeserializerStorage deserializerStorage = new DeserializerStorage(mockRealm);
        int companyId = 1;
        Company company = new Company();
        company.setId(companyId);
        Company companyFromStorage;

        int eventTypeId = 2;
        EventType eventType = new EventType();
        eventType.setId(eventTypeId);
        EventType eventTypeFromStorage;

        //Act
        deserializerStorage.add(company, Company.class);
        deserializerStorage.add(eventType, EventType.class);
        companyFromStorage = deserializerStorage.get(companyId, Company.class);
        eventTypeFromStorage = deserializerStorage.get(eventTypeId, EventType.class);

        //Assert
        Assert.assertEquals(company.getId(), companyFromStorage.getId());
        Assert.assertEquals(eventType.getId(), eventTypeFromStorage.getId());
    }

    @Test
    public void add_twoTimesSameCompany_storageContainsSingleCompany() {
        //Arrange
        IDeserializerStorage deserializerStorage = new DeserializerStorage(mockRealm);
        int companyId = 1;
        Company company = new Company();
        company.setId(companyId);
        List<Company> companiesFromStorage;

        //Act
        deserializerStorage.add(company, Company.class);
        deserializerStorage.add(company, Company.class);
        companiesFromStorage = deserializerStorage.getAll(Company.class);

        //Assert
        Assert.assertEquals(1, companiesFromStorage.size());
    }

    @Test
    public void add_twoDifferentCompanies_storageContainsBothCompanies() {
        //Arrange
        IDeserializerStorage deserializerStorage = new DeserializerStorage(mockRealm);
        int companyId1 = 1;
        Company company1 = new Company();
        company1.setId(companyId1);

        int companyId2 = 2;
        Company company2 = new Company();
        company2.setId(companyId2);
        List<Company> companiesFromStorage;

        //Act
        deserializerStorage.add(company1, Company.class);
        deserializerStorage.add(company2, Company.class);
        companiesFromStorage = deserializerStorage.getAll(Company.class);

        //Assert
        Assert.assertEquals(2, companiesFromStorage.size());
    }

    @Test
    public void  exist_companyNotPresentOnStorage_returnsFalse() {
        //Arrange
        IDeserializerStorage deserializerStorage = new DeserializerStorage(mockRealm);
        int companyId = 1;
        Company company = new Company();
        company.setId(companyId);

        //Act
        Boolean exist = deserializerStorage.exist(company, Company.class);

        //Assert
        Assert.assertFalse(exist);
    }

    @Test
    public void  exist_companyPresentOnMemoryStorage_ReturnsTrue() {
        //Arrange
        IDeserializerStorage deserializerStorage = new DeserializerStorage(mockRealm);
        int companyId = 1;
        Company company = new Company();
        company.setId(companyId);

        //Act
        deserializerStorage.add(company, Company.class);
        Boolean exist = deserializerStorage.exist(company, Company.class);

        //Assert
        Assert.assertTrue(exist);
    }

    @Test
    public void  clear_inMemoryStorageContainsOneCompany_inMemoryStorageIsEmpty() {
        //Arrange
        IDeserializerStorage deserializerStorage = new DeserializerStorage(mockRealm);
        int companyId = 1;
        Company company = new Company();
        company.setId(companyId);

        //Act
        deserializerStorage.add(company, Company.class);
        deserializerStorage.clear();

        //Assert
        List<Company> companies = deserializerStorage.getAll(Company.class);
        Assert.assertEquals(0, companies.size());
    }
}
