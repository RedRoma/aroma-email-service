/*
 * Copyright 2017 RedRoma, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.aroma.email.service;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javax.mail.Authenticator;
import org.apache.commons.mail.DefaultAuthenticator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import tech.aroma.thrift.authentication.service.AuthenticationService;
import tech.aroma.thrift.email.service.EmailService;
import tech.sirwellington.alchemy.annotations.testing.IntegrationTest;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;


/**
 *
 * @author SirWellington
 */
@IntegrationTest
@Repeat(10)
@RunWith(AlchemyTestRunner.class)
public class ModuleEmailServiceTest 
{
    
    @Mock
    private AuthenticationService.Iface authenticationService;
    
    private ModuleEmailService instance;
    
    private AbstractModule missingDependencies = new AbstractModule()
    {
        @Override
        protected void configure()
        {
            bind(Authenticator.class).toInstance(new DefaultAuthenticator("", ""));
            bind(AuthenticationService.Iface.class).toInstance(authenticationService);
        }
    };

    @Before
    public void setUp() throws Exception
    {
        instance = new ModuleEmailService();
        
    }

    @Test
    public void testConfigure()
    {
        Injector injector = Guice.createInjector(instance, missingDependencies);
        
        EmailService.Iface service = injector.getInstance(EmailService.Iface.class);
        assertThat(service, notNullValue());
    }

}