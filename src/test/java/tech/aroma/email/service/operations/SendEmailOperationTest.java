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

package tech.aroma.email.service.operations;

import org.apache.commons.mail.Email;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import tech.aroma.email.service.provider.EmailDelivery;
import tech.aroma.email.service.provider.EmailFactory;
import tech.aroma.thrift.email.EmailMessage;
import tech.aroma.thrift.email.service.SendEmailRequest;
import tech.aroma.thrift.email.service.SendEmailResponse;
import tech.aroma.thrift.exceptions.InvalidArgumentException;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.GeneratePojo;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.one;
import static tech.sirwellington.alchemy.generator.PeopleGenerators.emails;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;


/**
 *
 * @author SirWellington
 */
@Repeat(50)
@RunWith(AlchemyTestRunner.class)
public class SendEmailOperationTest
{

    @Mock
    private EmailFactory emailFactory;
    
    @Mock
    private EmailDelivery emailDelivery;
    
    @Mock
    private Email email;
    
    @GeneratePojo
    private SendEmailRequest request;
    
    @Mock
    private EmailMessage emailMessage;
    
    private String emailAddress;
    
    private SendEmailOperation instance;

    @Before
    public void setUp() throws Exception
    {

        setupData();
        setupMocks();
        
        instance = new SendEmailOperation(emailFactory, emailDelivery);
    }

    private void setupData() throws Exception
    {
        emailAddress = one(emails());
        
        request.setEmailAddress(emailAddress);
        request.setEmailMessage(emailMessage);
        
    }

    private void setupMocks() throws Exception
    {
        when(emailMessage.isSet()).thenReturn(true);
        
        when(emailFactory.createEmailFor(emailMessage))
            .thenReturn(email);
    }

    @DontRepeat
    @Test
    public void testConstructor()
    {
        assertThrows(() ->  new SendEmailOperation(null, emailDelivery));
        assertThrows(() ->  new SendEmailOperation(emailFactory, null));
    }
    
    @Test
    public void testProcess() throws Exception
    {
        SendEmailResponse response = instance.process(request);
        assertThat(response, notNullValue());
        
        verify(emailDelivery).sendEmail(email, emailAddress);
    }
    
    @Test
    public void testWithBadArgs() throws Exception
    {
        assertThrows(() -> instance.process(null))
            .isInstanceOf(InvalidArgumentException.class);

        //Missing Email
        SendEmailRequest requestMissingEmail = new SendEmailRequest(request);
        requestMissingEmail.unsetEmailAddress();
        assertThrows(() -> instance.process(requestMissingEmail))
            .isInstanceOf(InvalidArgumentException.class);

        //Missing email message
        SendEmailRequest requestMissingMessage = new SendEmailRequest(request);
        requestMissingMessage.unsetEmailMessage();
        assertThrows(() -> instance.process(requestMissingMessage))
            .isInstanceOf(InvalidArgumentException.class);

        //Message is not set
        SendEmailRequest requestMessageNotSet = new SendEmailRequest()
            .setEmailMessage(new EmailMessage());
        assertThrows(() -> instance.process(requestMessageNotSet))
            .isInstanceOf(InvalidArgumentException.class);
    }
    

}
