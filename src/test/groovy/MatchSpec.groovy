import org.deman.bot.engine.Context
import org.deman.bot.engine.Engine
import org.deman.bot.engine.State
import org.paumard.streams.StreamsUtils
import spock.lang.Specification

import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Created by deman on 17/01/17.
 */
class MatchSpec extends Specification {

    def "Match"() {
        given:
        def engine = new Engine()
        def state = new State()

        when:
        engine.loadAimlFile("src/test/resources/match.aiml")
        def response = engine.onNewUserInput(state,"match Toto est mon nom?")

        then:
        response.isPresent()
        response.get() == "Bonjour Toto"
    }

    def "Match with sr"() {
        given:
        def engine = new Engine()
        def state = new State()

        when:
        engine.loadAimlFile("src/test/resources/match.aiml")
        def response = engine.onNewUserInput(state,"Toc toc match Toto est mon nom?")

        then:
        response.isPresent()
        response.get() == "Bonjour Toto"
    }
}
