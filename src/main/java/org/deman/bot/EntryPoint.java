package org.deman.bot;

import org.deman.bot.aiml.AimlParserException;
import org.deman.bot.engine.Engine;
import org.deman.bot.engine.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.io.Console;
import java.util.Optional;

/**
 * Created by deman on 08/01/17.
 */

@SpringBootApplication
@EnableAutoConfiguration
public class EntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(Engine.class);

    @Bean
    public Engine engine(@Value("${aimls}") String aimls){
        Engine engine = new Engine();
        try {
            engine.loadAimlFilesFromPath(aimls);
        } catch (AimlParserException e) {
            logger.error(e.getMessage(),e);
        }
        return engine;
    }

    public static void main(String ... args) {
        SpringApplication.run(EntryPoint.class, args);
    }


/*
        Engine engine = new Engine();
        State state = new State();
        try {
            engine.loadAimlFilesFromPath("./src/main/resources/french-aiml-publish/");
        } catch (AimlParserException e) {
            e.printStackTrace();
        }

        Console console = System.console();
        if (console == null) {
            System.out.println("No console: non-interactive mode!");
            System.exit(0);
        }

        String line;
        do {
            System.out.print("# ");
            line = console.readLine();
            Optional<String> response = engine.onNewUserInput(state,line);
            System.out.println("> "+response.orElse("")+"\n");
        } while (!"quit".equals(line));
*/


}
