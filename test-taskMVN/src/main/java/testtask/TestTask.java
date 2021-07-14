package testtask;


import testtask.model.Account;
import testtask.model.Sex;

import java.util.*;
import java.util.stream.Collectors;

public class TestTask
{

    private Collection<Account> accounts;

    public TestTask(Collection<Account> accounts)
    {
        this.accounts = accounts;
    }

    /**
     * Returns {@link Optional} that contains an {@link Account} with the max value of balance
     *
     * @return account with max balance wrapped with optional
     */
    public Optional<Account> findRichestPerson()
    {
        return accounts.stream().max(Comparator.comparing(Account::getBalance));
    }

    /**
     * Returns a map that separates all accounts into two lists - male and female. Map has two keys {@code true} indicates
     * male list, and {@code false} indicates female list.
     *
     * @return a map where key is true or false, and value is list of male, and female accounts
     */
    public Map<Boolean, List<Account>> partitionMaleAccounts()
    {
        return accounts.stream().
                collect(Collectors.groupingBy(x -> Sex.MALE.equals(x.getSex()), Collectors.toList()));
    }

    /**
     * Returns a {@link Map} that stores accounts grouped by its email domain. A map key is {@link String} which is an
     * email domain like "gmail.com". And the value is a {@link List} of {@link Account} objects with a specific email domain.
     *
     * @return a map where key is an email domain and value is a list of all account with such email
     */
    public Map<String, List<Account>> groupAccountsByEmailDomain()
    {
        return accounts.stream().
                collect(Collectors.groupingBy(x -> x.getEmail().split("@")[1], Collectors.toList()));
    }

    /**
     * Returns a {@link Map} where key is a letter {@link Character}, and value is a number of its occurrences ignoring
     * case, in all {@link Account#firstName} and {@link Account#lastName}. All letters should stored in lower case.
     *
     * @return a map where key is a letter and value is its count ignoring case in all first and last names
     */
    public Map<Character, Long> getCharacterFrequencyIgnoreCaseInFirstAndLastNames()
    {
        return accounts.stream()
                .map(x -> (x.getFirstName() + x.getLastName()))
                .collect(Collectors.joining())
                .toLowerCase(Locale.ROOT)
                .chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toMap(x -> x, n -> 1L, Long::sum));
    }
}

