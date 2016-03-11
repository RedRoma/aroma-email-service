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


import com.google.inject.AbstractModule;
import decorice.DecoratorModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.thrift.email.service.EmailService;

/**
 *
 * @author SirWellington
 */
final class ModuleEmailService extends AbstractModule
{
    private final static Logger LOG = LoggerFactory.getLogger(ModuleEmailService.class);

    @Override
    protected void configure()
    {
        install(new ServiceModule());
    }
    
    private static class ServiceModule extends DecoratorModule
    {

        {
            bind(EmailService.Iface.class)
                .to(BaseEmailService.class)
                .decoratedBy(AuthenticationLayer.class);
        }
    }

}
