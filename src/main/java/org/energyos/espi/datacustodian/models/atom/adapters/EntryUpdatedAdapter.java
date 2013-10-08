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

package org.energyos.espi.datacustodian.models.atom.adapters;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.energyos.espi.datacustodian.models.atom.DateTimeType;
import org.energyos.espi.datacustodian.models.atom.ObjectFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.ISOChronology;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class EntryUpdatedAdapter extends XmlAdapter<JAXBElement<DateTimeType>, DateTime> {
    @Override
    public DateTime unmarshal(JAXBElement<DateTimeType> v) throws Exception {
        return new DateTime(((DateTimeType)v.getValue()).getValue().toGregorianCalendar(), ISOChronology.getInstance(DateTimeZone.UTC));
    }

    @Override
    public JAXBElement<DateTimeType> marshal(DateTime v) throws Exception {
        if (v == null) {
            return null;
        }

        DateTimeType dateTimeType = new DateTimeType();
        XMLGregorianCalendarImpl xmlGregorianCalendar = new XMLGregorianCalendarImpl(v.toGregorianCalendar());
        dateTimeType.setValue(xmlGregorianCalendar);

        return new ObjectFactory().createEntryTypeUpdated(dateTimeType);
    }
}
