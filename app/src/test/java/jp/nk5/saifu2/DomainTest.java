package jp.nk5.saifu2;

import org.junit.Test;

import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.SpecificId;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DomainTest {

    @Test(expected = Exception.class)
    public void AccountIsNotPermittedBlankName() throws Exception
    {
        Account account = new Account(1, "", 100);
    }

    @Test
    public void AccountCanMakeOriginalString() throws Exception
    {
        Account account = new Account(1, "testName", 1900);
        assertEquals("testName:1,900", account.toString());
    }

    @Test
    public void IfAccountIsNotPersistedYet_IdIsSpecifiedValue() throws Exception
    {
        Account account = new Account(SpecificId.NotPersisted.getId(), "temp", 100);
        assertEquals(-1, account.getId());
    }


}