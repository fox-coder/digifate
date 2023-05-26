package com.fox.digifate.data.repository;

import com.fox.digifate.data.entity.CardCategory;
import com.fox.digifate.data.entity.SpecificationCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecificationCardRepository extends JpaRepository<SpecificationCard, Long>, JpaSpecificationExecutor<SpecificationCard> {

    List<SpecificationCard> findByCardCategoryAndInUseIsFalse(CardCategory cardCategory);

    SpecificationCard findByTitle(String title);

}
