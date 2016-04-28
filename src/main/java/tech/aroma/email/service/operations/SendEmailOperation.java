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

package tech.aroma.email.service.operations;

import javax.inject.Inject;
import org.apache.commons.mail.Email;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.email.service.provider.EmailDelivery;
import tech.aroma.email.service.provider.EmailFactory;
import tech.aroma.thrift.email.service.SendEmailRequest;
import tech.aroma.thrift.email.service.SendEmailResponse;
import tech.aroma.thrift.exceptions.InvalidArgumentException;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.thrift.operations.ThriftOperation;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.assertions.BooleanAssertions.trueStatement;
import static tech.sirwellington.alchemy.arguments.assertions.PeopleAssertions.validEmailAddress;

/**
 *
 * @author SirWellington
 */
final class SendEmailOperation implements ThriftOperation<SendEmailRequest, SendEmailResponse>
{
    
    private final static Logger LOG = LoggerFactory.getLogger(SendEmailOperation.class);
    
    private final EmailFactory emailFactory;
    private final EmailDelivery emailDelivery;
    
    @Inject
    SendEmailOperation(EmailFactory emailFactory, EmailDelivery emailDelivery)
    {
        checkThat(emailFactory, emailDelivery)
            .are(notNull());
        
        this.emailFactory = emailFactory;
        this.emailDelivery = emailDelivery;
    }
    
    @Override
    public SendEmailResponse process(SendEmailRequest request) throws TException
    {
        checkThat(request)
            .throwing(ex -> new InvalidArgumentException(ex.getMessage()))
            .is(good());
        
        String destinationAddress = request.emailAddress;
        Email email = emailFactory.createEmailFor(request.emailMessage);
        
        emailDelivery.sendEmail(email, destinationAddress);
        LOG.debug("Sent email to {}", destinationAddress);
        
        return new SendEmailResponse();
    }
    
    private AlchemyAssertion<SendEmailRequest> good()
    {
        return request ->
        {
            checkThat(request)
                .usingMessage("missing request")
                .is(notNull());
            
            checkThat(request.emailAddress)
                .is(validEmailAddress());
            
            checkThat(request.token)
                .is(notNull());
            
            checkThat(request.emailMessage)
                .usingMessage("request missing email message")
                .is(notNull());
            
            checkThat(request.emailMessage.isSet())
                .usingMessage("EmailMessage has not been set")
                .is(trueStatement());
        };
    }
    
}
