/*
 * Copyright 2016 RedRoma, Inc.
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

package tech.aroma.email.service.provider;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javax.mail.Authenticator;
import org.apache.commons.mail.DefaultAuthenticator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.annotations.testing.IntegrationTest;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author SirWellington
 */
@IntegrationTest
@Repeat(10)
@RunWith(AlchemyTestRunner.class)
public class ModuleEmailProviderTest 
{

    private ModuleEmailProvider instance;
    
    @Before
    public void setUp() throws Exception
    {
        instance = new ModuleEmailProvider();
    }

    @Test
    public void testConfigure()
    {
        Injector injector = Guice.createInjector(instance, new ModuleEmailCredentials());
        
        assertThat(injector, notNullValue());
        EmailFactory factory = injector.getInstance(EmailFactory.class);
        EmailDelivery delivery = injector.getInstance(EmailDelivery.class);
    }

    
    static class ModuleEmailCredentials extends AbstractModule
    {

        @Override
        protected void configure()
        {
            bind(Authenticator.class).toInstance(new DefaultAuthenticator("", ""));
        }
    }
}