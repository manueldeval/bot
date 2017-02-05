import org.deman.bot.engine.Context
import org.deman.bot.engine.Engine
import org.deman.bot.engine.State
import spock.lang.Specification

/**
 * Created by deman on 17/01/17.
 */
class MatchStackSpec extends Specification {

    def "L1"(String input,String output) {
        given:
        def engine = new Engine()
        def state = new State()
        engine.loadAimlFile("src/test/resources/wildcard.aiml")

        expect:
        engine.onNewUserInput(state,input).get() == output

        where:
        input             | output
        "l1 tata toto"    | "L1 WILDCARD"
    }

    def "L0"(String input,String output) {
        given:
        def engine = new Engine()
        def state = new State()
        engine.loadAimlFile("src/test/resources/wildcard.aiml")

        expect:
        engine.onNewUserInput(state,input).get() == output

        where:
        input               | output
        "l0 other"          | "L0 OTHER"
        "l0 toto other"     | "L0 OTHER"
    }
}
