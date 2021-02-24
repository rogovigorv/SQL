package com.foxminded.SQL.dao;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

public class ConnectionFactoryTest {
    private static final String DRIVER = "org.h2.Driver";
    private static final String H2_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    private final ConnectionFactory connectionFactory =
            new ConnectionFactory(DRIVER, H2_URL, USERNAME, PASSWORD);

    @Test
    void shouldConfirmThatTheReturnedObjectClassMatchesTheConnectionClass() {
        final Connection actual = connectionFactory.connect();

        assertThat(actual, instanceOf(Connection.class));
    }
}
