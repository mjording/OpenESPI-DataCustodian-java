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
import org.energyos.espi.common.domain.ApplicationInformation;
import org.energyos.espi.common.domain.Subscription;
import org.energyos.espi.common.service.ApplicationInformationService;
import org.energyos.espi.common.service.ExportService;
import org.energyos.espi.common.utils.ExportFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Controller
public class ApplicationInformationRESTController {

    @Autowired
    private ApplicationInformationService applicationInformationService;
    
    @Autowired
    private ExportService exportService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleGenericException() {}

    // ROOT and XPath are the same for this one.
    //
    @RequestMapping(value = Routes.ROOT_APPLICATION_INFORMATION_COLLECTION, method = RequestMethod.GET)
	public void index(HttpServletResponse response,
    		@RequestParam Map<String, String> params) throws IOException, FeedException {
        response.setContentType(MediaType.APPLICATION_ATOM_XML_VALUE);
        exportService.exportApplicationInformations(response.getOutputStream(), new ExportFilter(params));
    }

    // 
    //
    @RequestMapping(value = Routes.ROOT_APPLICATION_INFORMATION_MEMBER, method = RequestMethod.GET)
    public void show(HttpServletResponse response, 
    		@PathVariable long applicationInformationId,
    		@RequestParam Map<String, String> params) throws IOException, FeedException {
        response.setContentType(MediaType.APPLICATION_ATOM_XML_VALUE);
        try {
            exportService.exportApplicationInformation(applicationInformationId, response.getOutputStream(), new ExportFilter(params));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
 
    }

    // 
    //
    @RequestMapping(value = Routes.ROOT_APPLICATION_INFORMATION_COLLECTION, method = RequestMethod.POST)
    public void create(HttpServletResponse response,
    		@RequestParam Map<String, String> params, 
    		InputStream stream) throws IOException {
        response.setContentType(MediaType.APPLICATION_ATOM_XML_VALUE);
        try {
            ApplicationInformation applicationInformation = this.applicationInformationService.importResource(stream);
            exportService.exportApplicationInformation(applicationInformation.getId(), response.getOutputStream(), new ExportFilter(params));            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    //

    @RequestMapping(value = Routes.ROOT_APPLICATION_INFORMATION_MEMBER, method = RequestMethod.PUT)
    public void update(HttpServletResponse response, 
    		@PathVariable long applicationInformationId,
    		@RequestParam Map<String, String> params,
    		InputStream stream) throws IOException, FeedException {
    	ApplicationInformation applicationInformation = applicationInformationService.findById(applicationInformationId);
 
        if (applicationInformation != null) {
            try {
            	
                ApplicationInformation newApplicationInformation = applicationInformationService.importResource(stream);
                applicationInformation.merge(newApplicationInformation);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = Routes.ROOT_APPLICATION_INFORMATION_MEMBER, method = RequestMethod.DELETE)
    public void delete(HttpServletResponse response, 
    		@PathVariable long applicationInformationId,
    		@RequestParam Map<String, String> params,
    		InputStream stream) throws IOException, FeedException {
    	ApplicationInformation applicationInformation = applicationInformationService.findById(applicationInformationId);
        if (applicationInformation != null) {
        	applicationInformationService.delete(applicationInformation);
        }
    }    		
   
    public void setApplicationInformationService(ApplicationInformationService applicationInformationService) {
        this.applicationInformationService = applicationInformationService;
    }

    public void setExportService(ExportService exportService) {
        this.exportService = exportService;
    }
}
