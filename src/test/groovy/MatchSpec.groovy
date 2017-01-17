import org.deman.bot.engine.Context
import org.deman.bot.engine.Engine
import spock.lang.Specification

/**
 * Created by deman on 17/01/17.
 */
class MatchSpec extends Specification {

    def "Match"() {
        given:
        def engine = new Engine()
        def context = new Context()

        when:
        engine.loadAimlFile("src/main/resources/test.aiml")
        def response = engine.onNewUserInput(context,"match Toto est mon nom?")

        then:
        response.isPresent()
        response.get() == "Hallo Toto"
    }
}
