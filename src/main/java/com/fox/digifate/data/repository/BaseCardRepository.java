package com.fox.digifate.data.repository;

import com.fox.digifate.data.entity.BaseCard;
import com.fox.digifate.data.entity.CardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseCardRepository extends JpaRepository<BaseCard, Long>, JpaSpecificationExecutor<BaseCard> {

    List<BaseCard> findByCardCategoryAndInUseIsFalse(CardCategory cardCategory);

}
