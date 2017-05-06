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

package tech.aroma.email.service.provider;

import java.util.List;
import javax.mail.Authenticator;
import org.apache.commons.mail.Email;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.mockito.Mockito.verify;
import static tech.sirwellington.alchemy.generator.CollectionGenerators.listOf;
import static tech.sirwellington.alchemy.generator.PeopleGenerators.emails;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;


/**
 *
 * @author SirWellington
 */
@Repeat(10)
@RunWith(AlchemyTestRunner.class)
public class EmailDeliveryImplTest 
{

    @Mock
    private Authenticator authenticator;
    
    @Mock
    private Email email;
    
    private EmailDeliveryImpl instance;
    
    private List<String> emails;
    
    @Before
    public void setUp() throws Exception
    {
        
        setupData();
        setupMocks();
        
        instance = new EmailDeliveryImpl(authenticator);
    }


    private void setupData() throws Exception
    {
        emails = listOf(emails());
    }

    private void setupMocks() throws Exception
    {
        
    }

    @DontRepeat
    @Test
    public void testConstructor()
    {
        assertThrows(() -> new EmailDeliveryImpl(null))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void testSendEmail() throws Exception
    {
        instance.sendEmail(email, emails);
        
        verify(email).send();
        verify(email).setAuthenticator(authenticator);
    }

}