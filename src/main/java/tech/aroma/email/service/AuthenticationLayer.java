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

package tech.aroma.email.service;

import decorice.DecoratedBy;
import javax.inject.Inject;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.thrift.authentication.AuthenticationToken;
import tech.aroma.thrift.authentication.service.AuthenticationService;
import tech.aroma.thrift.authentication.service.VerifyTokenRequest;
import tech.aroma.thrift.email.service.EmailService;
import tech.aroma.thrift.email.service.SendEmailRequest;
import tech.aroma.thrift.email.service.SendEmailResponse;
import tech.aroma.thrift.exceptions.InvalidArgumentException;
import tech.aroma.thrift.exceptions.InvalidTokenException;
import tech.aroma.thrift.exceptions.OperationFailedException;
import tech.sirwellington.alchemy.annotations.designs.patterns.DecoratorPattern;

import static tech.sirwellington.alchemy.annotations.designs.patterns.DecoratorPattern.Role.CONCRETE_DECORATOR;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.nonEmptyString;

/**
 * Decorates and existing {@linkplain EmailService.Iface Email Service} and provides Token Authentication.
 *
 * @author SirWellington
 */
@DecoratorPattern(role = CONCRETE_DECORATOR)
final class AuthenticationLayer implements EmailService.Iface
{

    private final static Logger LOG = LoggerFactory.getLogger(AuthenticationLayer.class);

    private final AuthenticationService.Iface authenticationService;
    private final EmailService.Iface delegate;

    @Inject
    AuthenticationLayer(AuthenticationService.Iface authenticationService,
                        @DecoratedBy(AuthenticationLayer.class) EmailService.Iface delegate)
    {
        this.authenticationService = authenticationService;
        this.delegate = delegate;
    }

    @Override
    public double getApiVersion() throws TException
    {
        return delegate.getApiVersion();
    }

    @Override
    public SendEmailResponse sendEmail(SendEmailRequest request) throws InvalidArgumentException, 
                                                                        OperationFailedException,
                                                                        InvalidTokenException,
                                                                        TException
    {
        checkThat(request)
            .throwing(InvalidArgumentException.class)
            .usingMessage("request is missing")
            .is(notNull());

        verifyToken(request.token);

        return delegate.sendEmail(request);
    }

    private void verifyToken(AuthenticationToken token) throws TException
    {
        checkThat(token)
            .throwing(InvalidTokenException.class)
            .usingMessage("Missing token");

        checkThat(token.tokenId)
            .usingMessage("Missing tokenId")
            .is(nonEmptyString());

        VerifyTokenRequest request = new VerifyTokenRequest()
            .setTokenId(token.tokenId);

        try
        {
            authenticationService.verifyToken(request);
        }
        catch (InvalidTokenException | OperationFailedException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new OperationFailedException("Could not verify token: " + ex.getMessage());
        }

    }

}
