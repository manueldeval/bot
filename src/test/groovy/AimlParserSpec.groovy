import org.deman.bot.aiml.AimlParser
import org.deman.bot.engine.Engine
import org.deman.bot.tags.TagsRegistry
import spock.lang.Specification

/**
 * Created by deman on 08/01/17.
 */
class AimlParserSpec extends Specification {

    def "Parse"() {
        expect:
        AimlParser.parse(new File("src/main/resources/humor_ed_mini.aiml"), new TagsRegistry());
    }

    def "Engine"() {
        given:
        def engine = new Engine()

        when:
        engine.loadAimlFile("src/main/resources/humor_ed.aiml")

        then:
        1 == 1

    }
}
