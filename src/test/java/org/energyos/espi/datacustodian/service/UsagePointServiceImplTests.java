/*
 * Copyright 2013 EnergyOS.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.energyos.espi.datacustodian.service;


import org.energyos.espi.datacustodian.domain.RetailCustomer;
import org.energyos.espi.datacustodian.domain.UsagePoint;
import org.energyos.espi.datacustodian.models.atom.FeedType;
import org.energyos.espi.datacustodian.repositories.UsagePointRepository;
import org.energyos.espi.datacustodian.service.impl.UsagePointServiceImpl;
import org.energyos.espi.datacustodian.utils.ATOMMarshaller;
import org.energyos.espi.datacustodian.utils.UsagePointBuilder;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UsagePointServiceImplTests {

    private UsagePointServiceImpl service;
    private UsagePointRepository repository;

    @Before
    public void setup() {
        repository = mock(UsagePointRepository.class);
        service = new UsagePointServiceImpl();
        service.setRepository(repository);
    }

    @Test
    public void findAllByRetailCustomer_returnsUsageDataForRetailCustomer() {
        RetailCustomer customer = new RetailCustomer();

        service.findAllByRetailCustomer(customer);

        verify(repository, times(1)).findAllByRetailCustomerId(customer.getId());
    }

    @Test
    public void persist_persistsUsagePoint() {
        UsagePoint up = new UsagePoint();

        service.persist(up);

        verify(repository).persist(up);
    }

    @Test
    public void importUsagePoints_persistsUsagePoint() throws JAXBException, FileNotFoundException {
        RetailCustomer customer = new RetailCustomer();
        UsagePoint usagePoint = new UsagePoint();

        ATOMMarshaller marshaller = mock(ATOMMarshaller.class);
        UsagePointBuilder builder = mock(UsagePointBuilder.class);

        when(builder.newUsagePoint(any(FeedType.class))).thenReturn(usagePoint);

        service.setMarshaller(marshaller);
        service.setBuilder(builder);

        service.importUsagePoint(customer, mock(InputStream.class));

        verify(repository).persist(usagePoint);
        assertEquals(customer, usagePoint.getRetailCustomer());
    }
}
