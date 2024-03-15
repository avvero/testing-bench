package pw.avvero.spring.sandbox.bot.mock;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.core.model.*;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.runtime.IOGroovyMethods;
import org.codehaus.groovy.runtime.ResourceGroovyMethods;
import org.intellij.lang.annotations.Language;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
public class CustomMockRestResponseCreators extends MockRestResponseCreators {

    public static DefaultResponseCreator withSuccess(@Language("JSON") String body) {
        return MockRestResponseCreators.withSuccess(body, APPLICATION_JSON);
    }

    public static DefaultResponseCreator fromContract(String contractFileName) {
        File pactFile = new File("src/test/resources/contracts/" + contractFileName);
        RequestResponsePact pact = (RequestResponsePact) DefaultPactReader.INSTANCE.loadPact(pactFile);
        Assert.isTrue(pact.getInteractions().size() == 1, "There should be exactly one iteration per contract file");
        RequestResponseInteraction interaction = (RequestResponseInteraction) pact.getInteractions().get(0);
        Response response = interaction.getResponse();
        return MockRestResponseCreators.withRawStatus(response.getStatus())
                .contentType(MediaType.parseMediaType(Objects.requireNonNull(response.determineContentType().asString())))
                .body(response.getBody().valueAsString());
    }

    public static String fromFile(String testResourceFile) throws IOException {
        return IOGroovyMethods.getText(ResourceGroovyMethods.newReader(new File("src/test/resources/" + testResourceFile)));
    }
}