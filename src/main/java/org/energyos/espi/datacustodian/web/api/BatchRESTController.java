/*
 * Copyright 2013, 2014 EnergyOS.org
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
package org.energyos.espi.datacustodian.web.api;
import com.sun.syndication.io.FeedException;

import org.energyos.espi.common.domain.Routes;
import org.energyos.espi.common.domain.RetailCustomer;
import org.energyos.espi.common.domain.Subscription;
import org.energyos.espi.common.domain.UsagePoint;
import org.energyos.espi.common.models.atom.EntryType;
import org.energyos.espi.common.service.ApplicationInformationService;
import org.energyos.espi.common.service.ExportService;
import org.energyos.espi.common.service.ImportService;
import org.energyos.espi.common.service.RetailCustomerService;
import org.energyos.espi.common.service.UsagePointService;
import org.energyos.espi.common.utils.ExportFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class BatchRESTController {

    @Autowired
    private ImportService importService;
    
    @Autowired
    private RetailCustomerService retailCustomerService;
    
    @Autowired
    private UsagePointService usagePointService;
    
    @Autowired
    private ExportService exportService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleGenericException() {}
 
    @RequestMapping(value = Routes.BATCH_UPLOAD_MY_DATA, method = RequestMethod.POST)
    public void upload(HttpServletResponse response, 
    		@PathVariable Long retailCustomerId,
    		@RequestParam Map<String, String> params,
    		InputStream stream) throws IOException, FeedException {
        try {
        	RetailCustomer rc = retailCustomerService.findById(retailCustomerId);
        	importService.importData(stream);
       
            List<EntryType> entries = importService.getEntries();
            Iterator<EntryType> its = entries.iterator();
            while (its.hasNext()) {
            	EntryType entry = its.next();
            	UsagePoint usagePoint = entry.getContent().getUsagePoint();
            	if ( usagePoint != null)
                usagePointService.associateByUUID(rc, usagePoint.getUUID());
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
 
    }

    @RequestMapping(value = Routes.BATCH_DOWNLOAD_MY_DATA_COLLECTION, method = RequestMethod.GET)
    public void download_collection(HttpServletResponse response, 
    		@PathVariable Long retailCustomerId,
    		@RequestParam Map<String, String> params) throws IOException, FeedException {
        response.setContentType(MediaType.APPLICATION_ATOM_XML_VALUE);
        response.addHeader("Content-Disposition", "attachment; filename=GreenButtonDownload.xml");
          try {
              exportService.exportUsagePointsFull(retailCustomerId, response.getOutputStream(), new ExportFilter(params));
              
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
 
    }
    
    @RequestMapping(value = Routes.BATCH_DOWNLOAD_MY_DATA_MEMBER, method = RequestMethod.GET)
    public void download_member(HttpServletResponse response, 
    		@PathVariable Long retailCustomerId,
    		@PathVariable Long usagePointId,
    		@RequestParam Map<String, String> params) throws IOException, FeedException {
        response.setContentType(MediaType.APPLICATION_ATOM_XML_VALUE);
        response.addHeader("Content-Disposition", "attachment; filename=GreenButtonDownload.xml");
          try {
              exportService.exportUsagePointFull(retailCustomerId, usagePointId, response.getOutputStream(), new ExportFilter(params));
              
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
 
    }
    
    @RequestMapping(value = Routes.BATCH_SUBSCRIPTION, method = RequestMethod.GET)
    public void subscription(HttpServletResponse response, 
    		@PathVariable Long subscriptionId,
    		@RequestParam Map<String, String> params) throws IOException, FeedException {
        response.setContentType(MediaType.APPLICATION_ATOM_XML_VALUE);
        response.addHeader("Content-Disposition", "attachment; filename=GreenButtonDownload.xml");
          try {
              exportService.exportBatchSubscription(subscriptionId, response.getOutputStream(), new ExportFilter(params));
              
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
 
    }
    
    @RequestMapping(value = Routes.BATCH_BULK_MEMBER, method = RequestMethod.GET)
    public void bulk(HttpServletResponse response, 
    		@PathVariable Long bulkId,
    		@RequestParam Map<String, String> params) throws IOException, FeedException {
        response.setContentType(MediaType.APPLICATION_ATOM_XML_VALUE);
        response.addHeader("Content-Disposition", "attachment; filename=GreenButtonDownload.xml");
          try {
              exportService.exportBatchBulk(bulkId, response.getOutputStream(), new ExportFilter(params));
              
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
 
    }
    public void setRetailCustomerService(RetailCustomerService retailCustomerService) {
        this.retailCustomerService = retailCustomerService;
    }

    public void setUsagePointService(UsagePointService usagePointService) {
        this.usagePointService = usagePointService;
    }
    
    public void setExportService(ExportService exportService) {
        this.exportService = exportService;
    }
    
    public void setImportService(ImportService importService) {
    	this.importService = importService;
    }
}
