package testtask;

import org.junit.Test;

import org.junit.jupiter.api.Assertions;
import testtask.model.Account;
import testtask.model.Sex;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class TestTaskTest
{

    private static Collection<Account> accounts = new LinkedList<>();
    private static Account account1;
    private static Account account2;
    private static Account account3;
    private static Account account4;
    private static Account account5;
    private static Account account6;

    static
    {
        account1 = new Account(1L, "TestName1", "TestLastName1", "test1@gmail.com",
                LocalDate.of(1999, Month.JANUARY, 1), Sex.MALE, LocalDate.now(), new BigDecimal(100));

        account2 = new Account(2L, "TestName2", "TestLastName2", "test2@gmail.com",
                LocalDate.of(1999, 6, 2), Sex.MALE, LocalDate.now(), new BigDecimal(1000));

        account3 = new Account(3L, "TestName3", "TestLastName3", "test3@ukr.net",
                LocalDate.of(1999, Month.JANUARY, 3), Sex.MALE, LocalDate.now(), new BigDecimal(10_000));

        account4 = new Account(4L, "TestName4", "TestLastName4", "test4@ukr.net",
                LocalDate.of(1999, Month.JANUARY, 4), Sex.FEMALE, LocalDate.now(), new BigDecimal(100_000));

        account5 = new Account(5L, "TestName5", "TestLastName5", "test5@gmail.com",
                LocalDate.of(1999, Month.JANUARY, 5), Sex.MALE, LocalDate.now(), new BigDecimal(1_000_000));

        account6 = new Account(6L, "TestName6", "TestLastName6", "test6@gmail.com",
                LocalDate.of(1999, Month.JANUARY, 6), Sex.FEMALE, LocalDate.now(), new BigDecimal(10_000_001));

        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
        accounts.add(account4);
        accounts.add(account5);
        accounts.add(account6);
    }

    private static final TestTask testTask = new TestTask(accounts);

    @Test
    public void testFindRichestPerson()
    {
        Account account = null;
        Optional<Account> richestPerson = testTask.findRichestPerson();
        if (richestPerson.isPresent())
        {
            account = richestPerson.get();
        }
        Assertions.assertEquals(account6, account);
    }

    @Test
    public void testPartitionMaleAccounts()
    {
        List<Account> f = new LinkedList<>();
        List<Account> m = new LinkedList<>();
        f.add(account4);
        f.add(account6);
        m.add(account1);
        m.add(account2);
        m.add(account3);
        m.add(account5);
        Map<Boolean, List<Account>> expected = new HashMap<>();
        expected.put(true, m);
        expected.put(false, f);
        Map<Boolean, List<Account>> partitionMaleAccounts = testTask.partitionMaleAccounts();
        Assertions.assertEquals(expected, partitionMaleAccounts);
    }

    @Test
    public void testGroupAccountsByEmailDomain()
    {
        Map<String, List<Account>> expected = new HashMap<>();
        List<Account> gmail = new LinkedList<>();
        List<Account> ukr = new LinkedList<>();
        ukr.add(account3);
        ukr.add(account4);
        gmail.add(account1);
        gmail.add(account2);
        gmail.add(account5);
        gmail.add(account6);
        expected.put("@ukr.net", ukr);
        expected.put("@gmail.com", gmail);
        Map<String, List<Account>> groupAccountsByEmailDomain = testTask.groupAccountsByEmailDomain();
        Assertions.assertEquals(expected, groupAccountsByEmailDomain);
    }

    @Test
    public void testGetCharacterFrequencyIgnoreCaseInFirstAndLastNames()
    {
        account1.setFirstName("Kali");
        account2.setFirstName("Kali");
        account3.setFirstName("Kali");
        account4.setFirstName("Kali");
        account5.setFirstName("Kali");
        account6.setFirstName("Kali");
        account1.setLastName("Stone");
        account2.setLastName("Stone");
        account3.setLastName("Stone");
        account4.setLastName("Stone");
        account5.setLastName("Stone");
        account6.setLastName("Stone");
        Map<Character, Long> expected = new HashMap<>();
        expected.put('a', 6L);
        expected.put('s', 6L);
        expected.put('t', 6L);
        expected.put('e', 6L);
        expected.put('i', 6L);
        expected.put('k', 6L);
        expected.put('l', 6L);
        expected.put('n', 6L);
        expected.put('o', 6L);
        Map<Character, Long> result = testTask.getCharacterFrequencyIgnoreCaseInFirstAndLastNames();
        Assertions.assertEquals(expected, result);
    }
}