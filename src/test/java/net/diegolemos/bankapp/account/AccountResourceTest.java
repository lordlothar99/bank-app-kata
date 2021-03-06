package net.diegolemos.bankapp.account;

import net.diegolemos.bankapp.AbstractHttpTest;
import net.diegolemos.bankapp.client.Client;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.Collection;

import static java.util.Arrays.asList;
import static javax.ws.rs.client.Entity.json;
import static net.diegolemos.bankapp.client.ClientBuilder.aClient;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class AccountResourceTest extends AbstractHttpTest {

    private static final Client ALICE = aClient().withUsername("alice").build();
    private static final Client BOB = aClient().withUsername("bob").build();
    private static final Account ALICE_ACCOUNT = new Account(ALICE);
    private static final Account BOB_ACCOUNT = new Account(BOB);

    private AccountService accountService = injectMock(AccountService.class);
    private WebTarget accountResource = resource("account");

    @Test public void
    should_get_account_balance_for_a_given_client() {
        given(accountService.findBy("bob")).willReturn(BOB_ACCOUNT);

        Account bobAccount = accountResource.path("bob").request().get(Account.class);

        assertThat(bobAccount.balance(), is(0.0));
    }

    @Test public void
    should_create_a_new_account_for_a_given_client() {
        accountResource.request().put(json(BOB_ACCOUNT));

        verify(accountService).save(BOB_ACCOUNT);
    }

    @Test public void
    should_get_all_accounts() {
        given(accountService.all()).willReturn(asList(BOB_ACCOUNT, ALICE_ACCOUNT));

        Collection<Account> accounts = accountResource.request().get(new GenericType<Collection<Account>>() {});

        assertThat(accounts, hasItems(BOB_ACCOUNT, ALICE_ACCOUNT));
    }

    @Test public void
    should_update_account() {
        Account account = new Account(ALICE);
        account.deposit(10);

        accountResource.request().put(json(account));

        verify(accountService).save(account);
    }
}
