import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class Validator {

  private Validator() {}

  public static boolean isValidMIMEType(String mimeType) {
    return true;
  }

  public static boolean isValidPath(String path) {
    return true;
  }

  public static boolean isValidURLFormat(String url) {
    return true;
  }
}

class Error {

  public boolean hasError() {
    return false;
  }

  public String getError() {
    return "";
  }
}

class Request {}

class Response {

  private StringBuilder content= new StringBuilder();
  private String contentType;

  public void setContentType(String contentType) {
    if (Validator.isValidMIMEType(contentType)) {
      this.contentType = contentType;
    } else {}
  }

  public void append(String content) {}
}

class Bro {

  private String staticResourcesFolder;

  Map<String, BiConsumer<Request, Response>> urlMappings = new HashMap<>();

  public Bro() {}

  public void setStaticResourcesFolder(String folderName) {
    if (Validator.isValidPath(folderName)) {
      this.staticResourcesFolder = folderName;
    } else {}
  }

  public void get(String url, BiConsumer<Request, Response> operation) {
    // operation.accept()
    if (Validator.isValidURLFormat(url)) {
      urlMappings.put(url, operation);
    }
  }

  public void listen(int portnumber, Consumer<Error> operation) {}
}

class WebDev {

  public static void main(String gg[]) {
    Bro bro = new Bro();
    bro.setStaticResourcesFolder("whatever");
    bro.get(
      "/",
      (Request request, Response response) -> {
        String html =
          """
            <html lang="en">
            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>Whatever</title>
            </head>
            <body>
                <h1>Welcome</h1>
                <h3>Some text</h3>
                <a href="getCustomers">Customers List</a>
            </body>
            </html>
            """;
        response.setContentType("text/html");
        response.append(html);
      }
    );

    bro.get(
      "/getCustomers",
      (Request request, Response response) -> {
        String html =
          """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>Whatever</title>
            </head>
            <body>
                <h1>List of Customers</h1>
                <ul>
                <li>Ramesh</li>
                <li>Suresh</li>
                </ul>
            </body>
            </html>
        """;
        response.setContentType("text/html");
        response.append(html);
      }
    );

    bro.listen(
      6060,
      (Error error) -> {
        if (error.hasError()) {
          System.out.println("Error: " + error);
          return;
        }
        System.out.println(
          "Bro HTTP Server is ready to accept request on port 6060"
        );
      }
    );
  }
}
