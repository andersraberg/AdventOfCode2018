package se.anders_raberg.adventofcode2018;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class Day14 {
    private static final Logger LOGGER = Logger.getLogger(Day14.class.getName());
    private static final int NUMBER_OF_RECIPES = 768071;
    private static final List<Integer> LAST_RECIPES = Arrays.asList(7, 6, 8, 0, 7, 1);

    public static void run() {

        // Part 1
        int elfOne = 0;
        int elfTwo = 1;
        List<Integer> recipes = new ArrayList<>(Arrays.asList(3, 7));
        do {
            List<Integer> newRecipes = newRecipes(recipes.get(elfOne), recipes.get(elfTwo));
            recipes.addAll(newRecipes);
            elfOne = (elfOne + 1 + recipes.get(elfOne)) % recipes.size();
            elfTwo = (elfTwo + 1 + recipes.get(elfTwo)) % recipes.size();
        } while (recipes.size() < NUMBER_OF_RECIPES + 10);

        List<Integer> subList = recipes.subList(NUMBER_OF_RECIPES, NUMBER_OF_RECIPES + 10);
        LOGGER.info("Part 1 : " + subList);

        // Part 2
        recipes = new ArrayList<>(Arrays.asList(3, 7));
        elfOne = 0;
        elfTwo = 1;
        for (int i = 0; i < 20_000_000; i++) {
            List<Integer> newRecipes = newRecipes(recipes.get(elfOne), recipes.get(elfTwo));
            recipes.addAll(newRecipes);
            elfOne = (elfOne + 1 + recipes.get(elfOne)) % recipes.size();
            elfTwo = (elfTwo + 1 + recipes.get(elfTwo)) % recipes.size();
        }
        LOGGER.info("Part 2 : " + startOfSublist(recipes, LAST_RECIPES).get());
    }

    private static List<Integer> newRecipes(int elf1, int elf2) {
        List<Integer> result = new ArrayList<>();
        int tmp = elf1 + elf2;
        if (tmp / 10 > 0) {
            result.add(tmp / 10);
        }
        result.add(tmp % 10);
        return result;
    }

    private static <T> Optional<Integer> startOfSublist(List<T> l1, List<T> l2) {
        for (int i = 0; i < l1.size() - l2.size() + 1; i++) {
            if (l1.subList(i, i + l2.size()).equals(l2)) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

}