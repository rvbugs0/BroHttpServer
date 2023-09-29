import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.net.URLDecoder;

class FileSystemUtility {

  private FileSystemUtility() {}

  public static boolean fileExists(String path) {
    File f = new File(path);
    return f.exists();
  }

  public static boolean directoryExists(String path) {
    File f = new File(path);
    return f.exists() && f.isDirectory();
  }

  public static String getFileExtension(String path) {
    System.out.println("path is: " + path);
    int lastDotIndex = path.lastIndexOf(".");
    if (lastDotIndex != -1) {
      String extension = path.substring(lastDotIndex + 1, path.length());

      return extension.toLowerCase();
    }
    return null;
  }
}

class BroUtilities {

  private BroUtilities() {}

  public static String decode(String encodedString){
    String decodedValue;
        try{
          decodedValue = URLDecoder.decode(encodedString,"UTF-8");

        }
        catch(Exception e){
          decodedValue = null;

        }
        return decodedValue;
  }

  public static Map<String, String> loadMIMETypes() {
    Map<String, String> mimeTypes = new HashMap<String, String>();
    File file;
    BufferedReader reader;
    try {
      file = new File("bro-data/mime.types.txt");
      if (file.exists()) {
        reader = new BufferedReader(new FileReader("bro-data/mime.types.txt"));
        String line = null;
        while ((line = reader.readLine()) != null) {
          if (line.startsWith("#")) {
            continue;
          }

          String splits[] = {
            line.substring(0, line.indexOf("\t")),
            line.substring(line.indexOf("\t"), line.length()),
          };

          String types[] = splits[1].trim().split(" ");

          for (int i = 0; i < types.length; i++) {
            mimeTypes.put(types[i].trim(), splits[0].trim());
            // System.out.println(mimeTypes.get(types[i].trim()));
          }
        }
        reader.close();
      } else {
        System.out.println("loadMIMETypes() -- File does not exist");
        // throw new Exception("loadMIMETypes() -- File does not exist");
      }
    } catch (Exception e) {
      System.out.println("loadMIMETypes() -- " + e);
    }
    return mimeTypes;
  }
}

class HttpResponseUtility {

  public static void sendResponse(Socket clientSocket, Response response)
    throws Exception {
    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
    String htmlMessage = response.getContent();

    String resp = "HTTP/1.1 200 OK\r\n";
    resp += "Connection: close\r\n";
    resp += "Content-Type: text/html\r\nContent-Length: ";
    resp += htmlMessage.length();
    resp += "\r\n\r\n";
    resp += htmlMessage;
    writer.println(resp);
    writer.close();
  }
}

class HttpUtility {

  private HttpUtility() {}

  public static String[] HttpMethods = {
    "GET",
    "PUT",
    "POST",
    "DELETE",
    "TRACE",
    "OPTION",
    "HEAD",
    "CONNECT",
  };
}

class StringUtility {

  private StringUtility() {}

  public static int indexInArray(String[] array, String item) {
    int index = -1;

    for (int i = 0; i < array.length; i++) {
      if (item.compareTo(array[i]) == 0) {
        index = i;
        break;
      }
    }
    return index;
  }
}

class HttpErrorStatusUtility {

  private HttpErrorStatusUtility() {}

  public static void sendBadRequestError(Socket clientSocket) {}

  public static void sendHttpVersionNotSupportedError(
    Socket clientSocket,
    String clientHttpVersion
  ) {}

  public static void sendNotFoundError(Socket clientSocket, String clientURL)
    throws Exception {
    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
    String htmlMessage =
      "<html><head><title>BroServer</title></head><body><h1>404: Resource not found</h1><h4>" +
      clientURL +
      "</h4></body></html>";

    // String respHTML = "<html><head><title>BroServer</title></head><body><h1>Hello from BroServer</h1></body></html>";
    String resp = "HTTP/1.1 200 OK\r\n";
    resp += "Connection: close\r\n";
    resp += "Content-Type: text/html\r\nContent-Length: ";
    resp += htmlMessage.length();
    resp += "\r\n\r\n";
    resp += htmlMessage;

    writer.println(resp);

    writer.close();
  }

  public static void sendMethodNotAllowedError(
    Socket clientSocket,
    String methodName
  ) {}
}

class URLMapping {

  private static Map<String, __request_method__> httpMethodNameEnumMap = new HashMap<>();

  static {
    httpMethodNameEnumMap.put("GET", __request_method__.GET);
    httpMethodNameEnumMap.put("POST", __request_method__.POST);
    httpMethodNameEnumMap.put("PUT", __request_method__.PUT);
    httpMethodNameEnumMap.put("DELETE", __request_method__.DELETE);
    httpMethodNameEnumMap.put("OPTION", __request_method__.OPTION);
    httpMethodNameEnumMap.put("TRACE", __request_method__.TRACE);
    httpMethodNameEnumMap.put("HEAD", __request_method__.HEAD);
    httpMethodNameEnumMap.put("CONNECT", __request_method__.CONNECT);
  }

