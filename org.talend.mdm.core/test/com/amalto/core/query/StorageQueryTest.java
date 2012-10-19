// ============================================================================
//
// Copyright (C) 2006-2011 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

package com.amalto.core.query;

import com.amalto.core.metadata.ComplexTypeMetadata;
import com.amalto.core.metadata.FieldMetadata;
import com.amalto.core.query.user.*;
import com.amalto.core.storage.StorageResults;
import com.amalto.core.storage.record.DataRecord;
import com.amalto.core.storage.record.DataRecordReader;
import com.amalto.core.storage.record.DataRecordXmlWriter;
import com.amalto.core.storage.record.XmlStringDataRecordReader;
import com.amalto.core.storage.record.metadata.DataRecordMetadata;
import com.amalto.xmlserver.interfaces.IWhereItem;
import com.amalto.xmlserver.interfaces.WhereAnd;
import com.amalto.xmlserver.interfaces.WhereCondition;

import java.io.*;
import java.util.*;

import static com.amalto.core.query.user.UserQueryBuilder.*;

@SuppressWarnings("nls")
public class StorageQueryTest extends StorageTestCase {

    private void populateData() {
        DataRecordReader<String> factory = new XmlStringDataRecordReader();

        List<DataRecord> allRecords = new LinkedList<DataRecord>();
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                country,
                                "<Country><id>1</id><creationDate>2010-10-10</creationDate><creationTime>2010-10-10T00:00:01</creationTime><name>France</name></Country>"));
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                country,
                                "<Country><id>2</id><creationDate>2011-10-10</creationDate><creationTime>2011-10-10T01:01:01</creationTime><name>USA</name></Country>"));
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                address,
                                "<Address><id>1</id><enterprise>false</enterprise><Street>Street1</Street><ZipCode>10000</ZipCode><City>City</City><country>[1]</country></Address>"));
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                address,
                                "<Address><id>1</id><enterprise>true</enterprise><Street>Street1</Street><ZipCode>10000</ZipCode><City>City</City><country>[2]</country></Address>"));
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                address,
                                "<Address><id>2</id><enterprise>true</enterprise><Street>Street2</Street><ZipCode>10000</ZipCode><City>City</City><country>[2]</country></Address>"));
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                address,
                                "<Address><id>3</id><enterprise>false</enterprise><Street>Street3</Street><ZipCode>10000</ZipCode><City>City</City><country>[1]</country></Address>"));
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                address,
                                "<Address><id>4</id><enterprise>false</enterprise><Street>Street3</Street><ZipCode>10000</ZipCode><City>City</City><OptionalCity>City2</OptionalCity><country>[1]</country></Address>"));
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                person,
                                "<Person><id>1</id><score>130000.00</score><lastname>Dupond</lastname><resume>[EN:my splendid resume, splendid isn't it][FR:mon magnifique resume, n'est ce pas ?]</resume><middlename>John</middlename><firstname>Julien</firstname><addresses><address>[2][true]</address><address>[1][false]</address></addresses><age>10</age><Status>Employee</Status><Available>true</Available></Person>"));
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                person,
                                "<Person><id>2</id><score>170000.00</score><lastname>Dupont</lastname><middlename>John</middlename><firstname>Robert-Julien</firstname><addresses><address>[1][false]</address><address>[2][true]</address></addresses><age>20</age><Status>Customer</Status><Available>false</Available></Person>"));
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                person,
                                "<Person><id>3</id><score>200000.00</score><lastname>Leblanc</lastname><middlename>John</middlename><firstname>Juste</firstname><addresses><address>[3][false]</address><address>[1][false]</address></addresses><age>30</age><Status>Friend</Status></Person>"));
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                a,
                                "<A><id>1</id><textA>TextA</textA><nestedB><text>Text</text></nestedB></A>"));
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                a,
                                "<A><id>2</id><textA>TextA</textA><nestedB><text>Text</text></nestedB><refA>[1]</refA></A>"));
        allRecords.add(factory.read(1, repository, supplier, "<Supplier>\n" + "    <Id>1</Id>\n"
                + "    <SupplierName>Renault</SupplierName>\n" + "    <Contact>" + "        <Name>Jean Voiture</Name>\n"
                + "        <Phone>33123456789</Phone>\n" + "        <Email>test@test.org</Email>\n" + "    </Contact>\n"
                + "</Supplier>"));
        allRecords.add(factory.read(1, repository, supplier, "<Supplier>\n" + "    <Id>2</Id>\n"
                + "    <SupplierName>Starbucks Talend</SupplierName>\n" + "    <Contact>" + "        <Name>Jean Cafe</Name>\n"
                + "        <Phone>33234567890</Phone>\n" + "        <Email>test@testfactory.org</Email>\n" + "    </Contact>\n"
                + "</Supplier>"));
        allRecords.add(factory.read(1, repository, supplier, "<Supplier>\n" + "    <Id>3</Id>\n"
                + "    <SupplierName>Talend</SupplierName>\n" + "    <Contact>" + "        <Name>Jean Paul</Name>\n"
                + "        <Phone>33234567890</Phone>\n" + "        <Email>test@talend.com</Email>\n" + "    </Contact>\n"
                + "</Supplier>"));
        allRecords.add(factory.read(1, repository, productFamily, "<ProductFamily>\n" + "    <Id>1</Id>\n"
                + "    <Name>Product family #1</Name>\n" + "</ProductFamily>"));
        allRecords.add(factory.read(1, repository, productFamily, "<ProductFamily>\n" + "    <Id>2</Id>\n"
                + "    <Name>Product family #2</Name>\n" + "</ProductFamily>"));
        allRecords.add(factory.read(1, repository, product, "<Product>\n" + "    <Id>1</Id>\n"
                + "    <Name>Product name</Name>\n" + "    <ShortDescription>Short description word</ShortDescription>\n"
                + "    <LongDescription>Long description</LongDescription>\n" + "    <Price>10</Price>\n" + "    <Features>\n"
                + "        <Sizes>\n" + "            <Size>Small</Size>\n" + "            <Size>Medium</Size>\n"
                + "            <Size>Large</Size>\n" + "        </Sizes>\n" + "        <Colors>\n"
                + "            <Color>Blue</Color>\n" + "            <Color>Red</Color>\n" + "        </Colors>\n"
                + "    </Features>\n" + "    <Status>Pending</Status>\n" + "    <Family>[2]</Family>\n"
                + "    <Supplier>[1]</Supplier>\n" + "</Product>"));
        allRecords.add(factory.read(1, repository, product, "<Product>\n" + "    <Id>2</Id>\n" + "    <Name>Renault car</Name>\n"
                + "    <ShortDescription>A car</ShortDescription>\n"
                + "    <LongDescription>Long description 2</LongDescription>\n" + "    <Price>10</Price>\n" + "    <Features>\n"
                + "        <Sizes>\n" + "            <Size>Large</Size>\n" + "        </Sizes>\n" + "        <Colors>\n"
                + "            <Color>Blue 2</Color>\n" + "            <Color>Blue 1</Color>\n"
                + "            <Color>Klein blue2</Color>\n" + "        </Colors>\n" + "    </Features>\n"
                + "    <Family>[1]</Family>\n" + "    <Status>Pending</Status>\n" + "    <Supplier>[2]</Supplier>\n"
                + "    <Supplier>[1]</Supplier>\n" + "</Product>"));
        try {
            storage.begin();
            storage.update(allRecords);
            storage.commit();
        } finally {
            storage.end();
        }

    }

    @Override
    public void setUp() throws Exception {
        populateData();
        userSecurity.setActive(false); // Not testing security here
    }

    @Override
    public void tearDown() throws Exception {
        try {
            storage.begin();
            {
                UserQueryBuilder qb = from(person);
                storage.delete(qb.getSelect());

                qb = from(address);
                storage.delete(qb.getSelect());

                qb = from(country);
                storage.delete(qb.getSelect());

            }
            storage.commit();
        } finally {
            storage.end();
        }
    }

    public void testXmlSerialization() {
        UserQueryBuilder qb = from(person).where(eq(person.getField("id"), "1"));

        StorageResults results = storage.fetch(qb.getSelect());
        DataRecordXmlWriter writer = new DataRecordXmlWriter();
        try {
            String expectedXml = "<Person><id>1</id><firstname>Julien</firstname><middlename>John</middlename><lastname>"
                    + "Dupond</lastname><resume>[EN:my splendid resume, splendid isn&apos;t it][FR:mon magnifique resume, n&apos;est ce pas ?]</resume>"
                    + "<age>10</age><score>130000.00</score><Available>true</Available><addresses><address>[2][true]</address><address>"
                    + "[1][false]</address></addresses><Status>Employee</Status></Person>";
            String expectedXml2 = "<Person><id>1</id><firstname>Julien</firstname><middlename>John</middlename><lastname>"
                    + "Dupond</lastname><resume>[EN:my splendid resume, splendid isn&apos;t it][FR:mon magnifique resume, n&apos;est ce pas ?]</resume>"
                    + "<age>10</age><score>130000</score><Available>true</Available><addresses><address>[2][true]</address><address>"
                    + "[1][false]</address></addresses><Status>Employee</Status></Person>";
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            for (DataRecord result : results) {
                try {
                    writer.write(result, output);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            String actual = new String(output.toByteArray());
            if (!"Oracle".equalsIgnoreCase(DATABASE)) {
                assertEquals(expectedXml, actual);
            } else {
                assertEquals(expectedXml2, actual);
            }
        } finally {
            results.close();
        }

    }

    public void testSelectId() throws Exception {
        List<FieldMetadata> keyFields = person.getKeyFields();
        assertEquals(1, keyFields.size());
        FieldMetadata keyField = keyFields.get(0);

        UserQueryBuilder qb = from(person).select(person.getField("id"));

        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getSize());
            assertEquals(3, results.getCount());
            for (DataRecord result : results) {
                assertNotNull(result.get(keyField));
            }
        } finally {
            results.close();
        }
    }

    public void testSelectById() throws Exception {
        List<FieldMetadata> keyFields = person.getKeyFields();
        assertEquals(1, keyFields.size());
        FieldMetadata keyField = keyFields.get(0);

        UserQueryBuilder qb = from(person).where(eq(person.getField("id"), "1"));

        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
            for (DataRecord result : results) {
                assertNotNull(result.get(keyField));
            }
        } finally {
            results.close();
        }
    }

    public void testSelectByIdWithProjection() throws Exception {
        List<FieldMetadata> keyFields = person.getKeyFields();
        assertEquals(1, keyFields.size());
        FieldMetadata keyField = keyFields.get(0);

        UserQueryBuilder qb = from(person).select(person.getField("id")).where(eq(person.getField("id"), "1"));

        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
            for (DataRecord result : results) {
                assertNotNull(result.get(keyField));
            }
        } finally {
            results.close();
        }
    }

    public void testOrderByASC() throws Exception {
        // Test ASC direction
        FieldMetadata personLastName = person.getField("lastname");
        UserQueryBuilder qb = from(person).orderBy(personLastName, OrderBy.Direction.ASC);
        String[] ascExpectedValues = {"Dupond", "Dupont", "Leblanc"};

        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getSize());
            assertEquals(3, results.getCount());

            int i = 0;
            for (DataRecord result : results) {
                assertEquals(ascExpectedValues[i++], result.get(personLastName));
            }

        } finally {
            results.close();
        }
    }

    public void testOrderByDESC() throws Exception {
        FieldMetadata personLastName = person.getField("lastname");
        UserQueryBuilder qb = from(person).orderBy(personLastName, OrderBy.Direction.DESC);
        String[] descExpectedValues = {"Leblanc", "Dupont", "Dupond"};

        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getSize());
            assertEquals(3, results.getCount());

            int i = 0;
            for (DataRecord result : results) {
                assertEquals(descExpectedValues[i++], result.get(personLastName));
            }

        } finally {
            results.close();
        }
    }

    public void testNoConditionQuery() throws Exception {
        UserQueryBuilder qb = from(person);
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getSize());
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }

        qb = from(address);
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(5, results.getSize());
            assertEquals(5, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testEqualsCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(eq(person.getField("lastname"), "Dupond"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }

        qb = from(address).where(eq(address.getField("Street"), "Street1"));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testEqualsDateCondition() throws Exception {
        UserQueryBuilder qb = from(country).where(eq(country.getField("creationDate"), "2010-10-10"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testEqualsTimeCondition() throws Exception {
        UserQueryBuilder qb = from(country).where(eq(country.getField("creationTime"), "2010-10-10T00:00:01"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testEqualsBooleanCondition() throws Exception {
        UserQueryBuilder qb = from(address).where(eq(address.getField("enterprise"), "true"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }

        qb = from(address).where(eq(address.getField("enterprise"), "false"));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getSize());
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testNotEqualsCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(neq(person.getField("lastname"), "Dupond"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testGreaterThanCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(gt(person.getField("age"), "10"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testGreaterThanDateCondition() throws Exception {
        UserQueryBuilder qb = from(country).where(gt(country.getField("creationDate"), "2000-01-01"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testGreaterThanTimeCondition() throws Exception {
        UserQueryBuilder qb = from(country).where(gt(country.getField("creationTime"), "2000-01-01T00:00:00"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testGreaterThanDecimalCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(gt(person.getField("score"), "100000"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getSize());
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testLessThanCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(lt(person.getField("age"), "20"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testLessThanDateCondition() throws Exception {
        UserQueryBuilder qb = from(country).where(lt(country.getField("creationDate"), "2020-01-01"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testLessThanTimeCondition() throws Exception {
        UserQueryBuilder qb = from(country).where(lt(country.getField("creationTime"), "2020-01-01T00:00:00"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testLessThanDecimalCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(lt(person.getField("score"), "1000000"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getSize());
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testGreaterThanEqualsCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(gte(person.getField("age"), "10"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getSize());
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testIntervalCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(gte(person.getField("age"), "10")).where(lte(person.getField("age"), "30"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getSize());
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testGreaterThanEqualsDateCondition() throws Exception {
        UserQueryBuilder qb = from(country).where(gte(country.getField("creationDate"), "2011-10-10"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testGreaterThanEqualsTimeCondition() throws Exception {
        UserQueryBuilder qb = from(country).where(gte(country.getField("creationTime"), "2011-10-10T00:00:00"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testGreaterThanEqualsDecimalCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(gte(person.getField("score"), "170000"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testLessThanEqualsCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(lte(person.getField("age"), "20"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testLessThanEqualsDateCondition() throws Exception {
        UserQueryBuilder qb = from(country).where(lte(country.getField("creationDate"), "2010-10-10"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testLessThanEqualsTimeCondition() throws Exception {
        UserQueryBuilder qb = from(country).where(lte(country.getField("creationTime"), "2010-10-10T00:00:01"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testLessThanEqualsDecimalCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(lte(person.getField("score"), "170000"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testStartsWithCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(startsWith(person.getField("firstname"), "Ju"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testContainsCondition() throws Exception {
        UserQueryBuilder qb = from(person).where(contains(person.getField("lastname"), "Dupo"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }

        qb = from(address).where(contains(address.getField("Street"), "Street"));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(5, results.getSize());
            assertEquals(5, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testConditionOr() throws Exception {
        UserQueryBuilder qb = from(person).where(
                or(eq(person.getField("lastname"), "Dupond"), eq(person.getField("firstname"), "Robert-Julien")));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testConditionAnd() throws Exception {
        UserQueryBuilder qb = from(person).where(
                and(eq(person.getField("lastname"), "Dupond"), eq(person.getField("firstname"), "Robert-Damien")));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(0, results.getSize());
            assertEquals(0, results.getCount());
        } finally {
            results.close();
        }

        // Wheres are equivalent to "and" statements
        qb = from(person).where(eq(person.getField("lastname"), "Dupond")).where(
                eq(person.getField("firstname"), "Robert-Damien"));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(0, results.getSize());
            assertEquals(0, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testConditionNot() throws Exception {
        UserQueryBuilder qb = from(person).where(
                and(eq(person.getField("lastname"), "Dupond"), not(eq(person.getField("firstname"), "Robert"))));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }

        // Equivalent to the previous query (chained wheres are "and")
        qb = from(person).where(eq(person.getField("lastname"), "Dupond")).where(not(eq(person.getField("firstname"), "Robert")));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testJoinQuery() throws Exception {
        UserQueryBuilder qb = from(person).select(person.getField("firstname")).select(address.getField("Street"))
                .join(person.getField("addresses/address"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(6, results.getSize());
            assertEquals(6, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testJoinQueryWithId() throws Exception {
        UserQueryBuilder qb = from(person)
                .select(person.getField("firstname"))
                .select(address.getField("Street"))
                .where(and(eq(person.getField("id"), "1"), UserQueryHelper.NO_OP_CONDITION))
                .join(person.getField("addresses/address"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }

        qb = from(person)
                .select(person.getField("firstname"))
                .select(address.getField("Street"))
                .where(and(UserQueryHelper.NO_OP_CONDITION, eq(person.getField("id"), "1")))
                .join(person.getField("addresses/address"));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testJoinQueryNormalize() throws Exception {
        UserQueryBuilder qb = from(person)
                .select(person.getField("firstname"))
                .select(address.getField("Street"))
                .where(and(eq(person.getField("id"), "1"), UserQueryHelper.NO_OP_CONDITION))
                .join(person.getField("addresses/address"));
        Select select = qb.getSelect();
        assertTrue(select.getCondition() instanceof BinaryLogicOperator);
        Select normalizedSelect = (Select) select.normalize(); // Binary condition can be simplified because right is NO_OP_CONDITION
        assertTrue(normalizedSelect.getCondition() instanceof Compare);

        qb = from(person)
                .select(person.getField("firstname"))
                .select(address.getField("Street"))
                .where(and(UserQueryHelper.NO_OP_CONDITION, eq(person.getField("id"), "1")))
                .join(person.getField("addresses/address"));
        select = qb.getSelect();
        assertTrue(select.getCondition() instanceof BinaryLogicOperator);
        normalizedSelect = (Select) select.normalize(); // Binary condition can be simplified because right is NO_OP_CONDITION
        assertTrue(normalizedSelect.getCondition() instanceof Compare);
    }

    public void testJoinQueryUsingSingleParameterJoin() throws Exception {
        UserQueryBuilder qb = from(person).select(person.getField("firstname")).select(address.getField("Street"))
                .join(person.getField("addresses/address"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(6, results.getSize());
            assertEquals(6, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testJoinQueryWithCondition() throws Exception {
        UserQueryBuilder qb = from(person).select(person.getField("firstname")).select(address.getField("Street"))
                .join(person.getField("addresses/address")).where(eq(person.getField("lastname"), "Dupond"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testJoinQueryWithConditionAnd() throws Exception {
        UserQueryBuilder qb = from(person).select(person.getField("firstname")).select(address.getField("Street"))
                .join(person.getField("addresses/address")).where(eq(person.getField("lastname"), "Dupond"))
                .where(eq(person.getField("firstname"), "Julien"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testJoinQueryWithConditionNot() throws Exception {
        UserQueryBuilder qb = from(person).select(person.getField("firstname")).select(address.getField("Street"))
                .join(person.getField("addresses/address"))
                .where(and(eq(person.getField("lastname"), "Dupond"), not(eq(person.getField("firstname"), "Julien"))));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(0, results.getSize());
            assertEquals(0, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testDoubleJoinQuery() throws Exception {
        UserQueryBuilder qb = from(person).select(person.getField("firstname")).select(address.getField("Street"))
                .select(country.getField("name")).join(person.getField("addresses/address"))
                .join(address.getField("country"), country.getField("id"));
        StorageResults results = storage.fetch(qb.getSelect());

        try {
            assertEquals(6, results.getSize());
            assertEquals(6, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testDoubleJoinQueryWithCondition() throws Exception {
        UserQueryBuilder qb = from(person).select(person.getField("firstname")).select(address.getField("Street"))
                .select(country.getField("name")).join(person.getField("addresses/address"))
                .join(address.getField("country"), country.getField("id")).where(eq(person.getField("lastname"), "Dupond"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getSize());
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testPaging() throws Exception {
        UserQueryBuilder qb = from(person).limit(1);
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(3, results.getCount());
            for (DataRecord result : results) {
                assertNotNull(result.get("id"));
            }
        } finally {
            results.close();
        }

        //
        qb = from(person).limit(-1);
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getSize());
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }

        //
        qb = from(person).limit(1).start(1);
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }

        qb = from(person).limit(1).start(4);
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getSize());
            assertEquals(3, results.getCount());
            assertFalse(results.iterator().hasNext());
        } finally {
            results.close();
        }
    }

    public void testEnumeration() throws Exception {
        UserQueryBuilder qb = from(person).where(eq(person.getField("Status"), "Friend"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testTimestamp() throws Exception {
        UserQueryBuilder qb = from(person).where(eq(person.getField("Status"), "Friend"));
        StorageResults results = storage.fetch(qb.getSelect());

        long lastModificationTime1;
        try {
            assertEquals(1, results.getCount());
            Iterator<DataRecord> iterator = results.iterator();
            assertTrue(iterator.hasNext());
            DataRecord result = iterator.next();
            assertNotNull(result);
            DataRecordMetadata recordMetadata = result.getRecordMetadata();
            assertNotNull(recordMetadata);
            lastModificationTime1 = recordMetadata.getLastModificationTime();
            assertNotSame("0", lastModificationTime1);
        } finally {
            results.close();
        }

        DataRecordReader<String> factory = new XmlStringDataRecordReader();
        DataRecord record = factory
                .read(1,
                        repository,
                        person,
                        "<Person><id>3</id><score>200000</score><lastname>Leblanc</lastname><middlename>John</middlename><firstname>Juste</firstname><addresses><address>[3][false]</address><address>[1][false]</address></addresses><age>30</age><Status>Friend</Status></Person>");
        try {
            storage.begin();
            storage.update(record);
            storage.commit();
        } finally {
            storage.end();
        }

        qb = from(person).where(eq(person.getField("Status"), "Friend"));
        results = storage.fetch(qb.getSelect());
        long lastModificationTime2;
        try {
            assertEquals(1, results.getCount());
            Iterator<DataRecord> iterator = results.iterator();
            assertTrue(iterator.hasNext());
            DataRecord result = iterator.next();
            assertNotNull(result);
            DataRecordMetadata recordMetadata = result.getRecordMetadata();
            assertNotNull(recordMetadata);
            lastModificationTime2 = recordMetadata.getLastModificationTime();
            assertNotSame("0", lastModificationTime2);
        } finally {
            results.close();
        }

        // Now the actual timestamp test
        assertNotSame(lastModificationTime1, lastModificationTime2);
    }

    public void testAliases() throws Exception {
        long endTime = System.currentTimeMillis() + 60000;

        UserQueryBuilder qb = from(person).select(alias(timestamp(), "timestamp")).select(alias(taskId(), "taskid"))
                .selectId(person).where(gte(timestamp(), "0")).where(lte(timestamp(), String.valueOf(endTime))).limit(20)
                .start(0);

        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getCount());
            for (DataRecord result : results) {
                assertNotNull(result.get("timestamp"));
                assertNull(result.get("taskid"));
            }
        } finally {
            results.close();
        }
    }

    public void testRevision() throws Exception {
        UserQueryBuilder qb = from(person).select(alias(revision(), "revision")).selectId(person).where(gte(revision(), "1"))
                .limit(20).start(0);

        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getCount());
            for (DataRecord result : results) {
                assertNotNull(result.get("revision"));
            }
        } finally {
            results.close();
        }
    }

    public void testFKSearch() throws Exception {
        UserQueryBuilder qb = from(address)
                .selectId(address)
                .select(address.getField("country"))
                .where(eq(address.getField("country"), "[1]"));

        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testEmptyOrNull() throws Exception {
        UserQueryBuilder qb = from(address).selectId(address).where(emptyOrNull(address.getField("City")));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(0, results.getCount());
        } finally {
            results.close();
        }

        //
        qb = from(address).selectId(address).where(emptyOrNull(address.getField("OptionalCity")));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(4, results.getCount());
        } finally {
            results.close();
        }

        //
        qb = from(address).selectId(address).where(not(emptyOrNull(address.getField("OptionalCity"))));
        results = storage.fetch(qb.getSelect().normalize());
        try {
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testIsEmptyOrNullOnNonString() throws Exception {
        UserQueryBuilder qb = from(address).selectId(address).where(emptyOrNull(address.getField("enterprise")));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(0, results.getCount());
        } finally {
            results.close();
        }

        //
        qb = from(address).selectId(address).where(not(emptyOrNull(address.getField("enterprise"))));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(5, results.getCount());
        } finally {
            results.close();
        }

        //
        qb = from(country).selectId(country).where(emptyOrNull(country.getField("creationDate")));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(0, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testBoolean() throws Exception {
        UserQueryBuilder qb = from(person).selectId(person).where(eq(person.getField("Available"), "false"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(2, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testInterFieldCondition() throws Exception {
        UserQueryBuilder qb = from(person)
                .selectId(person)
                .where(lte(person.getField("id"), person.getField("score")));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testRecursiveQuery() throws Exception {
        UserQueryBuilder qb = from(a)
                .selectId(a)
                .select(a.getField("refA"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            Set<Object> expectedValues = new HashSet<Object>();
            expectedValues.add(null);
            expectedValues.add("1");
            assertEquals(2, results.getCount());
            for (DataRecord result : results) {
                Object value = result.get("refA");
                boolean wasRemoved = expectedValues.remove(value);
                assertTrue(wasRemoved);
            }
            assertEquals(0, expectedValues.size());
        } finally {
            results.close();
        }
    }

    public void testTimeStampQuery() throws Exception {
        UserQueryBuilder qb = UserQueryBuilder.from(person);
        String fieldName = "Person/../../t";
        IWhereItem item = new WhereAnd(Arrays.<IWhereItem>asList(new WhereCondition(fieldName, WhereCondition.GREATER_THAN, "1000", WhereCondition.NO_OPERATOR)));
        qb = qb.where(UserQueryHelper.buildCondition(qb, item, repository));
        Select select = qb.getSelect();
        select = (Select) select.normalize();
        Condition condition = select.getCondition();
        assertTrue(condition instanceof Compare);
        assertTrue(((Compare) condition).getLeft() instanceof Timestamp);
    }

    public void testContainsOnNumericField() throws Exception {
        UserQueryBuilder qb = UserQueryBuilder.from(address)
                .where(contains(address.getField("ZipCode"), "10000"));
        Condition condition = qb.getSelect().getCondition();
        assertTrue(condition instanceof Compare);
        assertTrue(((Compare) condition).getLeft() instanceof Field);
        assertTrue(((Compare) condition).getRight() instanceof IntegerConstant);
        assertTrue(((Compare) condition).getPredicate() == Predicate.EQUALS);

        StorageResults results = storage.fetch(qb.getSelect());
        int expected = 10000;
        for (DataRecord result : results) {
            assertEquals(expected, result.get("ZipCode"));
        }
    }

    public void testNonValueFieldAndQueryOnId() throws Exception {
        UserQueryBuilder qb = UserQueryBuilder.from(person)
                .select(person.getField("addresses"), person.getField("id"))
                .where(eq(person.getField("id"), "1"));
        StorageResults results = storage.fetch(qb.getSelect());
        for (DataRecord result : results) {
            assertEquals(1, result.get("id"));
            assertEquals("", result.get("addresses"));
        }
    }

    public void testNonValueFieldAndQueryOnValue() throws Exception {
        UserQueryBuilder qb = UserQueryBuilder.from(person)
                .select(person.getField("addresses"), person.getField("id"))
                .where(eq(person.getField("firstname"), "Juste"));
        StorageResults results = storage.fetch(qb.getSelect());
        for (DataRecord result : results) {
            assertEquals(3, result.get("id"));
            assertEquals("", result.get("addresses"));
        }
    }

    public void testRangeOnTimestamp() throws Exception {
        UserQueryBuilder qb = UserQueryBuilder.from(person)
                .where(and(gte(timestamp(), "0"), lte(timestamp(), String.valueOf(System.currentTimeMillis()))));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testCollectionClean() throws Exception {
        DataRecordReader<String> factory = new XmlStringDataRecordReader();
        DataRecord productInstance = factory.read(1, repository, product, "<Product>\n" + "    <Id>1</Id>\n"
                + "    <Name>Product name</Name>\n" + "    <ShortDescription>Short description word</ShortDescription>\n"
                + "    <LongDescription>Long description</LongDescription>\n" + "    <Price>10</Price>\n" + "    <Features>\n"
                + "        <Sizes>\n" + "            <Size>Small</Size>\n" + "            <Size>Medium</Size>\n"
                + "            <Size>Large</Size>\n" + "        </Sizes>\n" + "        <Colors>\n"
                + "            <Color>Blue</Color>\n" + "            <Color>Red</Color>\n" + "        </Colors>\n"
                + "    </Features>\n" + "    <Status>Pending</Status>\n"
                + "</Product>");
        try {
            storage.begin();
            storage.update(productInstance);
            storage.commit();
        } finally {
            storage.end();
        }

        UserQueryBuilder qb = from(product).where(eq(product.getField("Id"), "1"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getCount());
            for (DataRecord result : results) {
                Object o = result.get("Features/Colors/Color");
                assertTrue(o instanceof List);
                assertEquals(2, ((List) o).size());
            }
        } finally {
            results.close();
        }

        productInstance = factory.read(1, repository, product, "<Product>\n" + "    <Id>1</Id>\n"
                + "    <Name>Product name</Name>\n" + "    <ShortDescription>Short description word</ShortDescription>\n"
                + "    <LongDescription>Long description</LongDescription>\n" + "    <Price>10</Price>\n" + "    <Features>\n"
                + "        <Sizes>\n" + "            <Size>Small</Size>\n" + "            <Size>Medium</Size>\n"
                + "            <Size>Large</Size>\n" + "        </Sizes>\n" + "        <Colors><Color/><Color/></Colors>\n"
                + "    </Features>\n" + "    <Status>Pending</Status>\n"
                + "</Product>");
        try {
            storage.begin();
            storage.update(productInstance);
            storage.commit();
        } finally {
            storage.end();
        }

        qb = from(product).where(eq(product.getField("Id"), "1"));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getCount());
            for (DataRecord result : results) {
                Object o = result.get("Features/Colors/Color");
                assertTrue(o instanceof List);
                assertEquals(0, ((List) o).size());
            }
        } finally {
            results.close();
        }

        productInstance = factory.read(1, repository, product, "<Product>\n" + "    <Id>1</Id>\n"
                + "    <Name>Product name</Name>\n" + "    <ShortDescription>Short description word</ShortDescription>\n"
                + "    <LongDescription>Long description</LongDescription>\n" + "    <Price>10</Price>\n" + "    <Features>\n"
                + "        <Sizes>\n" + "            <Size>Small</Size>\n" + "            <Size>Medium</Size>\n"
                + "            <Size>Large</Size>\n" + "        </Sizes>\n" + "        <Colors>"
                + "            <Color>Blue</Color>\n" + "            <Color>Red</Color>\n" + "        </Colors>\n"
                + "    </Features>\n" + "    <Status>Pending</Status>\n"
                + "</Product>");
        try {
            storage.begin();
            storage.update(productInstance);
            storage.commit();
        } finally {
            storage.end();
        }

        qb = from(product).where(eq(product.getField("Id"), "1"));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getCount());
            for (DataRecord result : results) {
                Object o = result.get("Features/Colors/Color");
                assertTrue(o instanceof List);
                assertEquals(2, ((List) o).size());
            }
        } finally {
            results.close();
        }
    }

    public void testUpdateReportCreation() throws Exception {
        StringBuilder builder = new StringBuilder();
        InputStream testResource = this.getClass().getResourceAsStream("UpdateReportCreationTest.xml");
        BufferedReader reader = new BufferedReader(new InputStreamReader(testResource));
        String current;
        while ((current = reader.readLine()) != null) {
            builder.append(current);
        }

        DataRecordReader<String> dataRecordReader = new XmlStringDataRecordReader();
        DataRecord report = dataRecordReader.read(1, repository, updateReport, builder.toString());

        try {
            storage.begin();
            storage.update(report);
            storage.commit();
        } finally {
            storage.end();
        }

        UserQueryBuilder qb = from(updateReport);
        StorageResults results = storage.fetch(qb.getSelect());
        StringWriter storedDocument = new StringWriter();
        try {
            DataRecordXmlWriter writer = new DataRecordXmlWriter();
            for (DataRecord result : results) {
                writer.write(result, storedDocument);
            }
            assertEquals(builder.toString(), storedDocument.toString());
        } finally {
            results.close();
        }
    }

    public void testUpdateReportTimeStampQuery() throws Exception {
        StringBuilder builder = new StringBuilder();
        InputStream testResource = this.getClass().getResourceAsStream("UpdateReportCreationTest.xml");
        BufferedReader reader = new BufferedReader(new InputStreamReader(testResource));
        String current;
        while ((current = reader.readLine()) != null) {
            builder.append(current);
        }

        DataRecordReader<String> dataRecordReader = new XmlStringDataRecordReader();
        DataRecord report = dataRecordReader.read(1, repository, updateReport, builder.toString());
        try {
            storage.begin();
            storage.update(report);
            storage.commit();
        } finally {
            storage.end();
        }

        UserQueryBuilder qb = from(updateReport).where(gt(timestamp(), "0"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testUpdateReportTaskIdQuery() throws Exception {
        StringBuilder builder = new StringBuilder();
        InputStream testResource = this.getClass().getResourceAsStream("UpdateReportCreationTest.xml");
        BufferedReader reader = new BufferedReader(new InputStreamReader(testResource));
        String current;
        while ((current = reader.readLine()) != null) {
            builder.append(current);
        }

        DataRecordReader<String> dataRecordReader = new XmlStringDataRecordReader();
        DataRecord report = dataRecordReader.read(1, repository, updateReport, builder.toString());
        try {
            storage.begin();
            storage.update(report);
            storage.commit();
        } finally {
            storage.end();
        }

        UserQueryBuilder qb = from(updateReport).where(isNull(taskId()));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testNativeQueryWithReturn() throws Exception {
        UserQueryBuilder qb = from("SELECT * FROM PERSON;");
        StorageResults results = storage.fetch(qb.getExpression());
        assertEquals(3, results.getCount());
        assertEquals(3, results.getSize());
        for (DataRecord result : results) {
            assertNotNull(result.get("col0") != null);
        }
    }

    public void testNativeQueryWithNoReturn() throws Exception {
        UserQueryBuilder qb = from("UPDATE PERSON set x_firstname='My SQL modified firstname';");
        StorageResults results = storage.fetch(qb.getExpression());
        assertEquals(0, results.getCount());
        assertEquals(0, results.getSize());
        for (DataRecord result : results) {
            // Test iterator too (even if size is 0).
        }

        qb = from(person).where(eq(person.getField("firstname"), "Julien"));
        results = storage.fetch(qb.getExpression());
        try {
            assertEquals(0, results.getCount());
        } finally {
            results.close();
        }

        qb = from(person).where(eq(person.getField("firstname"), "My SQL modified firstname"));
        results = storage.fetch(qb.getExpression());
        try {
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testContainsWithWildcards() throws Exception {
        UserQueryBuilder qb = from(person)
                .where(contains(person.getField("firstname"), "*Ju*e"));

        Select select = qb.getSelect();
        assertNotNull(select);
        Condition condition = select.getCondition();
        assertNotNull(condition);
        assertTrue(condition instanceof Compare);
        Compare compareCondition = (Compare) condition;
        Expression right = compareCondition.getRight();
        assertTrue(right instanceof StringConstant);
        assertEquals("*Ju*e", ((StringConstant) right).getValue());

        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(3, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testMultiLingualSearch() throws Exception {
        UserQueryBuilder qb = from(person)
                .select(person.getField("resume"))
                .where(contains(person.getField("resume"), "*[EN:*splendid*]*"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }

        qb = from(person)
                .select(person.getField("resume"))
                .where(contains(person.getField("resume"), "*[FR:*magnifique*]*"));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }

        qb = from(person)
                .select(person.getField("resume"))
                .where(contains(person.getField("resume"), "*[FR:*splendid*]*"));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(0, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testSortOnXPath() throws Exception {
        UserQueryBuilder qb = from(person).selectId(person);
        TypedExpression sortField = UserQueryHelper.getField(repository, "Person", "../../i");
        qb.orderBy(sortField, OrderBy.Direction.DESC);

        StorageResults storageResults = storage.fetch(qb.getSelect());
        int[] expected = {3, 2, 1};
        int i = 0;
        for (DataRecord result : storageResults) {
            assertEquals(expected[i++], result.get("id"));
        }
    }

    public void testCompositeFKCollectionSearch() throws Exception {
        UserQueryBuilder qb = from(person)
                .selectId(person)
                .where(eq(person.getField("addresses/address"), "[3][false]"));
        StorageResults storageResults = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, storageResults.getCount());
        } finally {
            storageResults.close();
        }
    }

    public void testCompositeFKCollectionSearchWithWhereItem() throws Exception {
        UserQueryBuilder qb = UserQueryBuilder.from(person);
        String fieldName = "Person/addresses/address";
        IWhereItem item = new WhereAnd(Arrays.<IWhereItem>asList(new WhereCondition(fieldName, WhereCondition.EQUALS, "[3][false]", WhereCondition.NO_OPERATOR)));
        qb = qb.where(UserQueryHelper.buildCondition(qb, item, repository));
        StorageResults storageResults = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, storageResults.getCount());
        } finally {
            storageResults.close();
        }
    }

    public void testFKCollectionSearch() throws Exception {
        UserQueryBuilder qb = from(product)
                .selectId(product)
                .where(eq(product.getField("Supplier"), "[2]"));
        StorageResults storageResults = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, storageResults.getCount());
        } finally {
            storageResults.close();
        }
    }

    public void testFKCollectionSearchWithWhereItem() throws Exception {
        UserQueryBuilder qb = UserQueryBuilder.from(product);
        String fieldName = "Product/Supplier";
        IWhereItem item = new WhereAnd(Arrays.<IWhereItem>asList(new WhereCondition(fieldName, WhereCondition.EQUALS, "[2]", WhereCondition.NO_OPERATOR)));
        qb = qb.where(UserQueryHelper.buildCondition(qb, item, repository));
        StorageResults storageResults = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, storageResults.getCount());
        } finally {
            storageResults.close();
        }
    }

    public void testValueCollectionSearch() throws Exception {
        UserQueryBuilder qb = from(product)
                .selectId(product)
                .where(eq(product.getField("Features/Colors/Color"), "Blue"));
        StorageResults storageResults = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, storageResults.getCount());
        } finally {
            storageResults.close();
        }
    }

    public void testValueCollectionSearchWithWhereItem() throws Exception {
        UserQueryBuilder qb = UserQueryBuilder.from(product);
        String fieldName = "Product/Features/Colors/Color";
        IWhereItem item = new WhereAnd(Arrays.<IWhereItem>asList(new WhereCondition(fieldName, WhereCondition.EQUALS, "Blue", WhereCondition.NO_OPERATOR)));
        qb = qb.where(UserQueryHelper.buildCondition(qb, item, repository));
        StorageResults storageResults = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, storageResults.getCount());
        } finally {
            storageResults.close();
        }
    }

    public void testValueCollectionSearchInNested() throws Exception {
        DataRecordReader<String> factory = new XmlStringDataRecordReader();
        List<DataRecord> allRecords = new LinkedList<DataRecord>();
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                person,
                                "<Person><id>4</id><score>200000.00</score><lastname>Leblanc</lastname><middlename>John" +
                                        "</middlename><firstname>Juste</firstname><addresses><address>[3][false]" +
                                        "</address><address>[1][false]</address></addresses><age>30</age>" +
                                        "<knownAddresses><knownAddress><Street>Street 1</Street><City>City 1</City>" +
                                        "<Phone>012345</Phone></knownAddress>" +
                                        "<knownAddress><Street>Street 2</Street><City>City 2</City><Phone>567890" +
                                        "</Phone></knownAddress></knownAddresses>" +
                                        "<Status>Friend</Status></Person>"));
        storage.begin();
        storage.update(allRecords);
        storage.commit();
        UserQueryBuilder qb = from(person)
                .selectId(person)
                .where(eq(person.getField("knownAddresses/knownAddress/City"), "City 1"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getCount());
        } finally {
            results.close();
        }
        qb = from(person)
                .selectId(person)
                .where(eq(person.getField("knownAddresses/knownAddress/City"), "City 0"));
        results = storage.fetch(qb.getSelect());
        try {
            assertEquals(0, results.getCount());
        } finally {
            results.close();
        }
    }

    public void testSelectCompositeFK() throws Exception {
        ComplexTypeMetadata a1 = repository.getComplexType("a1");
        ComplexTypeMetadata a2 = repository.getComplexType("a2");

        DataRecordReader<String> factory = new XmlStringDataRecordReader();
        List<DataRecord> allRecords = new LinkedList<DataRecord>();
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                a2,
                                "<a2><subelement>1</subelement><subelement1>10</subelement1><b3>String b3</b3><b4>String b4</b4></a2>"));
        allRecords
                .add(factory
                        .read(1,
                                repository,
                                a1,
                                "<a1><subelement>1</subelement><subelement1>11</subelement1><b1>String b1</b1><b2>[1][10]</b2></a1>"));
        storage.begin();
        storage.update(allRecords);
        storage.commit();

        UserQueryBuilder qb = from(a1).selectId(a1).select(a1.getField("b1")).select(a1.getField("b2"));
        StorageResults results = storage.fetch(qb.getSelect());
        try {
            assertEquals(1, results.getCount());
            for (DataRecord result : results) {
                Object b2Value = result.get("b2");
                assertTrue(b2Value instanceof Object[]);
                Object[] b2Values = (Object[]) b2Value;
                assertEquals("1", b2Values[0]);
                assertEquals("10", b2Values[1]);
            }
        } finally {
            results.close();
        }
    }
}