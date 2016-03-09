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

import java.util.List;
import org.apache.commons.mail.Email;
import org.apache.thrift.TException;
import sir.wellington.alchemy.collections.lists.Lists;


/**
 *
 * @author SirWellington
 */
public interface EmailDelivery 
{
    default void sendEmail(Email email, String emailAddress) throws TException
    {
        sendEmail(email, Lists.createFrom(emailAddress));
    }
    
    void sendEmail(Email email, List<String> emailAddresses) throws TException;
    
    EmailDelivery NO_OP = (email, addresses) -> {};
    
    static EmailDelivery noOp()
    {
        return NO_OP;
    }
    
}
