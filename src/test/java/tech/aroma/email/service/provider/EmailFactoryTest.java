/*
 * Copyright 2016 RedRoma.
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

import org.apache.commons.mail.Email;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import tech.aroma.thrift.email.EmailMessage;
import tech.aroma.thrift.email.EmailNewApplication;
import tech.aroma.thrift.email.EmailNewUserRegistration;
import tech.aroma.thrift.exceptions.InvalidArgumentException;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.GeneratePojo;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;


/**
 *
 * @author SirWellington
 */
@Repeat(50)
@RunWith(AlchemyTestRunner.class)
public class EmailFactoryTest 
{
    
    @Mock
    private EmailProvider emailProvider;
    
    
    @Mock
    private Email email;
    
    @GeneratePojo
    private EmailNewApplication newApp;
    
    @GeneratePojo
    private EmailNewUserRegistration newUser;
    
    private EmailMessage emailMessage;
    
    private EmailFactory instance;

    @Before
    public void setUp() throws Exception
    {
        setupData();
        setupMocks();
        
        instance = new EmailFactory.Impl(emailProvider);
    }


    private void setupData() throws Exception
    {
        emailMessage = new EmailMessage();
    }

    private void setupMocks() throws Exception
    {
        
    }

    @Test
    public void testWithNewApp() throws Exception
    {
        emailMessage.setNewApp(newApp);
        
        when(emailProvider.getNewApplicationCreated(newApp.app, newApp.creator, newApp.appToken))
            .thenReturn(email);
        
        Email result = instance.createEmailFor(emailMessage);
        assertThat(result, is(email));
        
    }
    
    @Test
    public void testWithNewUser() throws Exception
    {
        emailMessage.setNewUser(newUser);
        
        when(emailProvider.getNewUserEmailFor(newUser.infoOfNewUser))
            .thenReturn(email);
        
        Email result = instance.createEmailFor(emailMessage);
        assertThat(result, is(email));
    }
    
    @Test
    public void testWhenNotSet() throws Exception
    {
        assertThrows(() -> instance.createEmailFor(emailMessage))
            .isInstanceOf(InvalidArgumentException.class);
    }
    
    @Test
    public void testWithBadArgs() throws Exception
    {
        assertThrows(() -> instance.createEmailFor(null));
    }

}