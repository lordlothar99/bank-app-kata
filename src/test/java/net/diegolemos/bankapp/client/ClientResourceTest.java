package net.diegolemos.bankapp.client;

import net.diegolemos.bankapp.AbstractHttpTest;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.Collection;

import static java.util.Arrays.asList;
import static javax.ws.rs.client.Entity.json;
import static net.diegolemos.bankapp.client.ClientBuilder.aClient;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class ClientResourceTest extends AbstractHttpTest {

    private static final Client BOB = aClient().withUsername("bob").build();
    private static final Client ALICE = aClient().withUsername("alice").build();

    private ClientService clients = injectMock(ClientService.class);
    private WebTarget clientResource = resource("client");

    @Test public void
    should_save_client() {
        clientResource.path("bob").request().put(json(BOB));

        verify(clients).save( BOB);
    }

    @Test public void
    should_get_client_by_username() {
        given(clients.findByUsername("bob")).willReturn(BOB);

        Client client = clientResource.path("bob").request().get(Client.class);

        assertThat(client, equalTo(BOB));
    }

    @Test public void
    should_get_all_clients() {
        given(clients.all()).willReturn(asList(ALICE, BOB));

        Collection<Client> clients = clientResource.request().get(new GenericType<Collection<Client>>() {});

        assertThat(clients, hasItems(ALICE, BOB));
    }
}
