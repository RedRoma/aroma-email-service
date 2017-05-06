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

import org.apache.commons.mail.Email;
import tech.aroma.thrift.Application;
import tech.aroma.thrift.Message;
import tech.aroma.thrift.User;
import tech.aroma.thrift.authentication.ApplicationToken;
import tech.aroma.thrift.exceptions.OperationFailedException;
import tech.sirwellington.alchemy.annotations.arguments.Required;


/**
 *
 * @author SirWellington
 */
public interface EmailProvider 
{
    Email getNewApplicationCreated(@Required Application newApp, 
                                   @Required User creator,
                                   @Required ApplicationToken appToken) throws OperationFailedException;
    
    Email getNewUserEmailFor(@Required User user) throws OperationFailedException;
    
    Email getNewMessageEmailFor(@Required Message message) throws OperationFailedException;
    
    static EmailProvider simpleEmails()
    {
        return new SimpleEmailProviderImpl();
    }
}
