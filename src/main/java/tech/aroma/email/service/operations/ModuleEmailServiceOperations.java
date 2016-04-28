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


import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.thrift.email.service.SendEmailRequest;
import tech.aroma.thrift.email.service.SendEmailResponse;
import tech.sirwellington.alchemy.thrift.operations.ThriftOperation;

/**
 *
 * @author SirWellington
 */
public final class ModuleEmailServiceOperations extends AbstractModule
{
    private final static Logger LOG = LoggerFactory.getLogger(ModuleEmailServiceOperations.class);

    @Override
    protected void configure()
    {
        bind(new TypeLiteral<ThriftOperation<SendEmailRequest, SendEmailResponse>>() {})
            .to(SendEmailOperation.class);
    }

}
