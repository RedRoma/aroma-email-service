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

 
package tech.aroma.email.service;


import javax.inject.Inject;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.thrift.email.service.EmailService;
import tech.aroma.thrift.email.service.SendEmailRequest;
import tech.aroma.thrift.email.service.SendEmailResponse;
import tech.aroma.thrift.exceptions.InvalidArgumentException;
import tech.aroma.thrift.exceptions.InvalidTokenException;
import tech.aroma.thrift.exceptions.OperationFailedException;
import tech.aroma.thrift.service.AromaServiceConstants;
import tech.sirwellington.alchemy.thrift.operations.ThriftOperation;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 *
 * @author SirWellington
 */
class BaseEmailService implements EmailService.Iface
{
    private final static Logger LOG = LoggerFactory.getLogger(BaseEmailService.class);
    
    private final ThriftOperation<SendEmailRequest, SendEmailResponse> sendEmailOperation;

    @Inject
    BaseEmailService(ThriftOperation<SendEmailRequest, SendEmailResponse> sendEmailOperation)
    {
        checkThat(sendEmailOperation).is(notNull());
        
        this.sendEmailOperation = sendEmailOperation;
    }
    
    @Override
    public double getApiVersion() throws TException
    {
        return AromaServiceConstants.API_VERSION;
    }

    @Override
    public SendEmailResponse sendEmail(SendEmailRequest request) throws InvalidArgumentException, 
                                                                        OperationFailedException,
                                                                        InvalidTokenException, 
                                                                        TException
    {
        checkThat(request)
            .throwing(InvalidArgumentException.class)
            .usingMessage("missing request")
            .is(notNull());
        
        LOG.info("Received request to send Email: {}", request.emailMessage);
        
        return sendEmailOperation.process(request);
    }

}