  public static __request_method__ getMappedMethodEnum(String methodName) {
    return httpMethodNameEnumMap.get(methodName.toUpperCase());
  }

  public static enum __request_method__ {
    GET,
    POST,
    PUT,
    DELETE,
    OPTION,
    CONNECT,
    TRACE,
    HEAD,
  }

  public URLMapping(
    __request_method__ requestMethod,
    BiConsumer<Request, Response> mappedFunction
  ) {
    this.requestMethod = requestMethod;
    this.mappedFunction = mappedFunction;
  }

  private __request_method__ requestMethod;
  private BiConsumer<Request, Response> mappedFunction;

  public __request_method__ getRequestMethod() {
    return this.requestMethod;
  }

  public BiConsumer<Request, Response> getMappedFunction() {
    return this.mappedFunction;
  }
}

class Validator {

  private Validator() {}

  public static boolean isValidMIMEType(String mimeType) {
    return true;
  }

  public static boolean isValidPath(String path) {
    return FileSystemUtility.directoryExists(path);
  }

  public static boolean isValidURLFormat(String url) {
    return true;
  }
}

class Error {

  private String error;

  public Error(String error) {
    this.error = error;
  }

  public boolean hasError() {
    return this.error.length() > 0;
  }

  public String getError() {
    return "";
  }

  public String toString() {
    return this.getError();
  }
}

class Request {

  private String methodName;
  private String host;
  private String httpVersion;
  private Map<String, String> requestDataMap = new HashMap<String, String>();

  public Request(
    String methodName,
    String host,
    String httpVersion,
    String queryString
  ) {
    createRequestDataMap(queryString);
  }

  private void createRequestDataMap(String queryString) {
    if (queryString == null) {
      return;
    }
    int equalIndex = -1;
    int ampIndex = -1;
    while ((equalIndex = queryString.indexOf("=")) != -1) {
      String key = queryString.substring(0, equalIndex);
      String value = null;
      ampIndex = queryString.indexOf("&", equalIndex);
      if (ampIndex != -1) {
        //has an &
        value = BroUtilities.decode(queryString.substring(equalIndex + 1, ampIndex));
        requestDataMap.put(key, value);
        queryString = queryString.substring(ampIndex+1, queryString.length());
        
        System.out.println("Map Entry: Key="+key+" value="+value);


      } else {
        
        value = BroUtilities.decode(queryString.substring(equalIndex + 1, queryString.length()));
        requestDataMap.put(key,value);
        
        System.out.println("Map Entry: Key="+key+" value="+value);


        break;
      }
    }
  }
}

class Response {

  private StringBuilder content = new StringBuilder();
  private String contentType;

  public void setContentType(String contentType) {
    if (Validator.isValidMIMEType(contentType)) {
      this.contentType = contentType;
    } else {}
  }

  public String getContent() {
    return content.toString();
  }

  public void append(String text) {
    if (text != null) {
      content.append(text);
    }
  }
}

class Bro {

  private String staticResourcesFolder;
  Map<String, String> mimeTypes;
  Map<String, URLMapping> urlMappings = new HashMap<>();

  public Bro() throws Exception {
    mimeTypes = BroUtilities.loadMIMETypes();
    if (mimeTypes.size() == 0) {
      throw new Exception("bro-data/mime.types has been tampered with.");
    }
  }

  public void setStaticResourcesFolder(String folderName) throws Exception {
    if (Validator.isValidPath(folderName)) {
      this.staticResourcesFolder = folderName;
    } else {
      throw new Exception(
        "setStaticResourcesFolder(): --- Invalid static resources path: " +
        folderName
      );
    }
  }

  public void get(String url, BiConsumer<Request, Response> operation) {
    // operation.accept()
    if (Validator.isValidURLFormat(url)) {
      urlMappings.put(
        url,
        new URLMapping(URLMapping.__request_method__.GET, operation)
      );
    }
  }

  private boolean serveStaticResource(Socket clientSocket, String url)
    throws Exception {
    if (this.staticResourcesFolder.length() == 0) {
      System.out.println("returning false - 1");
      return false;
    }
    if (!FileSystemUtility.directoryExists(this.staticResourcesFolder)) {
      System.out.println("returning false - 2");
      return false;
    }
    String resourcePath = this.staticResourcesFolder + url;
    System.out.println("Static Resource path is: " + resourcePath);
    if (!FileSystemUtility.fileExists(resourcePath)) {
      System.out.println("returning false - 3");
      return false;
    }
    File file = new File(resourcePath);
    if (file.length() == 0) {
      System.out.println("returning false - 4");
      return false;
    }
    // System.out.println("File size is: " + file.length());
    String extension = FileSystemUtility.getFileExtension(url);
    String mimeType;
    if (extension == null) {
      mimeType = "text/html";
    } else {
      mimeType = mimeTypes.getOrDefault(extension, "text/html");
    }
    String header =
      "HTTP/1.1 200 OK\r\nContent-Type:" +
      mimeType +
      "\r\nConnection:close\r\nContent-Length:" +
      file.length() +
      "\r\n\r\n";
    OutputStream outputStream = clientSocket.getOutputStream();
    outputStream.write(header.getBytes());
    int count = 0;
    byte[] buffer = new byte[4096];
    FileInputStream inputStream = new FileInputStream(file);

    while ((count = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, count);
    }
    inputStream.close();
    outputStream.close();
    return true;
  }

