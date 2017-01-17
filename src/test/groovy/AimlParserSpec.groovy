import org.deman.bot.aiml.AimlParser
import org.deman.bot.aiml.AimlParserException
import org.deman.bot.engine.Context
import org.deman.bot.engine.Engine
import org.deman.bot.tags.TagsRegistry
import spock.lang.Specification

/**
 * Created by deman on 08/01/17.
 */
class AimlParserSpec extends Specification {

    def "Parse a real aiml file with no error does not throw any exception."() {
        when:
        AimlParser.parse(new File("src/main/resources/humor_ed.aiml"), new TagsRegistry());

        then:
        notThrown AimlParserException
    }

    def "No database, empty response"() {
        given:
        def engine = new Engine()
        def context = new Context()

        when:
        def response = engine.onNewUserInput(context,"HELLO WORLD")

        then:
        !response.isPresent()
    }

    def "Simple match"() {
        given:
        def engine = new Engine()
        def context = new Context()

        when:
        engine.loadAimlFile("src/main/resources/test.aiml")
        def response = engine.onNewUserInput(context,"HELLO WORLD")

        then:
        response.isPresent()
        response.get() == "Bonjour monde."
    }

    def "Simple match, case insensitive."() {
        given:
        def engine = new Engine()
        def context = new Context()

        when:
        engine.loadAimlFile("src/main/resources/test.aiml")
        def response = engine.onNewUserInput(context,"hello WORLD")

        then:
        response.isPresent()
        response.get() == "Bonjour monde."
    }

    def "Simple match, extra spaces."() {
        given:
        def engine = new Engine()
        def context = new Context()

        when:
        engine.loadAimlFile("src/main/resources/test.aiml")
        def response = engine.onNewUserInput(context,"   HELLO  WORLD   ")

        then:
        response.isPresent()
        response.get() == "Bonjour monde."
    }

    def "No strict match, * match!"() {
        given:
        def engine = new Engine()
        def context = new Context()

        when:
        engine.loadAimlFile("src/main/resources/test.aiml")
        def response = engine.onNewUserInput(context,"   qqqq  WORLD   ")

        then:
        response.isPresent()
        response.get() == "J'AI RIEN COMPRIS!"
    }

    def "More that one sentences."() {
        given:
        def engine = new Engine()
        def context = new Context()

        when:
        engine.loadAimlFile("src/main/resources/test.aiml")
        def response = engine.onNewUserInput(context,"HELLO  WORLD. Hello world? Hoho!")

        then:
        response.isPresent()
        response.get() == "Bonjour monde. Bonjour monde. J'AI RIEN COMPRIS!"
    }
}
