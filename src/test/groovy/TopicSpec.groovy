import org.deman.bot.engine.Engine
import org.deman.bot.engine.State
import spock.lang.Specification

/**
 * Created by deman on 17/01/17.
 */
class TopicSpec extends Specification {

    def "Topic"() {
        given:
        def engine = new Engine()
        def state = new State()

        when:
        engine.loadAimlFile("src/test/resources/topic.aiml")
        def response1 = engine.onNewUserInput(state,"toto")
        def response2 = engine.onNewUserInput(state,"toto")

        then:
        response1.isPresent()
        response1.get().trim() == "outside"
        response2.isPresent()
        response2.get().trim() == "inside"
    }
}
