import org.deman.bot.engine.Context
import org.deman.bot.engine.Engine
import org.deman.bot.engine.State
import spock.lang.Specification

/**
 * Created by deman on 17/01/17.
 */
class OtherTagsSpec extends Specification {

    def "bot"(){
        given:
        def engine = new Engine()
        def state = new State()
        engine.loadAimlFile("src/test/resources/otherTags.aiml")

        when:
        def value = engine.onNewUserInput(state,"BOT").get()

        then:
        value == "Mon nom est Jarvis"
    }

}
