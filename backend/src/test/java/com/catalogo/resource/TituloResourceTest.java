package com.catalogo.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class TituloResourceTest {

    @Test
    void deveExecutarFluxoCompletoDeTituloEAvaliacao() {
        long generoId = given()
                .contentType(ContentType.JSON)
                .body("""
                        {"nome": "Teste Suspense %s"}
                        """.formatted(System.nanoTime()))
                .when().post("/api/generos")
                .then().statusCode(201)
                .extract().jsonPath().getLong("id");

        String tituloPayload = """
                {
                  "titulo": "Filme de Teste",
                  "tituloOriginal": "Test Movie",
                  "tipo": "FILME",
                  "sinopse": "Sinopse de teste",
                  "anoLancamento": 2024,
                  "duracaoMinutos": 120,
                  "generoIds": [%d],
                  "elenco": []
                }
                """.formatted(generoId);

        long tituloId = given()
                .contentType(ContentType.JSON)
                .body(tituloPayload)
                .when().post("/api/titulos")
                .then().statusCode(201)
                .body("titulo", equalTo("Filme de Teste"))
                .body("notaMedia", equalTo(null))
                .extract().jsonPath().getLong("id");

        given()
                .when().get("/api/titulos/" + tituloId)
                .then().statusCode(200)
                .body("id", equalTo((int) tituloId))
                .body("generos.size()", greaterThanOrEqualTo(1));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"nota": 8.5, "comentario": "Muito bom", "nomeAvaliador": "Teste"}
                        """)
                .when().post("/api/titulos/" + tituloId + "/avaliacoes")
                .then().statusCode(201);

        given()
                .when().get("/api/titulos/" + tituloId)
                .then().statusCode(200)
                .body("notaMedia", notNullValue())
                .body("totalAvaliacoes", equalTo(1));

        given()
                .when().delete("/api/titulos/" + tituloId)
                .then().statusCode(204);

        given()
                .when().get("/api/titulos/" + tituloId)
                .then().statusCode(404);
    }

    @Test
    void deveRejeitarTituloSemGenero() {
        String payloadSemGenero = """
                {
                  "titulo": "Sem Gênero",
                  "tipo": "FILME",
                  "anoLancamento": 2024,
                  "generoIds": []
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(payloadSemGenero)
                .when().post("/api/titulos")
                .then().statusCode(400);
    }
}
