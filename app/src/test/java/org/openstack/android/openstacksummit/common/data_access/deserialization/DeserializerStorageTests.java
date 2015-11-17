package org.openstack.android.openstacksummit.common.data_access.deserialization;

import junit.framework.Assert;

import org.junit.Test;
import org.openstack.android.openstacksummit.common.entities.Company;
import org.openstack.android.openstacksummit.common.entities.EventType;

import java.util.List;

/**
 * Created by Claudio Redi on 11/11/2015.
 */
public class DeserializerStorageTests {

    @Test
    public void add_company_storageContainsAddedCompany() {
        //Arrange
        IDeserializerStorage deserializerStorage = new DeserializerStorage();
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
        IDeserializerStorage deserializerStorage = new DeserializerStorage();
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
        IDeserializerStorage deserializerStorage = new DeserializerStorage();
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
        IDeserializerStorage deserializerStorage = new DeserializerStorage();
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
        IDeserializerStorage deserializerStorage = new DeserializerStorage();
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
        IDeserializerStorage deserializerStorage = new DeserializerStorage();
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
        IDeserializerStorage deserializerStorage = new DeserializerStorage();
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
