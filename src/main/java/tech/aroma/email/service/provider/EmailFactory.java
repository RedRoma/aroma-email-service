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


import com.google.inject.ImplementedBy;
import javax.inject.Inject;
import org.apache.commons.mail.Email;
import org.apache.thrift.TException;
import tech.aroma.thrift.email.EmailMessage;
import tech.aroma.thrift.email.EmailNewApplication;
import tech.aroma.thrift.email.EmailNewUserRegistration;
import tech.aroma.thrift.exceptions.InvalidArgumentException;
import tech.sirwellington.alchemy.annotations.arguments.Required;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.assertions.BooleanAssertions.trueStatement;

/**
 * This interface is responsible for creating an appropriate email message for
 * an {@link EmailMessage}.
 * 
 * @author SirWellington
 */
@ImplementedBy(EmailFactory.Impl.class)
public interface EmailFactory 
{
    /**
     * Given an Email Message, creates an appropriate Email message to send.
     * 
     * @param message
     * 
     * @return
     * @throws TException 
     */
    Email createEmailFor(@Required EmailMessage message) throws TException;
    
    static class Impl implements EmailFactory
    {
        
        private final EmailProvider emailProvider;
        
        @Inject
        Impl(EmailProvider emailProvider)
        {
            checkThat(emailProvider).is(notNull());
            
            this.emailProvider = emailProvider;
        }
        
        @Override
        public Email createEmailFor(EmailMessage message) throws TException
        {
            checkThat(message).is(notNull());
            
            checkThat(message.isSet())
                .usingMessage("EmailMessage is not set")
                .throwing(InvalidArgumentException.class)
                .is(trueStatement());
            
            if (message.isSetNewApp())
            {
                EmailNewApplication newApp = message.getNewApp();
                
                return emailProvider.getNewApplicationCreated(newApp.app, newApp.creator, newApp.appToken);
            }
            
            if (message.isSetNewUser())
            {
                EmailNewUserRegistration newUser = message.getNewUser();
                
                return emailProvider.getNewUserEmailFor(newUser.infoOfNewUser);
            }
            
            return null;
        }
    }

}
