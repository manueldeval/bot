import org.deman.bot.engine.Engine
import org.deman.bot.engine.State
import spock.lang.Specification

/**
 * Created by deman on 17/01/17.
 */
class ThatSpec extends Specification {

    def "That"() {
        given:
        def engine = new Engine()
        def state = new State()

        when:
        engine.loadAimlFile("src/test/resources/that.aiml")
        def response1 = engine.onNewUserInput(state,"toto")
        def response2 = engine.onNewUserInput(state,"toto")

        then:
        response1.isPresent()
        response1.get().trim() == "REPONSE"
        response2.isPresent()
        response2.get().trim() == "REPONSE2"
    }
}
