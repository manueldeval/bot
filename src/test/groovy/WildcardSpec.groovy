import org.deman.bot.engine.Context
import org.deman.bot.engine.Engine
import org.deman.bot.engine.State
import spock.lang.Specification

/**
 * Created by deman on 17/01/17.
 */
class WildcardSpec extends Specification {

    def "L1"(String input,String output) {
        given:
        def engine = new Engine()
        def state = new State()
        engine.loadAimlFile("src/test/resources/wildcard.aiml")

        expect:
        engine.onNewUserInput(state,input).get() == output

        where:
        input                   | output
        "l1 toto tutu other"    | "L1 OTHER"
        "l1 toto tutu tata"     | "L1 WILDCARD"
        "l1 strict"             | "L1 STRICT"
    }

    def "H1"(String input,String output) {
        given:
        def engine = new Engine()
        def state = new State()
        engine.loadAimlFile("src/test/resources/wildcard.aiml")

        expect:
        engine.onNewUserInput(state,input).get() == output

        where:
        input                   | output
        "h1 toto tutu other"    | "H1 WILDCARD"
        "h1 toto tutu tata"     | "H1 WILDCARD"
        "h1 strict"             | "H1 WILDCARD"
    }

    def "L0"(String input,String output) {
        given:
        def engine = new Engine()
        def state = new State()
        engine.loadAimlFile("src/test/resources/wildcard.aiml")

        expect:
        engine.onNewUserInput(state,input).get() == output

        where:
        input                   | output
        "l0 toto tutu other"    | "L0 OTHER"
        "l0 toto tutu tata"     | "L0 WILDCARD"
        "l0 strict"             | "L0 STRICT"
        "l0 other"              | "L0 OTHER"
        "l0"                    | "L0 WILDCARD"
    }

    def "H0"(String input,String output) {
        given:
        def engine = new Engine()
        def state = new State()
        engine.loadAimlFile("src/test/resources/wildcard.aiml")

        expect:
        println("== "+input+" ==")
        engine.onNewUserInput(state,input).get() == output

        where:
        input                   | output
        "h0 toto tutu other"    | "H0 WILDCARD"
        "h0 toto tutu tata"     | "H0 WILDCARD"
        "h0 strict"             | "H0 WILDCARD"
        "h0 other"              | "H0 WILDCARD"
        "h0"                    | "H0 WILDCARD"
    }
}