  public void listen(int portnumber, Consumer<Error> operation) {
    try {
      ServerSocket serverSocket = new ServerSocket(portnumber);
      Error error = new Error("");
      operation.accept(error);

      while (true) {
        try {
          Socket clientSocket = serverSocket.accept();
          System.out.println(
            "Connected client: " +
            clientSocket.getInetAddress().getHostAddress()
          );

          ArrayList<String> requestBuffer = new ArrayList<>();
          BufferedReader reader = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream())
          );
          String line = null;

          while ((line = reader.readLine()) != null && !line.isEmpty()) {
            System.out.println(line);
            requestBuffer.add(line);
          }
          // reader.close();

          if (requestBuffer.size() == 0) {
            //bad request
            HttpErrorStatusUtility.sendBadRequestError(clientSocket);
            System.out.println("Client socket closed -1");
            clientSocket.close();
            continue;
          }

          String firstLine = requestBuffer.get(0);
          //format of first line - [GET /url HTTP/1.1] - if not this format then malformed
          String[] splits = firstLine.split(" ");
          if (splits.length != 3) {
            //bad request
            HttpErrorStatusUtility.sendBadRequestError(clientSocket);
            System.out.println("Client socket closed -2");
            clientSocket.close();
            continue;
          }

          String methodName = splits[0].toUpperCase();

          int index = StringUtility.indexInArray(
            HttpUtility.HttpMethods,
            methodName
          );
          if (index == -1) {
            //            bad request
            HttpErrorStatusUtility.sendBadRequestError(clientSocket);
            System.out.println("Client socket closed -3");
            clientSocket.close();
            continue;
          }

          //parsing the end of first line
          String lineEnd = splits[2];
          // if (
          //   lineEnd.length() < 8 ) {
          //   // 9 - for "HTTP/1.1\n" - atleast 9 or "HTTP/1.1\r\n"
          //   //bad request
          //   HttpErrorStatusUtility.sendBadRequestError(clientSocket);
          //   System.out.println(lineEnd+":");
          //   clientSocket.close();
          //   continue;
          // }

          String protocol = lineEnd;
          if (lineEnd.charAt(lineEnd.length() - 2) == '\r') {
            protocol = protocol.substring(0, protocol.length() - 2);
          }
          if (protocol.toUpperCase().compareTo("HTTP/1.1") != 0) {
            //protocol not supported
            HttpErrorStatusUtility.sendHttpVersionNotSupportedError(
              clientSocket,
              protocol
            );
            System.out.println("Client socket closed -4");
            clientSocket.close();
            continue;
          }

          String url = splits[1];
          String queryString = null;
          int questionMarkIndex = url.indexOf("?");
          if (questionMarkIndex != -1) {
            queryString = url.substring(questionMarkIndex + 1, url.length());
            url = url.substring(0, questionMarkIndex);
          }

          if (!urlMappings.containsKey(url)) {
            //404
            if (!serveStaticResource(clientSocket, url)) {
              HttpErrorStatusUtility.sendNotFoundError(clientSocket, url);
            }

            System.out.println("Client socket closed -5");
            clientSocket.close();
            continue;
          }

          URLMapping urlMapping = urlMappings.get(url);
          if (
            URLMapping.getMappedMethodEnum(methodName) !=
            urlMapping.getRequestMethod()
          ) {
            //method not supported error
            HttpErrorStatusUtility.sendMethodNotAllowedError(
              clientSocket,
              methodName
            );
            System.out.println("Client socket closed -6");
            clientSocket.close();
            continue;
          }

          Request request = new Request(methodName, url, protocol, queryString);
          Response response = new Response();
          urlMapping.getMappedFunction().accept(request, response);

          HttpResponseUtility.sendResponse(clientSocket, response);
          // clientSocket.close();
        } catch (Exception e) {
          error = new Error(e.getMessage());
          operation.accept(error);
        }
      }
    } catch (Exception exception) {
      System.out.println(exception);
      Error error = new Error(exception.getMessage());
      operation.accept(error);
    }
  }
}

class WebDev {

  public static void main(String gg[]) {
    try {
      Bro bro = new Bro();

      try {
        bro.setStaticResourcesFolder("whatever");
      } catch (Exception e) {
        System.out.println(e);
        System.exit(0);
      }

      bro.get(
        "/",
        (Request request, Response response) -> {
          String html =
            """
            <html lang="en">
            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>BroServer</title>
            </head>
            <body>
                <h1>Welcome</h1>
                <h3>Hello from bro server</h3>
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

      bro.get(
        "/save_test1_data",
        (Request request, Response response) -> {
          String html =
            """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>Test Result - GET with query String</title>
            </head>
            <body>
                <h1>Data Saved</h1>
                
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
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
