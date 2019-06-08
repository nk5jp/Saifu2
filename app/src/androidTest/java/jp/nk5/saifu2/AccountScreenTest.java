package jp.nk5.saifu2;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import java.util.List;

import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.repository.AccountRepository;
import jp.nk5.saifu2.infra.dao.DBHelper;
import jp.nk5.saifu2.infra.repository.AccountRepositorySQLite;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AccountScreenTest {

    @Rule
    public ActivityTestRule<BankActivity> activityRule = new ActivityTestRule<>(BankActivity.class);

    private BankActivity activity;
    private AccountRepository repository;

    @Before
    public void clearDB() throws Exception
    {
        DBHelper dbHelper = DBHelper.getInstance(activityRule.getActivity());
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 1);
        activity = activityRule.getActivity();
        repository = AccountRepositorySQLite.getInstance(activity);
        repository.initialize();
    }

    @Test
    public void openAccount() throws Exception {
        onView(withId(R.id.editText1)).perform(typeText("test1"));
        onView(withId(R.id.button1)).perform(click());

        List<Account> accounts = repository.getAllAccount();
        assertThat(accounts.size()).isEqualTo(1);
        assertThat(accounts.get(0).getName()).isEqualTo("test1");
    }

    @Test
    public void failedToOpenDuplicatedAccount() throws Exception {
        onView(withId(R.id.editText1)).perform(typeText("test1"));
        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.button1)).perform(click());

        List<Account> accounts = repository.getAllAccount();
        assertThat(accounts.size()).isEqualTo(1);

    }

    @Test
    public void openAnotherAccount() throws Exception {
        onView(withId(R.id.editText1)).perform(typeText("test1"));
        onView(withId(R.id.button1)).perform(click());
        onView(withId(R.id.editText1)).perform(clearText()).perform(typeText("test2"));
        onView(withId(R.id.button1)).perform(click());

        List<Account> accounts = repository.getAllAccount();
        assertThat(accounts.size()).isEqualTo(2);
        assertThat(accounts.get(0).getName()).isEqualTo("test1");
        assertThat(accounts.get(1).getName()).isEqualTo("test2");
    }
}
