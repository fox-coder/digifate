package com.fox.digifate.data.service;

import com.fox.digifate.data.entity.CardCategory;
import com.fox.digifate.data.entity.SpecificationCard;
import com.fox.digifate.data.repository.SpecificationCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class SpecificationCardService {

    private final SpecificationCardRepository specificationCardRepository;

    @Autowired
    public SpecificationCardService(SpecificationCardRepository specificationCardRepository) {
        this.specificationCardRepository = specificationCardRepository;
    }

    public Optional<SpecificationCard> get(Long id) {
        return specificationCardRepository.findById(id);
    }

    public SpecificationCard getCardByTitle(String title) {
        return specificationCardRepository.findByTitle(title);
    }

    public SpecificationCard update(SpecificationCard card) {
        return specificationCardRepository.save(card);
    }

    public SpecificationCard drawCardForCategory(CardCategory cardCategory) {
        List<SpecificationCard> cards = getCardsForCategory(cardCategory);
        Random rand = new Random();
        SpecificationCard randomCard = cards.get(rand.nextInt(cards.size()));
        randomCard.setInUse(true);
        return update(randomCard);
    }

    public SpecificationCard redrawCard(SpecificationCard card) {
        SpecificationCard newCard = drawCardForCategory(card.getCardCategory());
        card.setInUse(false);
        update(card);
        return newCard;
    }

    public void returnCardToDeck(SpecificationCard card) {
        card.setInUse(false);
        specificationCardRepository.save(card);
    }

    public void markCardDrawn(SpecificationCard card) {
        card.setInUse(true);
        specificationCardRepository.save(card);
    }

    public void returnCardsToDeck(List<SpecificationCard> cards) {
        cards.forEach(baseCard -> baseCard.setInUse(false));
        specificationCardRepository.saveAll(cards);
    }

    public List<SpecificationCard> getCardsForCategory(CardCategory cardCategory) {
        return specificationCardRepository.findByCardCategoryAndInUseIsFalse(cardCategory);
    }

}
