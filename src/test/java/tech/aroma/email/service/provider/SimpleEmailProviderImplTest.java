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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.aroma.thrift.Message;
import tech.aroma.thrift.User;
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner;
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat;
import tech.sirwellington.alchemy.test.junit.runners.GeneratePojo;
import tech.sirwellington.alchemy.test.junit.runners.Repeat;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.assertThrows;

/**
 *
 * @author SirWellington
 */
@Repeat(10)
@RunWith(AlchemyTestRunner.class)
public class SimpleEmailProviderImplTest
{

    @GeneratePojo
    private Message message;

    @GeneratePojo
    private User user;

    private SimpleEmailProviderImpl instance;

    @Before
    public void setUp() throws Exception
    {
        instance = new SimpleEmailProviderImpl();

        setupData();
        setupMocks();
    }

    private void setupData() throws Exception
    {

    }

    private void setupMocks() throws Exception
    {

    }

    @Test
    public void testGetNewUserEmailFor() throws Exception
    {
        Email result = instance.getNewUserEmailFor(user);
        assertThat(result, notNullValue());
    }

    @DontRepeat
    @Test
    public void testGetNewUserEmailForWithBadArgs() throws Exception
    {
        assertThrows(() -> instance.getNewUserEmailFor(null));
    }

    @Test
    public void testGetNewMessageEmailFor() throws Exception
    {
        Email result = instance.getNewMessageEmailFor(message);
        assertThat(result, notNullValue());
    }

    @DontRepeat
    @Test
    public void testGetNewMessageEmailForWithBadArgs() throws Exception
    {
        assertThrows(() -> instance.getNewMessageEmailFor(null));
    }

}
