import org.deman.bot.engine.Context
import org.deman.bot.engine.Engine
import spock.lang.Specification

/**
 * Created by deman on 17/01/17.
 */
class OtherTagsSpec extends Specification {

    def "bot"(){
        given:
        def engine = new Engine()
        def context = new Context()
        engine.loadAimlFile("src/test/resources/otherTags.aiml")

        when:
        def value = engine.onNewUserInput(context,"BOT").get()

        then:
        value == "Mon nom est Jarvis"
    }

}
