import org.deman.bot.aiml.AimlParser
import org.deman.bot.engine.Context
import org.deman.bot.engine.Engine
import org.deman.bot.tags.TagsRegistry
import spock.lang.Specification

/**
 * Created by deman on 08/01/17.
 */
class AimlParserSpec extends Specification {

    def "Parse"() {
        expect:
        AimlParser.parse(new File("src/main/resources/test.aiml"), new TagsRegistry());
    }

    def "Engine"() {
        given:
        def engine = new Engine()
        def context = new Context()

        when:
        engine.loadAimlFile("src/main/resources/test.aiml")
        println(engine.onNewSentence(context,"HELLO WORLD"))
        println(engine.onNewSentence(context,"BONJOUR"))


        then:
        1 == 1

    }
}
