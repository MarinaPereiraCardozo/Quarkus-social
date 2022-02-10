package my.groupId.quarkussocial;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(
    info = @Info(
            title="API Quarkus Social",
            version = "1.0",
            contact = @Contact(
                    name = "Marina Cardozo",
                    url = "https://github.com/MarinaPereiraCardozo",
                    email = "marina.cardozo@best2bee.com.br"),
            license = @License(
                    name = "Apache 2.0",
                    url = "https://www.apache.org/licenses/LICENSE-2.0.html")
    )
)

public class QuakusSocialApplication extends Application {

}
