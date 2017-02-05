package org.deman.bot.rest;

import org.deman.bot.aiml.AimlParserException;
import org.deman.bot.engine.Engine;
import org.deman.bot.engine.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by deman on 05/02/17.
 */
@RestController
@RequestMapping("/chat/")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private String aimls;

    private Engine engine;

    @Autowired
    public ChatController(Engine engine,@Value("${aimls}") String aimls) {
        this.engine = engine;
        this.aimls = aimls;
    }

    @RequestMapping(path = "/submit", method = RequestMethod.POST)
    public ChatResponse newInput(@RequestBody ChatRequest chatRequest) {
        State state = chatRequest.getState();
        String input = chatRequest.getInput();
        if (chatRequest.getState() == null) {
            state = new State();
        }
        return new ChatResponse(state, engine.onNewUserInput(state, input).orElse(""));
    }

    @RequestMapping(path = "/reload", method = RequestMethod.GET)
    public void reload() {
        engine.unloadAiml();
        try {
            engine.loadAimlFilesFromPath(aimls);
        } catch (AimlParserException e) {
            logger.error(e.getMessage(), e);
        }
    }


}
