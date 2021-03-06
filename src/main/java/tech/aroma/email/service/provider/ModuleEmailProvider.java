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


import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author SirWellington
 */
public final class ModuleEmailProvider extends AbstractModule
{
    private final static Logger LOG = LoggerFactory.getLogger(ModuleEmailProvider.class);

    @Override
    protected void configure()
    {
        bind(EmailDelivery.class).to(EmailDeliveryImpl.class);
        bind(EmailProvider.class).to(SimpleEmailProviderImpl.class);
        bind(EmailFactory.class).to(EmailFactory.Impl.class);
    }

}
