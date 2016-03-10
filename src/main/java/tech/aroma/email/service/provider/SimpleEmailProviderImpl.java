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
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.thrift.Message;
import tech.aroma.thrift.User;
import tech.aroma.thrift.exceptions.OperationFailedException;

import static java.lang.String.format;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 *
 * @author SirWellington
 */
final class SimpleEmailProviderImpl implements EmailProvider
{

    private final static Logger LOG = LoggerFactory.getLogger(SimpleEmailProviderImpl.class);

    @Override
    public Email getNewUserEmailFor(User user) throws OperationFailedException
    {
        checkThat(user).is(notNull());

        String message = format("Hello %s! Welcome to Aroma", user.firstName);

        try
        {
            return new SimpleEmail().setMsg(message);
        }
        catch (EmailException ex)
        {
            LOG.error("Failed to create Simple Email", ex);
            throw new OperationFailedException("Could not generate email: " + ex.getMessage());
        }
    }

    @Override
    public Email getNewMessageEmailFor(Message message) throws OperationFailedException
    {
        checkThat(message).is(notNull());

        String email = format("New Message from %s : %s\n\n%s", message.applicationName, message.title, message.body);

        try
        {
            return new SimpleEmail().setMsg(email);
        }
        catch (EmailException ex)
        {
            LOG.error("Could not create simple email", ex);
            throw new OperationFailedException("Could not generate email: " + ex.getMessage());
        }
    }

}
