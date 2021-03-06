package org.energyos.espi.datacustodian.integration.api;

import org.energyos.espi.common.domain.RetailCustomer;
import org.energyos.espi.common.domain.Routes;
import org.energyos.espi.common.domain.Subscription;
import org.energyos.espi.common.test.EspiPersistenceFactory;
import org.energyos.espi.common.test.TestUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("/spring/test-context.xml")
@Transactional
public class SubscriptionRESTTests {

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private EspiPersistenceFactory factory;

    private MockMvc mockMvc;
    private Subscription subscription;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
        RetailCustomer retailCustomer = factory.createRetailCustomer();
        subscription = factory.createSubscription(retailCustomer);
        factory.createUsagePoint(retailCustomer);
    }

    @Test
    @Ignore
    public void show_returnsOk() throws Exception {
        mockMvc.perform(get(Routes.buildDataCustodianSubscription(subscription.getHashedId())))
                .andExpect(status().isOk());
    }

    @Test
    @Ignore("Until the hashed.vs.id is fully in place")
    public void show_returnsATOMContentType() throws Exception {
        mockMvc.perform(get(Routes.buildDataCustodianSubscription(subscription.getHashedId())))
                .andExpect(content().contentType(MediaType.APPLICATION_ATOM_XML));
    }

    @Test
    @Ignore("Until the hashed.vs.id is fully in place")
    public void show_returnsSubscriptionXML() throws Exception {
        mockMvc.perform(get(Routes.buildDataCustodianSubscription(subscription.getHashedId())))
            .andExpect(MockMvcResultMatchers.xpath("/:feed", TestUtils.namespaces()).exists());
    }
}
