package com.fox.digifate.views.generatecharacter;

import com.fox.digifate.data.entity.BaseCard;
import com.fox.digifate.data.entity.CardCategory;
import com.fox.digifate.data.entity.CharacterRequest;
import com.fox.digifate.data.entity.SpecificationCard;
import com.fox.digifate.data.service.BaseCardService;
import com.fox.digifate.data.service.CharacterRequestService;
import com.fox.digifate.data.service.SpecificationCardService;
import com.fox.digifate.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Generate character")
@Route(value = "generate", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class GenerateCharacterView extends Div {

    private final CharacterRequestService characterRequestService;
    private final BaseCardService baseCardService;
    private final SpecificationCardService specificationCardService;

    private final ComboBox<BaseCard> selectCharacter = new ComboBox<>();
    private final ComboBox<BaseCard> selectOrigins = new ComboBox<>();
    private final ComboBox<BaseCard> selectBackground = new ComboBox<>();
    private final ComboBox<SpecificationCard> selectFirstBond = new ComboBox<>();
    private final List<TextField> firstBondAnswersRow = new ArrayList<>();
    private final ComboBox<SpecificationCard> selectSecondBond = new ComboBox<>();
    private final List<TextField> secondBondAnswersRow = new ArrayList<>();
    private final ComboBox<SpecificationCard> selectMarkOfThePast = new ComboBox<>();
    private final List<TextField> markOfThePastAnswersFirstRow = new ArrayList<>();
    private final List<TextField> markOfThePastAnswersSecondRow = new ArrayList<>();
    private final ComboBox<SpecificationCard> selectImpetus = new ComboBox<>();
    private final List<TextField> impetusAnswersFirstRow = new ArrayList<>();
    private final List<TextField> impetusAnswersSecondRow = new ArrayList<>();
    private final TextArea resultingRequest = new TextArea();

    private final Button reset = new Button("Reset");
    private final Button generate = new Button("Generate resulting text");

    private final Binder<CharacterRequest> binder = new Binder<>(CharacterRequest.class);

    public GenerateCharacterView(CharacterRequestService characterRequestService,
                                 BaseCardService baseCardService, SpecificationCardService specificationCardService) {
        this.characterRequestService = characterRequestService;
        this.baseCardService = baseCardService;
        this.specificationCardService = specificationCardService;

        addClassName("generatecharacter-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        updateLists();

        clearForm();
    }

    private void clearForm() {
        binder.setBean(new CharacterRequest());
        selectCharacter.clear();
        selectOrigins.clear();
        selectBackground.clear();
        selectFirstBond.clear();
        selectSecondBond.clear();
        selectMarkOfThePast.clear();
        selectImpetus.clear();
        clearFirstBondAnswers();
        clearSecondBondAnswers();
        clearMarkOfThePastAnswers();
        clearImpetusAnswers();
        resultingRequest.clear();
    }

    private Component createTitle() {
        return new H3("Generate character profile");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        Component firstBondAnswersRowLayout = createFirstBondAnswersRow();
        firstBondAnswersRowLayout.setVisible(false);
        Component secondBondAnswersRowLayout = createSecondBondAnswersRow();
        secondBondAnswersRowLayout.setVisible(false);
        Component markOfThePastAnswersFirstRowLayout = createMarkOfThePastAnswersFirstRow();
        markOfThePastAnswersFirstRowLayout.setVisible(false);
        Component markOfThePastAnswersSecondRowLayout = createMarkOfThePastAnswersSecondRow();
        markOfThePastAnswersSecondRowLayout.setVisible(false);
        Component impetusAnswersFirstRowLayout = createImpetusAnswersFirstRow();
        impetusAnswersFirstRowLayout.setVisible(false);
        Component impetusAnswersSecondRowLayout = createImpetusAnswersSecondRow();
        impetusAnswersSecondRowLayout.setVisible(false);

        // Base cards

        //----- Character -----
        selectCharacter.setItemLabelGenerator(GenerateCharacterView::baseCardView);
        selectCharacter.addValueChangeListener(select -> {
            binder.getBean().setCharacter(select.getValue());
            if (select.getValue() != null) {
                baseCardService.markCardDrawn(select.getValue());
            }
            if (select.getOldValue() != null) {
                baseCardService.returnCardToDeck(select.getOldValue());
            }
        });

        Button drawCharacterCardButton = new Button("Draw card");
        drawCharacterCardButton.setMaxWidth("200px");
        drawCharacterCardButton.addClickListener(event -> {
            if (selectCharacter.getValue() == null) {
                BaseCard card = baseCardService.drawCardForCategory(CardCategory.PERSONALITY);
                selectCharacter.setValue(card);
            } else {
                selectCharacter.setValue(baseCardService.redrawCard(selectCharacter.getValue()));
            }
        });

        //----- Origins -----
        selectOrigins.setItemLabelGenerator(GenerateCharacterView::baseCardView);
        selectOrigins.addValueChangeListener(select -> {
            binder.getBean().setOrigins(select.getValue());
            if (select.getValue() != null) {
                baseCardService.markCardDrawn(select.getValue());
            }
            if (select.getOldValue() != null) {
                baseCardService.returnCardToDeck(select.getOldValue());
            }
        });

        Button drawOriginsCardButton = new Button("Draw card");
        drawOriginsCardButton.setMaxWidth("200px");
        drawOriginsCardButton.addClickListener(event -> {
            if (selectOrigins.getValue() == null) {
                BaseCard card = baseCardService.drawCardForCategory(CardCategory.PLACE);
                selectOrigins.setValue(card);
            } else {
                selectOrigins.setValue(baseCardService.redrawCard(selectOrigins.getValue()));
            }
        });

        //----- Background -----
        selectBackground.setItemLabelGenerator(GenerateCharacterView::baseCardView);
        selectBackground.addValueChangeListener(select -> {
            binder.getBean().setBackground(select.getValue());
            if (select.getValue() != null) {
                baseCardService.markCardDrawn(select.getValue());
            }
            if (select.getOldValue() != null) {
                baseCardService.returnCardToDeck(select.getOldValue());
            }
        });

        Button drawBackroundCardButton = new Button("Draw card");
        drawBackroundCardButton.setMaxWidth("200px");
        drawBackroundCardButton.addClickListener(event -> {
            if (selectBackground.getValue() == null) {
                BaseCard card = baseCardService.drawCardForCategory(CardCategory.BACKGROUND);
                selectBackground.setValue(card);
            } else {
                selectBackground.setValue(baseCardService.redrawCard(selectBackground.getValue()));
            }
        });

        // Specification cards

        //----- First Bond -----
        Button drawFirstBondSupportCardsButton = new Button("Draw support cards");
        drawFirstBondSupportCardsButton.setEnabled(false);
        drawFirstBondSupportCardsButton.addClickListener(event -> {
            Map<String, List<BaseCard>> answersMap = characterRequestService.answerCardQuestions(selectFirstBond.getValue());
            binder.getBean().setFirstBondAnswers(answersMap);
            List<BaseCard> answers = answersMap.get("first");
            for (int i=0; i<answers.size(); i++) {
                firstBondAnswersRow.get(i+1).setValue(answers.get(i).getTitle());
            }
        });

        selectFirstBond.setItemLabelGenerator(GenerateCharacterView::specificationCardView);
        selectFirstBond.addValueChangeListener(select -> {
            binder.getBean().setFirstBond(select.getValue());
            if (select.getOldValue() != null) {
                specificationCardService.returnCardToDeck(select.getOldValue());
            }
            binder.getBean().setFirstBondAnswers(new HashMap<>());
            clearFirstBondAnswers();
            if (select.getValue() != null) {
                specificationCardService.markCardDrawn(select.getValue());
                if (select.getValue().getFirstQuestion() != null) {
                    drawFirstBondSupportCardsButton.setEnabled(true);
                    firstBondAnswersRowLayout.setVisible(true);
                    firstBondAnswersRow.get(0).setValue(select.getValue().getFirstQuestion());
                } else {
                    drawFirstBondSupportCardsButton.setEnabled(false);
                    firstBondAnswersRowLayout.setVisible(false);
                    firstBondAnswersRow.get(0).setValue("");
                }
            } else {
                drawFirstBondSupportCardsButton.setEnabled(false);
                firstBondAnswersRowLayout.setVisible(false);
            }
        });

        Button drawFirstBondCardButton = new Button("Draw card");
        drawFirstBondCardButton.setMaxWidth("200px");
        drawFirstBondCardButton.addClickListener(event -> {
            if (selectFirstBond.getValue() == null) {
                SpecificationCard card = specificationCardService.drawCardForCategory(CardCategory.BOND);
                selectFirstBond.setValue(card);
            } else {
                selectFirstBond.setValue(specificationCardService.redrawCard(selectFirstBond.getValue()));
            }
        });

        //----- Second bond -----
        Button drawSecondBondSupportCardsButton = new Button("Draw support cards");
        drawSecondBondSupportCardsButton.setEnabled(false);
        drawSecondBondSupportCardsButton.addClickListener(event -> {
            Map<String, List<BaseCard>> answersMap = characterRequestService.answerCardQuestions(selectSecondBond.getValue());
            binder.getBean().setSecondBondAnswers(answersMap);
            List<BaseCard> answers = answersMap.get("first");
            for (int i=0; i<answers.size(); i++) {
                secondBondAnswersRow.get(i+1).setValue(answers.get(i).getTitle());
            }
        });

        selectSecondBond.setItemLabelGenerator(GenerateCharacterView::specificationCardView);
        selectSecondBond.addValueChangeListener(select -> {
            binder.getBean().setSecondBond(select.getValue());
            if (select.getOldValue() != null) {
                specificationCardService.returnCardToDeck(select.getOldValue());
            }
            binder.getBean().setSecondBondAnswers(new HashMap<>());
            clearSecondBondAnswers();
            if (select.getValue() != null) {
                specificationCardService.markCardDrawn(select.getValue());
                if (select.getValue().getFirstQuestion() != null) {
                    drawSecondBondSupportCardsButton.setEnabled(true);
                    secondBondAnswersRowLayout.setVisible(true);
                    secondBondAnswersRow.get(0).setValue(select.getValue().getFirstQuestion());
                } else {
                    drawSecondBondSupportCardsButton.setEnabled(false);
                    secondBondAnswersRowLayout.setVisible(false);
                    secondBondAnswersRow.get(0).setValue("");
                }
            } else {
                drawSecondBondSupportCardsButton.setEnabled(false);
                secondBondAnswersRowLayout.setVisible(false);
            }
        });

        Button drawSecondBondCardButton = new Button("Draw card");
        drawSecondBondCardButton.setMaxWidth("200px");
        drawSecondBondCardButton.addClickListener(event -> {
            if (selectSecondBond.getValue() == null) {
                SpecificationCard card = specificationCardService.drawCardForCategory(CardCategory.BOND);
                selectSecondBond.setValue(card);
            } else {
                selectSecondBond.setValue(specificationCardService.redrawCard(selectSecondBond.getValue()));
            }
        });

        //----- Mark of the past -----
        Button drawPastSupportCardsButton = new Button("Draw support cards");
        drawPastSupportCardsButton.setEnabled(false);
        drawPastSupportCardsButton.addClickListener(event -> {
            Map<String, List<BaseCard>> answersMap = characterRequestService.answerCardQuestions(selectMarkOfThePast.getValue());
            binder.getBean().setMarkOfThePastAnswers(answersMap);
            List<BaseCard> firstAnswers = answersMap.get("first");
            for (int i=0; i<firstAnswers.size(); i++) {
                markOfThePastAnswersFirstRow.get(i+1).setValue(firstAnswers.get(i).getTitle());
            }
            List<BaseCard> secondAnswers = answersMap.get("second");
            if (secondAnswers != null) {
                for (int i = 0; i < secondAnswers.size(); i++) {
                    markOfThePastAnswersSecondRow.get(i + 1).setValue(secondAnswers.get(i).getTitle());
                }
            }
        });

        selectMarkOfThePast.setItemLabelGenerator(GenerateCharacterView::specificationCardView);
        selectMarkOfThePast.addValueChangeListener(select -> {
            binder.getBean().setMarkOfThePast(select.getValue());
            if (select.getOldValue() != null) {
                specificationCardService.returnCardToDeck(select.getOldValue());
            }
            binder.getBean().setMarkOfThePastAnswers(new HashMap<>());
            clearMarkOfThePastAnswers();
            if (select.getValue() != null) {
                specificationCardService.markCardDrawn(select.getValue());
                if (select.getValue().getFirstQuestion() != null) {
                    drawPastSupportCardsButton.setEnabled(true);
                    markOfThePastAnswersFirstRowLayout.setVisible(true);
                    markOfThePastAnswersFirstRow.get(0).setValue(select.getValue().getFirstQuestion());
                } else {
                    drawPastSupportCardsButton.setEnabled(false);
                    markOfThePastAnswersFirstRowLayout.setVisible(false);
                    markOfThePastAnswersFirstRow.get(0).setValue("");
                }
                if (select.getValue().getSecondQuestion() != null) {
                    markOfThePastAnswersSecondRowLayout.setVisible(true);
                    markOfThePastAnswersSecondRow.get(0).setValue(select.getValue().getSecondQuestion());
                } else {
                    markOfThePastAnswersSecondRowLayout.setVisible(false);
                    markOfThePastAnswersSecondRow.get(0).setValue("");
                }
            } else {
                drawPastSupportCardsButton.setEnabled(false);
                markOfThePastAnswersFirstRowLayout.setVisible(false);
                markOfThePastAnswersSecondRowLayout.setVisible(false);
            }
        });

        Button drawPastCardButton = new Button("Draw card");
        drawPastCardButton.setMaxWidth("200px");
        drawPastCardButton.addClickListener(event -> {
            if (selectMarkOfThePast.getValue() == null) {
                SpecificationCard card = specificationCardService.drawCardForCategory(CardCategory.EVENT);
                selectMarkOfThePast.setValue(card);
            } else {
                selectMarkOfThePast.setValue(specificationCardService.redrawCard(selectMarkOfThePast.getValue()));
            }
        });


        //----- Impetus -----
        Button drawImpetusSupportCardsButton = new Button("Draw support cards");
        drawImpetusSupportCardsButton.setEnabled(false);
        drawImpetusSupportCardsButton.addClickListener(event -> {
            Map<String, List<BaseCard>> answersMap = characterRequestService.answerCardQuestions(selectImpetus.getValue());
            binder.getBean().setImpetusAnswers(answersMap);
            List<BaseCard> firstAnswers = answersMap.get("first");
            for (int i=0; i<firstAnswers.size(); i++) {
                impetusAnswersFirstRow.get(i+1).setValue(firstAnswers.get(i).getTitle());
            }
            List<BaseCard> secondAnswers = answersMap.get("second");
            if (secondAnswers != null) {
                for (int i = 0; i < secondAnswers.size(); i++) {
                    impetusAnswersSecondRow.get(i + 1).setValue(secondAnswers.get(i).getTitle());
                }
            }
        });

        selectImpetus.setItemLabelGenerator(GenerateCharacterView::specificationCardView);
        selectImpetus.addValueChangeListener(select -> {
            binder.getBean().setImpetus(select.getValue());
            if (select.getOldValue() != null) {
                specificationCardService.returnCardToDeck(select.getOldValue());
            }
            binder.getBean().setImpetusAnswers(new HashMap<>());
            clearImpetusAnswers();
            if (select.getValue() != null) {
                specificationCardService.markCardDrawn(select.getValue());
                if (select.getValue().getFirstQuestion() != null) {
                    drawImpetusSupportCardsButton.setEnabled(true);
                    impetusAnswersFirstRowLayout.setVisible(true);
                    impetusAnswersFirstRow.get(0).setValue(select.getValue().getFirstQuestion());
                } else {
                    drawImpetusSupportCardsButton.setEnabled(false);
                    impetusAnswersFirstRowLayout.setVisible(false);
                    impetusAnswersFirstRow.get(0).setValue("");
                }
                if (select.getValue().getSecondQuestion() != null) {
                    impetusAnswersSecondRowLayout.setVisible(true);
                    impetusAnswersSecondRow.get(0).setValue(select.getValue().getSecondQuestion());
                } else {
                    impetusAnswersSecondRowLayout.setVisible(false);
                    impetusAnswersSecondRow.get(0).setValue("");
                }
            } else {
                drawImpetusSupportCardsButton.setEnabled(false);
                impetusAnswersFirstRowLayout.setVisible(false);
                impetusAnswersSecondRowLayout.setVisible(false);
            }
        });

        Button drawImpetusCardButton = new Button("Draw card");
        drawImpetusCardButton.setMaxWidth("200px");
        drawImpetusCardButton.addClickListener(event -> {
            if (selectImpetus.getValue() == null) {
                SpecificationCard card = specificationCardService.drawCardForCategory(CardCategory.EVENT);
                selectImpetus.setValue(card);
            } else {
                selectImpetus.setValue(specificationCardService.redrawCard(selectImpetus.getValue()));
            }
        });

        //----- Resulting text area -----
        resultingRequest.setLabel("Resulting configuration");
        resultingRequest.setMinHeight("300px");
        resultingRequest.setMinWidth("800px");
        resultingRequest.setReadOnly(true);

        //----- Form layout -----
        formLayout.addFormItem(selectCharacter, "Character");
        formLayout.add(drawCharacterCardButton);
        formLayout.addFormItem(selectOrigins, "Origins");
        formLayout.add(drawOriginsCardButton);
        formLayout.addFormItem(selectBackground, "Background");
        formLayout.add(drawBackroundCardButton);

        formLayout.addFormItem(selectFirstBond, "First bond");
        HorizontalLayout firstBondButtons = new HorizontalLayout();
        firstBondButtons.add(drawFirstBondCardButton);
        firstBondButtons.add(drawFirstBondSupportCardsButton);
        formLayout.add(firstBondButtons);
        formLayout.add(firstBondAnswersRowLayout);
        formLayout.setColspan(firstBondAnswersRowLayout,2);

        formLayout.addFormItem(selectSecondBond, "Second bond");
        HorizontalLayout secondBondButtons = new HorizontalLayout();
        secondBondButtons.add(drawSecondBondCardButton);
        secondBondButtons.add(drawSecondBondSupportCardsButton);
        formLayout.add(secondBondButtons);
        formLayout.add(secondBondAnswersRowLayout);
        formLayout.setColspan(secondBondAnswersRowLayout,2);

        formLayout.addFormItem(selectMarkOfThePast, "Mark of the past");
        HorizontalLayout markOfThePastBondButtons = new HorizontalLayout();
        markOfThePastBondButtons.add(drawPastCardButton);
        markOfThePastBondButtons.add(drawPastSupportCardsButton);
        formLayout.add(markOfThePastBondButtons);
        formLayout.add(markOfThePastAnswersFirstRowLayout);
        formLayout.setColspan(markOfThePastAnswersFirstRowLayout,2);
        formLayout.add(markOfThePastAnswersSecondRowLayout);
        formLayout.setColspan(markOfThePastAnswersSecondRowLayout,2);

        formLayout.addFormItem(selectImpetus, "Impetus");
        HorizontalLayout impetusButtons = new HorizontalLayout();
        impetusButtons.add(drawImpetusCardButton);
        impetusButtons.add(drawImpetusSupportCardsButton);
        formLayout.add(impetusButtons);
        formLayout.add(impetusAnswersFirstRowLayout);
        formLayout.setColspan(impetusAnswersFirstRowLayout,2);
        formLayout.add(impetusAnswersSecondRowLayout);
        formLayout.setColspan(impetusAnswersSecondRowLayout,2);

        formLayout.add(resultingRequest);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        generate.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(generate);
        buttonLayout.add(reset);

        reset.addClickListener(e -> {
            characterRequestService.returnUsedCardsToDeck(binder.getBean());
            clearForm();
        });
        generate.addClickListener(e -> {
            String generatedResult = characterRequestService.generateResultingRequestLayout(binder.getBean());
            resultingRequest.setValue(generatedResult);
        });

        return buttonLayout;
    }

    @NotNull
    private Component createAnwersRowComponent(List<TextField> answersRow) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        for (int i=0; i<6; i++) {
            TextField field = new TextField();
            field.setReadOnly(true);
            field.addThemeVariants(TextFieldVariant.LUMO_SMALL);
            answersRow.add(field);
            horizontalLayout.add(field);
        }
        return horizontalLayout;
    }

    private Component createFirstBondAnswersRow() {
        return createAnwersRowComponent(firstBondAnswersRow);
    }

    private Component createSecondBondAnswersRow() {
        return createAnwersRowComponent(secondBondAnswersRow);
    }

    private Component createMarkOfThePastAnswersFirstRow() {
        return createAnwersRowComponent(markOfThePastAnswersFirstRow);
    }

    private Component createMarkOfThePastAnswersSecondRow() {
        return createAnwersRowComponent(markOfThePastAnswersSecondRow);
    }

    private Component createImpetusAnswersFirstRow() {
        return createAnwersRowComponent(impetusAnswersFirstRow);
    }

    private Component createImpetusAnswersSecondRow() {
        return createAnwersRowComponent(impetusAnswersSecondRow);
    }

    private void clearFirstBondAnswers() {
        for (TextField field : firstBondAnswersRow) {
            field.setValue("");
        }
    }

    private void clearSecondBondAnswers() {
        for (TextField field : secondBondAnswersRow) {
            field.setValue("");
        }
    }

    private void clearMarkOfThePastAnswers() {
        for (TextField field : markOfThePastAnswersFirstRow) {
            field.setValue("");
        }
        for (TextField field : markOfThePastAnswersSecondRow) {
            field.setValue("");
        }
    }

    private void clearImpetusAnswers() {
        for (TextField field : impetusAnswersFirstRow) {
            field.setValue("");
        }
        for (TextField field : impetusAnswersSecondRow) {
            field.setValue("");
        }
    }

    private void updateLists() {
        List<BaseCard> characters = baseCardService.getCardsForCategory(CardCategory.PERSONALITY);
        selectCharacter.setItems(characters);
        List<BaseCard> origins = baseCardService.getCardsForCategory(CardCategory.PLACE);
        selectOrigins.setItems(origins);
        List<BaseCard> backgrounds = baseCardService.getCardsForCategory(CardCategory.BACKGROUND);
        selectBackground.setItems(backgrounds);
        List<SpecificationCard> bonds = specificationCardService.getCardsForCategory(CardCategory.BOND);
        selectFirstBond.setItems(bonds);
        selectSecondBond.setItems(bonds);
        List<SpecificationCard> events = specificationCardService.getCardsForCategory(CardCategory.EVENT);
        selectMarkOfThePast.setItems(events);
        selectImpetus.setItems(events);
    }

    private static String baseCardView(BaseCard baseCard) {
        if (baseCard == null) {
            return "";
        } else {
            return baseCard.getTitle();
        }
    }

    private static String specificationCardView(SpecificationCard card) {
        if (card == null) {
            return "";
        } else {
            return card.getTitle();
        }
    }

}
