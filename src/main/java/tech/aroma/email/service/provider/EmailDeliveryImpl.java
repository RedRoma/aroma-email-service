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
import java.util.Objects;
import javax.mail.Authenticator;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.thrift.exceptions.OperationFailedException;

import static java.util.stream.Collectors.toList;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 *
 * @author SirWellington
 */
final class EmailDeliveryImpl implements EmailDelivery
{

    private final static Logger LOG = LoggerFactory.getLogger(EmailDeliveryImpl.class);

    private final Authenticator authenticator;

    EmailDeliveryImpl(Authenticator authenticator)
    {
        checkThat(authenticator).is(notNull());

        this.authenticator = authenticator;
    }

    @Override
    public void sendEmail(Email email, List<String> emailAddresses) throws TException
    {
        List<InternetAddress> addresses = emailAddresses.stream()
            .map(this::toAddress)
            .filter(Objects::nonNull)
            .collect(toList());

        try
        {
            email.setTo(addresses);
            email.setAuthenticator(authenticator);
            email.send();
        }
        catch (EmailException ex)
        {
            LOG.error("Failed to send Email to {}", emailAddresses, ex);
            throw new OperationFailedException("Could not send email: " + ex.getMessage());
        }
    }

    private InternetAddress toAddress(String email)
    {
        try
        {
            return new InternetAddress(email);
        }
        catch (AddressException ex)
        {
            LOG.warn("Failed to parse {} as Email", email, ex);
            return null;
        }
    }

}
