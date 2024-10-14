package io.quarkiverse.quinoa.it;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import io.quarkiverse.playwright.InjectPlaywright;
import io.quarkiverse.playwright.WithPlaywright;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestProfile(TestProfiles.NextTests.class)
@WithPlaywright
public class QuinoaUINextAppRouterTest {


    @InjectPlaywright
    BrowserContext context;

    @TestHTTPResource("/index.html")
    URL index;
    
    @TestHTTPResource("/about")
    URL about;

    @TestHTTPResource("/something")
    URL url404;

    @TestHTTPResource("/api/quinoa")
    URL api;

    @Test
    public void testUIIndex() {
        final Page page = context.newPage();
        Response response = page.navigate(index.toString());
        Assertions.assertEquals("OK", response.statusText());

        page.waitForLoadState();

        String title = page.title();
        Assertions.assertEquals("Create Next App", title);

        // Make sure the component loaded and hits the backend
        final ElementHandle quinoaEl = page.waitForSelector(".quinoa.loaded");
        String greeting = quinoaEl.innerText();
        Assertions.assertEquals("Hello Quinoa", greeting);
    }
    
    @Test
    public void testAboutPage() {
        final Page page = context.newPage();
        Response response = page.navigate(about.toString());
        Assertions.assertEquals("OK", response.statusText());

        page.waitForLoadState();

        String title = page.title();
        Assertions.assertEquals("About", title);
    }

    @Test
    public void test404Endpoint() {
        given()
                .when().get(url404)
                .then()
                .statusCode(404);
    }

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get(api)
                .then()
                .statusCode(200)
                .body(is("Hello Quinoa"));
    }

    @Test
    public void testHelloEndpointPost() {
        given()
                .body("bowl")
                .contentType(ContentType.TEXT)
                .when().post(api)
                .then()
                .statusCode(200)
                .body(is("Hello Quinoa bowl"));
    }
}
