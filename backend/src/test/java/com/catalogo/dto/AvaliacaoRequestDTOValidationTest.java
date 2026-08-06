package com.catalogo.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AvaliacaoRequestDTOValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    void deveRejeitarNotaAbaixoDoMinimo() {
        var dto = new AvaliacaoRequestDTO(new BigDecimal("-0.1"), "comentário", "avaliador");
        Set<ConstraintViolation<AvaliacaoRequestDTO>> violacoes = validator.validate(dto);
        assertFalse(violacoes.isEmpty());
    }

    @Test
    void deveRejeitarNotaAcimaDoMaximo() {
        var dto = new AvaliacaoRequestDTO(new BigDecimal("10.1"), "comentário", "avaliador");
        Set<ConstraintViolation<AvaliacaoRequestDTO>> violacoes = validator.validate(dto);
        assertFalse(violacoes.isEmpty());
    }

    @Test
    void deveAceitarNotasNosLimitesDoIntervalo() {
        var minima = new AvaliacaoRequestDTO(BigDecimal.ZERO, null, "avaliador");
        var maxima = new AvaliacaoRequestDTO(BigDecimal.TEN, null, "avaliador");

        assertTrue(validator.validate(minima).isEmpty());
        assertTrue(validator.validate(maxima).isEmpty());
    }

    @Test
    void deveRejeitarNomeDoAvaliadorEmBranco() {
        var dto = new AvaliacaoRequestDTO(new BigDecimal("8.5"), null, " ");
        Set<ConstraintViolation<AvaliacaoRequestDTO>> violacoes = validator.validate(dto);
        assertFalse(violacoes.isEmpty());
    }
}
