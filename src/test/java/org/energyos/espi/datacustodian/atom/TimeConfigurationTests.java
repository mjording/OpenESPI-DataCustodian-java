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

package org.energyos.espi.datacustodian.atom;

import com.sun.syndication.io.FeedException;
import org.energyos.espi.datacustodian.domain.TimeConfiguration;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static org.energyos.espi.datacustodian.utils.factories.EspiFactory.newTimeConfigurationWithUsagePoint;
import static org.junit.Assert.assertEquals;

public class TimeConfigurationTests {

    private TimeConfigurationEntry entry;

    @Before
    public void setup() throws JAXBException, FeedException {
        TimeConfiguration timeConfiguration = newTimeConfigurationWithUsagePoint();
        timeConfiguration.setId(1L);

        entry = new TimeConfigurationEntry(timeConfiguration);
    }

    @Test
    public void selfHref() {
        assertEquals("LocalTimeParameters/1", entry.getSelfHref());
    }

    @Test
    public void upHref() {
        assertEquals("LocalTimeParameters", entry.getUpHref());
    }
}