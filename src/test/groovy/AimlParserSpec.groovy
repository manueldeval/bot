import org.deman.bot.aiml.AimlParser
import org.deman.bot.aiml.AimlParserException
import org.deman.bot.engine.Context
import org.deman.bot.engine.Engine
import org.deman.bot.engine.State
import org.deman.bot.tags.TagsRegistry
import spock.lang.Specification

/**
 * Created by deman on 08/01/17.
 */
class AimlParserSpec extends Specification {

    def "Parse a real aiml file with no error does not throw any exception."() {
        when:
        AimlParser.parse(new File("src/test/resources/humor_ed.aiml"), new TagsRegistry());

        then:
        notThrown AimlParserException
    }

    def "No database, empty response"() {
        given:
        def engine = new Engine()
        def state = new State()

        when:
        def response = engine.onNewUserInput(state,"HELLO WORLD")

        then:
        !response.isPresent()
    }

    def "Simple match"() {
        given:
        def engine = new Engine()
        def state = new State()

        when:
        engine.loadAimlFile("src/test/resources/simple.aiml")
        def response = engine.onNewUserInput(state,"HELLO WORLD")

        then:
        response.isPresent()
        response.get() == "Bonjour monde."
    }

    def "Simple match, case insensitive."() {
        given:
        def engine = new Engine()
        def state = new State()

        when:
        engine.loadAimlFile("src/test/resources/simple.aiml")
        def response = engine.onNewUserInput(state,"hello WORLD")

        then:
        response.isPresent()
        response.get() == "Bonjour monde."
    }

    def "Simple match, extra spaces."() {
        given:
        def engine = new Engine()
        def state = new State()

        when:
        engine.loadAimlFile("src/test/resources/simple.aiml")
        def response = engine.onNewUserInput(state,"   HELLO  WORLD   ")

        then:
        response.isPresent()
        response.get() == "Bonjour monde."
    }

    def "No strict match, * match!"() {
        given:
        def engine = new Engine()
        def state = new State()

        when:
        engine.loadAimlFile("src/test/resources/simple.aiml")
        def response = engine.onNewUserInput(state,"   qqqq  WORLD   ")

        then:
        response.isPresent()
        response.get() == "J'AI RIEN COMPRIS!"
    }

    def "More that one sentences."() {
        given:
        def engine = new Engine()
        def state = new State()

        when:
        engine.loadAimlFile("src/test/resources/simple.aiml")
        def response = engine.onNewUserInput(state,"HELLO  WORLD. Hello world? Hoho!")

        then:
        response.isPresent()
        response.get() == "Bonjour monde. Bonjour monde. J'AI RIEN COMPRIS!"
    }
}
