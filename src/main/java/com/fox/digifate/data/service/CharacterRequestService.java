package com.fox.digifate.data.service;

import com.fox.digifate.data.entity.BaseCard;
import com.fox.digifate.data.entity.CardCategory;
import com.fox.digifate.data.entity.CharacterRequest;
import com.fox.digifate.data.entity.SpecificationCard;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.fox.digifate.data.entity.CardCategory.BOND;
import static com.fox.digifate.data.entity.CardCategory.EVENT;

@Service
public class CharacterRequestService {

    private final BaseCardService baseCardService;
    private final SpecificationCardService specificationCardService;

    private static final List<CardCategory> SPEC_CATEGORIES = List.of(BOND, EVENT);

    @Autowired
    public CharacterRequestService(BaseCardService baseCardService, SpecificationCardService specificationCardService) {
        this.baseCardService = baseCardService;
        this.specificationCardService = specificationCardService;
    }

    public Map<String, List<BaseCard>> answerCardQuestions(SpecificationCard sCard) {
        Map<String, List<BaseCard>> answers = new HashMap<>();
        if (StringUtils.isNotBlank(sCard.getFirstQuestion())) {
            List<BaseCard> drawnCards = combineDecksAndDrawCards(sCard.getFirstQuestionCategoryArray());
            answers.put("first", drawnCards);
        }
        if (StringUtils.isNotBlank(sCard.getSecondQuestion())) {
            List<BaseCard> drawnCards = combineDecksAndDrawCards(sCard.getSecondQuestionCategoryArray());
            answers.put("second", drawnCards);
        }

        return answers;
    }

    private List<BaseCard> combineDecksAndDrawCards(List<CardCategory> categories) {
        List<BaseCard> drawnCards = new ArrayList<>();
        List<BaseCard> deck = new ArrayList<>();
        for (CardCategory cardCategory : categories) {
            if (SPEC_CATEGORIES.contains(cardCategory)) {
                deck.addAll(specificationCardService.getCardsForCategory(cardCategory));
            } else {
                deck.addAll(baseCardService.getCardsForCategory(cardCategory));
            }
        }

        Random rand = new Random();
        for (int i = 0; i < categories.size(); i++) {
            int randomIndex = rand.nextInt(deck.size());
            drawnCards.add(deck.get(randomIndex));
            deck.remove(randomIndex);
        }
        for (BaseCard drawnCard : drawnCards) {
            if (!SPEC_CATEGORIES.contains(drawnCard.getCardCategory())) {
                drawnCard.setInUse(true);
                baseCardService.update(drawnCard);
            } else {
                SpecificationCard card = specificationCardService.getCardByTitle(drawnCard.getTitle());
                card.setInUse(true);
                specificationCardService.update(card);
            }
        }
        return drawnCards;
    }

    public void returnUsedCardsToDeck(CharacterRequest characterRequest) {
        List<BaseCard> usedBasedCards = new ArrayList<>();
        if (characterRequest.getCharacter() != null) {
            usedBasedCards.add(characterRequest.getCharacter());
        }
        if (characterRequest.getOrigins() != null) {
            usedBasedCards.add(characterRequest.getOrigins());
        }
        if (characterRequest.getBackground() != null) {
            usedBasedCards.add(characterRequest.getBackground());
        }
        if (CollectionUtils.isNotEmpty(usedBasedCards)) {
            baseCardService.returnCardsToDeck(usedBasedCards);
        }
        List<SpecificationCard> usedSpecificationCards = new ArrayList<>();
        if (characterRequest.getFirstBond() != null) {
            usedSpecificationCards.add(characterRequest.getFirstBond());
        }
        if (characterRequest.getSecondBond() != null) {
            usedSpecificationCards.add(characterRequest.getSecondBond());
        }
        if (characterRequest.getMarkOfThePast() != null) {
            usedSpecificationCards.add(characterRequest.getMarkOfThePast());
        }
        if (characterRequest.getImpetus() != null) {
            usedSpecificationCards.add(characterRequest.getImpetus());
        }
        if (CollectionUtils.isNotEmpty(usedSpecificationCards)) {
            specificationCardService.returnCardsToDeck(usedSpecificationCards);
        }

        if (MapUtils.isNotEmpty(characterRequest.getFirstBondAnswers())) {
            returnUsedCardsToDeck(characterRequest.getFirstBondAnswers().values().stream().flatMap(List::stream).toList());
        }
        if (MapUtils.isNotEmpty(characterRequest.getSecondBondAnswers())) {
            returnUsedCardsToDeck(characterRequest.getSecondBondAnswers().values().stream().flatMap(List::stream).toList());
        }
        if (MapUtils.isNotEmpty(characterRequest.getMarkOfThePastAnswers())) {
            returnUsedCardsToDeck(characterRequest.getMarkOfThePastAnswers().values().stream().flatMap(List::stream).toList());
        }
        if (MapUtils.isNotEmpty(characterRequest.getImpetusAnswers())) {
            returnUsedCardsToDeck(characterRequest.getImpetusAnswers().values().stream().flatMap(List::stream).toList());
        }
    }

