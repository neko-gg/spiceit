package gg.neko.spiceit.example.agent;

import gg.neko.spiceit.annotation.LogIt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class SpiceItExampleAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpiceItExampleAgent.class);
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        shouldLogThisMethod(RANDOM.nextInt(1000));
        shouldNotLogThisMethod();
    }

    @LogIt
    private static void shouldLogThisMethod(int parameter) {
        LOGGER.info("If you ran this program with the -javaagent option correctly set, " +
                    "the entry and exit to this method should have been logged.");
        LOGGER.info("This method was invoked with {} as its argument. " +
                    "SpiceIt should have correctly logged it.",
                    parameter);
    }

    private static void shouldNotLogThisMethod() {
        LOGGER.info("Since it's not annotated, this other method should not have been logged.");
    }

}
