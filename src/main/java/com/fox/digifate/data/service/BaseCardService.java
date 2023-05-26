package com.fox.digifate.data.service;

import com.fox.digifate.data.entity.BaseCard;
import com.fox.digifate.data.entity.CardCategory;
import com.fox.digifate.data.repository.BaseCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class BaseCardService {

    private final BaseCardRepository baseCardRepository;

    @Autowired
    public BaseCardService(BaseCardRepository baseCardRepository) {
        this.baseCardRepository = baseCardRepository;
    }

    public Optional<BaseCard> get(Long id) {
        return baseCardRepository.findById(id);
    }

    public BaseCard update(BaseCard baseCard) {
        return baseCardRepository.save(baseCard);
    }

    public BaseCard drawCardForCategory(CardCategory cardCategory) {
        List<BaseCard> cards = getCardsForCategory(cardCategory);
        Random rand = new Random();
        BaseCard randomCard = cards.get(rand.nextInt(cards.size()));
        randomCard.setInUse(true);
        return update(randomCard);
    }

    public BaseCard redrawCard(BaseCard card) {
        BaseCard newCard = drawCardForCategory(card.getCardCategory());
        card.setInUse(false);
        update(card);
        return newCard;
    }

    public void returnCardToDeck(BaseCard baseCard) {
        baseCard.setInUse(false);
        baseCardRepository.save(baseCard);
    }

    public void markCardDrawn(BaseCard baseCard) {
        baseCard.setInUse(true);
        baseCardRepository.save(baseCard);
    }

    public void returnCardsToDeck(List<BaseCard> baseCards) {
        baseCards.forEach(baseCard -> baseCard.setInUse(false));
        baseCardRepository.saveAll(baseCards);
    }

    public List<BaseCard> getCardsForCategory(CardCategory cardCategory) {
        return baseCardRepository.findByCardCategoryAndInUseIsFalse(cardCategory);
    }

}