    private void returnUsedCardsToDeck(List<BaseCard> usedCards) {
        for (BaseCard card: usedCards) {
            if (!SPEC_CATEGORIES.contains(card.getCardCategory())) {
                baseCardService.returnCardToDeck(card);
            } else {
                SpecificationCard sCard = specificationCardService.getCardByTitle(card.getTitle());
                specificationCardService.returnCardToDeck(sCard);
            }
        }
    }

    public String generateResultingRequestLayout(CharacterRequest characterRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        if (characterRequest.getCharacter() != null) {
            stringBuilder.append("Character: ").append(characterRequest.getCharacter().getTitle()).append(System.lineSeparator());
        }

        if (characterRequest.getOrigins() != null) {
            stringBuilder.append("Origins: ").append(characterRequest.getOrigins().getTitle()).append(System.lineSeparator());
        }

        if (characterRequest.getBackground() != null) {
            stringBuilder.append("Background: ").append(characterRequest.getBackground().getTitle()).append(System.lineSeparator());
        }

        if (characterRequest.getFirstBond() != null) {
            stringBuilder.append("First bond: ").append(characterRequest.getFirstBond().getTitle());
            // checking first question
            if (StringUtils.isNotBlank(characterRequest.getFirstBond().getFirstQuestion()) &&
                    MapUtils.isNotEmpty(characterRequest.getFirstBondAnswers()) &&
                    CollectionUtils.isNotEmpty(characterRequest.getFirstBondAnswers().get("first"))) {
                stringBuilder.append(" - ").append(characterRequest.getFirstBond().getFirstQuestion()).append(" - ");
                for (BaseCard card : characterRequest.getFirstBondAnswers().get("first")) {
                    stringBuilder.append(card.getTitle()).append(" - ");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 2);
            }
            // checking second question
            if (StringUtils.isNotBlank(characterRequest.getFirstBond().getSecondQuestion()) &&
                    MapUtils.isNotEmpty(characterRequest.getFirstBondAnswers()) &&
                    CollectionUtils.isNotEmpty(characterRequest.getFirstBondAnswers().get("second"))) {
                stringBuilder.append(" - ").append(characterRequest.getFirstBond().getSecondQuestion()).append(" - ");
                for (BaseCard card : characterRequest.getFirstBondAnswers().get("second")) {
                    stringBuilder.append(card.getTitle()).append(" - ");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 2);
            }
            stringBuilder.append(System.lineSeparator());
        }

        if (characterRequest.getSecondBond() != null) {
            stringBuilder.append("Second bond: ").append(characterRequest.getSecondBond().getTitle());
            // checking first question
            if (StringUtils.isNotBlank(characterRequest.getSecondBond().getFirstQuestion()) &&
                    MapUtils.isNotEmpty(characterRequest.getSecondBondAnswers()) &&
                    CollectionUtils.isNotEmpty(characterRequest.getSecondBondAnswers().get("first"))) {
                stringBuilder.append(" - ").append(characterRequest.getSecondBond().getFirstQuestion()).append(" - ");
                for (BaseCard card : characterRequest.getSecondBondAnswers().get("first")) {
                    stringBuilder.append(card.getTitle()).append(" - ");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 2);
            }
            // checking second question
            if (StringUtils.isNotBlank(characterRequest.getSecondBond().getSecondQuestion()) &&
                    MapUtils.isNotEmpty(characterRequest.getSecondBondAnswers()) &&
                    CollectionUtils.isNotEmpty(characterRequest.getSecondBondAnswers().get("second"))) {
                stringBuilder.append(" - ").append(characterRequest.getSecondBond().getSecondQuestion()).append(" - ");
                for (BaseCard card : characterRequest.getSecondBondAnswers().get("second")) {
                    stringBuilder.append(card.getTitle()).append(" - ");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 2);
            }
            stringBuilder.append(System.lineSeparator());
        }

        if (characterRequest.getMarkOfThePast() != null) {
            stringBuilder.append("Mark of the past: ").append(characterRequest.getMarkOfThePast().getTitle());
            // checking first question
            if (StringUtils.isNotBlank(characterRequest.getMarkOfThePast().getFirstQuestion()) &&
                    MapUtils.isNotEmpty(characterRequest.getMarkOfThePastAnswers()) &&
                    CollectionUtils.isNotEmpty(characterRequest.getMarkOfThePastAnswers().get("first"))) {
                stringBuilder.append(" - ").append(characterRequest.getMarkOfThePast().getFirstQuestion()).append(" - ");
                for (BaseCard card : characterRequest.getMarkOfThePastAnswers().get("first")) {
                    stringBuilder.append(card.getTitle()).append(" - ");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 2);
            }
            // checking second question
            if (StringUtils.isNotBlank(characterRequest.getMarkOfThePast().getSecondQuestion()) &&
                    MapUtils.isNotEmpty(characterRequest.getMarkOfThePastAnswers()) &&
                    CollectionUtils.isNotEmpty(characterRequest.getMarkOfThePastAnswers().get("second"))) {
                stringBuilder.append(" - ").append(characterRequest.getMarkOfThePast().getSecondQuestion()).append(" - ");
                for (BaseCard card : characterRequest.getMarkOfThePastAnswers().get("second")) {
                    stringBuilder.append(card.getTitle()).append(" - ");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 2);
            }
            stringBuilder.append(System.lineSeparator());
        }

        if (characterRequest.getImpetus() != null) {
            stringBuilder.append("Impetus: ").append(characterRequest.getImpetus().getTitle());
            // checking first question
            if (StringUtils.isNotBlank(characterRequest.getImpetus().getFirstQuestion()) &&
                    MapUtils.isNotEmpty(characterRequest.getImpetusAnswers()) &&
                    CollectionUtils.isNotEmpty(characterRequest.getImpetusAnswers().get("first"))) {
                stringBuilder.append(" - ").append(characterRequest.getImpetus().getFirstQuestion()).append(" - ");
                for (BaseCard card : characterRequest.getImpetusAnswers().get("first")) {
                    stringBuilder.append(card.getTitle()).append(" - ");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 2);
            }
            // checking second question
            if (StringUtils.isNotBlank(characterRequest.getImpetus().getSecondQuestion()) &&
                    MapUtils.isNotEmpty(characterRequest.getImpetusAnswers()) &&
                    CollectionUtils.isNotEmpty(characterRequest.getImpetusAnswers().get("second"))) {
                stringBuilder.append(" - ").append(characterRequest.getImpetus().getSecondQuestion()).append(" - ");
                for (BaseCard card : characterRequest.getImpetusAnswers().get("second")) {
                    stringBuilder.append(card.getTitle()).append(" - ");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 2);
            }
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

}
