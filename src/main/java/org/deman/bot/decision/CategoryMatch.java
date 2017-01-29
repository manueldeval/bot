package org.deman.bot.decision;

import org.deman.bot.rules.Category;
import org.paumard.streams.StreamsUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Created by deman on 22/01/17.
 */
public class CategoryMatch {

    private Category category;
    private LinkedList<Match> matches = new LinkedList<>();
    private List<String> patternStar;
    private String currentPattern;
    private List<String> topicStar;
    private String currentTopic;
    private List<String> thatStar;
    private String currentThat;

    private static final Predicate<Match> isThat = (Match m) -> m.getType() == MatchType.VALUE && m.getValue().equals("<that>");
    private static final Predicate<Match> isTopic = (Match m) -> m.getType() == MatchType.VALUE && m.getValue().equals("<topic>");

    public CategoryMatch(Category category, Match match) {
        this.category = category;
        this.matches.add(match);
    }


    public CategoryMatch(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public List<String> getPatternStar() {
        return patternStar;
    }

    public String getCurrentPattern() {
        return currentPattern;
    }

    public List<String> getTopicStar() {
        return topicStar;
    }

    public String getCurrentTopic() {
        return currentTopic;
    }

    public List<String> getThatStar() {
        return thatStar;
    }

    public String getCurrentThat() {
        return currentThat;
    }

    public LinkedList<Match> getMatches() {
        return matches;
    }

    public CategoryMatch pushMatch(Match match) {
        this.matches.addFirst(match);
        return this;
    }


    public CategoryMatch postInit() {
        List<Match> patternMatches = StreamsUtils.interrupt(this.getMatches().stream(), isThat).collect(toList());
        this.currentPattern = patternMatches.stream().map(Match::getValue).collect(joining(" "));
        this.patternStar = aggregateWilcards(patternMatches);

        List<Match> thatMatches = StreamsUtils.gate(StreamsUtils.interrupt(this.getMatches().stream(), isTopic), isThat).collect(toList());
        this.currentThat = thatMatches.stream().map(Match::getValue).collect(joining(" "));
        this.thatStar = aggregateWilcards(thatMatches);

        List<Match> topicMatches = StreamsUtils.gate(this.getMatches().stream(), isTopic).collect(toList());
        this.currentTopic = topicMatches.stream().map(Match::getValue).collect(joining(" "));
        this.topicStar = aggregateWilcards(topicMatches);

        return this;
    }


    private List<String> aggregateWilcards(List<Match> patternMatches) {
        LinkedList<Match> grouped = new LinkedList<>();
        grouped.add(Match.value(""));
        patternMatches.forEach(curr -> {
            Match last = grouped.getLast();
            if (last.getType() != curr.getType()) {
                grouped.add(new Match(curr.getType(), curr.getValue()));
            } else {
                last.setValue(last.getValue() + " " + curr.getValue());
            }
        });
        return grouped.stream()
                .filter(m -> m.getType() == MatchType.WILDCARD)
                .map(Match::getValue)
                .collect(toList());
    }

    @Override
    public String toString() {
        return "Category: " + this.getCategory().toString() + "\n" +
                "Matches: " + this.getMatches().stream().map(Match::toString).collect(joining(",")) + "\n" +
                "Pattern Aggregates : " + this.patternStar.stream().collect(joining(",")) + "\n" +
                "That    Aggregates : " + this.thatStar.stream().collect(joining(",")) + "\n" +
                "topic   Aggregates : " + this.topicStar.stream().collect(joining(","));
    }
}
