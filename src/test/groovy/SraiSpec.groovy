import org.deman.bot.aiml.AimlParser
import org.deman.bot.aiml.AimlParserException
import org.deman.bot.engine.Context
import org.deman.bot.engine.Engine
import org.deman.bot.engine.State
import org.deman.bot.tags.TagsRegistry
import spock.lang.Specification

/**
 * Created by deman on 17/01/17.
 */
class SraiSpec extends Specification {

    def "SRAI"() {
        given:
        def engine = new Engine()
        def state = new State()

        when:
        engine.loadAimlFile("src/test/resources/srai.aiml")
        def response = engine.onNewUserInput(state,"SRAI2")

        then:
        response.isPresent()
        response.get() == "21srai12"
    }
}
