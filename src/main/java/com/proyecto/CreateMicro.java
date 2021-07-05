package com.proyecto;

import Entities.FormValue;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CreateMicro implements Runnable {

    String nombre;
    int seguridad = 0;
    int microservicio = 0;
    String appProperties = "";
    String RutaCapertaMadre;

    public CreateMicro(String name, int seg, int serv, String rutaCapertaMadre) {
        nombre = name;
        seguridad = seg;
        microservicio = serv;
        RutaCapertaMadre = rutaCapertaMadre;
    }

    public void CrearClase(FormValue clase) {
        clase.getFilas();

    }

    @Override
    public void run() {

        // Crear Aplicacion con Nombre e Importar toda las libs necesaria con los cmd de Mvn
        //////////////////////////////////////////////////////////

        File dir = new File(RutaCapertaMadre + "/" + nombre);
        if (!dir.exists()) dir.mkdirs();

        String path = System.getProperty("user.dir");
        String userHome = System.getProperty("user.home");
        String comandos, archivo_comando;
        comandos =
                "mvn io.quarkus:quarkus-maven-plugin:1.8.2.Final:create -DprojectGroupId=org.proyecto " +
                        "-DprojectArtifactId=" + nombre +
                        " -DclassName=\"org.proyecto.Apiapp\" -Dpath=\"/hello\"\n";
//        comando2= "cd "+ nombre + "\n";
//        comando3 = "mvn quarkus:add-extension -Dextensions=\"agroal\"";
//        comando4 = "mvn quarkus:add-extension -Dextensions=\"quarkus-hibernate-orm-panache\" \n";
//        comando5="mvn quarkus:add-extension -Dextensions=\"jdbc-mysql\" \n";
//        comando6="mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-smallrye-openapi\"\n";
//        comando7="mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-spring-security\"\n";
//        comando8="mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-resteasy-jsonb\"\n";


        archivo_comando = "cd " + path + "\\" + nombre + "\n" +
                "\n" +
                "call mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-agroal\"\n" +
                "\n" +
                "call mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-hibernate-orm-panache\"\n" +
                "\n" +
                "call mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-jdbc-mysql\"\n" +
                "\n" +
                "call mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-smallrye-openapi\"\n" +
                "\n" +
                "call mvn quarkus:add-extension -Dextensions=\"io.quarkus:quarkus-resteasy-jsonb\"\n" +
                "\n" +
                "call mvn quarkus:add-extension -Dextensions=\"org.apache.camel.quarkus:camel-quarkus-consul\"\n" +
                "\n";

        if (seguridad != 0) {
            archivo_comando = archivo_comando + "call mvn quarkus:add-extension -Dextensions=\"oidc,keycloak-authorization\"\n";
        }
        //System.out.println(comandos);

        ProcessBuilder processBuilder = new ProcessBuilder();
        ProcessBuilder processBuilder2 = new ProcessBuilder();
        // -- Linux --

        // Run a shell command
        //   processBuilder.command("bash", "-c", comandos);

        // -- Windows --

        // Run a command
        processBuilder.command("cmd.exe", "/c", comandos);


//        File currentDir = new File(".");
        try {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            process.destroyForcibly();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ///////////////////////////////////////////////////////////////////////////////////////////
        try {
            File myObj = new File(path + "/comando_add.bat");
            if (myObj.createNewFile()) {
//                System.out.println("Archivo Creado: " + myObj.getName());
            } else {
//                System.out.println("Archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(path + "/comando_add.bat");
            myWriter.write(archivo_comando);
            myWriter.close();
            //  System.out.println("Archivo bat creado.");
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }
        processBuilder2.command("cmd.exe", "/c", "comando_add.bat");
        try {
            Process process2 = processBuilder2.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process2.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process2.waitFor();
            process2.destroyForcibly();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            File myObj = new File(path + "/" + nombre + "/src/main/resources/application.properties");
            if (myObj.createNewFile()) {
                //  System.out.println("Archivo Creado: " + myObj.getName());
            } else {
                //   System.out.println("Archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        try {

            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/resources/application.properties");
            String apppropert = "#Datasource Config\n" +
                    "quarkus.datasource.db-kind=mysql\n" +
                    "quarkus.datasource.driver=com.mysql.cj.jdbc.Driver\n" +
                    "quarkus.datasource.username=root\n" +
                    "quarkus.datasource.password=12345678\n" +
                    "quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/prueba\n" +
                    "quarkus.hibernate-orm.log.sql=true\n" +
                    "# drop and create the database at startup (use `update` to only update the schema)\n" +
                    "quarkus.hibernate-orm.database.generation=update\n" +
                    "quarkus.smallrye-openapi.path=/swagger\n" +
                    "quarkus.swagger-ui.always-include=true\n" +
                    "quarkus.swagger-ui.path=/explorer\n" +
                    "mp.openapi.extensions.smallrye.operationIdStrategy=METHOD\n\n" +
                    "quarkus.http.port=0\n" +
                    "quarkus.application.name=" + nombre + "\n" +
                    "quarkus.application.version=1.0\n" +
                    "quarkus.consul-config.enabled=true\n" +
                    "quarkus.consul-config.properties-value-keys=config/${quarkus.application.name}\n";
            System.out.println(seguridad);
            if (seguridad != 0) {
                apppropert = apppropert + "# Keycloak with 100 offset\n" +
                        "keycloak.url=http://localhost:8180\n" +
                        "\n" +
                        "quarkus.oidc.enabled=true\n" +
                        "quarkus.oidc.auth-server-url=${keycloak.url}/auth/realms/quarkus-realm\n" +
                        "quarkus.oidc.client-id=quarkus-client\n" +
                        "quarkus.oidc.credentials.secret=mysecret\n" +
                        "quarkus.http.cors=true\n" +
                        "quarkus.oidc.tls.verification=none\n" +
                        "grant_type=password\n" +
                        "urltoken_request=${keycloak.url}/auth/realms/quarkus-realm/protocol/openid-connect/token\n\n";

            }

            appProperties = apppropert;
            myWriter.write(apppropert);
            myWriter.close();
            //  System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        String dockerCompose = "version: \"3\"\n" +
                "services:\n" +
                "  consul:\n" +
                "    image: consul:latest\n" +
                "    ports:\n" +
                "    - 8500:8500\n" +
                "    environment:\n" +
                "      - CONSUL_BIND_INTERFACE=eth0";

        if (seguridad == 1) {
            dockerCompose = dockerCompose + "\n  keycloak:\n" +
                    "    image: quay.io/keycloak/keycloak:7.0.1\n" +
                    "    ports:\n" +
                    "    - 8180:8180\n" +
                    "    environment:\n" +
                    "      - KEYCLOAK_IMPORT=/tmp/quarkus-realm.json\n" +
                    "      - KEYCLOAK_USER=admin\n" +
                    "      - KEYCLOAK_PASSWORD=admin\n" +
                    "    command: [\"-Djboss.http.port=8180\", \"-Dkeycloak.profile.feature.upload_scripts=enabled\"]\n" +
                    "    volumes:\n" +
                    "    - ./quarkus-realm.json:/tmp/quarkus-realm.json";
        }

        try {
            File myObj = new File(path + "/" + nombre + "/docker-compose.yml");
            if (myObj.createNewFile()) {
                //   System.out.println("File created: " + myObj.getName());
            } else {
                //  System.out.println("Archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/docker-compose.yml");
            myWriter.write(dockerCompose);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        try {
            File myObj = new File(path + "/" + nombre + "/JF-LINP.txt");
            if (myObj.createNewFile()) {
                //   System.out.println("File created: " + myObj.getName());
            } else {
                //  System.out.println("Archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/JF-LINP.txt");
            myWriter.write("Txt Identifier, please do not delete if you still want to use the framework." +
                    "\n Created on : " + java.time.LocalTime.now()
            );
            myWriter.close();
            //  System.out.println("Modelo generado");
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        String clase_micro = "";

        File theDir = new File(path + "/" + nombre + "/src/main/java/org/proyecto/MicroServiceConsul/");
        if (!theDir.exists()) theDir.mkdirs();

        // carpeta cliente *****************************************

        theDir = new File(path + "/" + nombre + "/src/main/java/org/proyecto/MicroServiceConsul/Client");
        if (!theDir.exists()) theDir.mkdirs();

        clase_micro = "package org.proyecto.MicroServiceConsul.Client;\n" +
                "\n" +
                "import java.net.URI;\n" +
                "import java.util.List;\n" +
                "import java.util.concurrent.atomic.AtomicInteger;\n" +
                "\n" +
                "import javax.ws.rs.client.ClientRequestContext;\n" +
                "import javax.ws.rs.client.ClientRequestFilter;\n" +
                "import javax.ws.rs.core.UriBuilder;\n" +
                "\n" +
                "import com.orbitz.consul.Consul;\n" +
                "import com.orbitz.consul.HealthClient;\n" +
                "import com.orbitz.consul.model.health.ServiceHealth;\n" +
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n" +
                "\n" +
                "public class LoadBalancedFilter implements ClientRequestFilter {\n" +
                "\n" +
                "    private static final Logger LOGGER = LoggerFactory\n" +
                "            .getLogger(LoadBalancedFilter.class);\n" +
                "\n" +
                "    private Consul consulClient;\n" +
                "    private AtomicInteger counter = new AtomicInteger();\n" +
                "\n" +
                "    public LoadBalancedFilter(Consul consulClient) {\n" +
                "        this.consulClient = consulClient;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void filter(ClientRequestContext ctx) {\n" +
                "        URI uri = ctx.getUri();\n" +
                "        HealthClient healthClient = consulClient.healthClient();\n" +
                "        List<ServiceHealth> instances = healthClient\n" +
                "                .getHealthyServiceInstances(uri.getHost()).getResponse();\n" +
                "        instances.forEach(it ->\n" +
                "                LOGGER.info(\"Instance: uri={}:{}\",\n" +
                "                        it.getService().getAddress(),\n" +
                "                        it.getService().getPort()));\n" +
                "        ServiceHealth instance = instances.get(counter.getAndIncrement()%instances.size());\n" +
                "        URI u = UriBuilder.fromUri(uri)\n" +
                "                .host(instance.getService().getAddress())\n" +
                "                .port(instance.getService().getPort())\n" +
                "                .build();\n" +
                "        ctx.setUri(u);\n" +
                "    }\n" +
                "\n" +
                "}";

        try {
            File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/MicroServiceConsul/Client/LoadBalancedFilter.java");
            if (myObj.createNewFile()) {
                // System.out.println("File created: " + myObj.getName());
            } else {
                //  System.out.println("Archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/MicroServiceConsul/Client/LoadBalancedFilter.java");
            myWriter.write(clase_micro);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        // carpeta config ***********************************

        theDir = new File(path + "/" + nombre + "/src/main/java/org/proyecto/MicroServiceConsul/Config/");
        if (!theDir.exists()) theDir.mkdirs();

        clase_micro = "package org.proyecto.MicroServiceConsul.Config;\n" +
                "\n" +
                "\n" +
                "import javax.enterprise.context.ApplicationScoped;\n" +
                "import javax.enterprise.inject.Produces;\n" +
                "\n" +
                "import com.orbitz.consul.Consul;\n" +
                "import org.proyecto.MicroServiceConsul.Client.LoadBalancedFilter;\n" +
                "\n" +
                "@ApplicationScoped\n" +
                "public class " + nombre + "BeansProducer {\n" +
                "\n" +
                "\n" +
                "    @Produces\n" +
                "    Consul consulClient = Consul.builder().build();\n" +
                "\n" +
                "    @Produces\n" +
                "    LoadBalancedFilter filter = new LoadBalancedFilter(consulClient);\n" +
                "\n" +
                "}\n";

        try {
            File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/MicroServiceConsul/Config/" + nombre + "BeansProducer.java");
            if (myObj.createNewFile()) {
                // System.out.println("File created: " + myObj.getName());
            } else {
                //  System.out.println("Archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/MicroServiceConsul/Config/" + nombre + "BeansProducer.java");
            myWriter.write(clase_micro);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        // carpeta liceCycle ********************************
        theDir = new File(path + "/" + nombre + "/src/main/java/org/proyecto/MicroServiceConsul/LifeCycle/");
        if (!theDir.exists()) theDir.mkdirs();

        clase_micro = "package org.proyecto.MicroServiceConsul.LifeCycle;\n" +
                "\n" +
                "import java.util.List;\n" +
                "import java.util.concurrent.Executors;\n" +
                "import java.util.concurrent.ScheduledExecutorService;\n" +
                "import java.util.concurrent.TimeUnit;\n" +
                "\n" +
                "import javax.enterprise.context.ApplicationScoped;\n" +
                "import javax.enterprise.event.Observes;\n" +
                "import javax.inject.Inject;\n" +
                "\n" +
                "import com.orbitz.consul.Consul;\n" +
                "import com.orbitz.consul.HealthClient;\n" +
                "import com.orbitz.consul.model.agent.ImmutableRegistration;\n" +
                "import com.orbitz.consul.model.health.ServiceHealth;\n" +
                "import io.quarkus.runtime.ShutdownEvent;\n" +
                "import io.quarkus.runtime.StartupEvent;\n" +
                "import org.eclipse.microprofile.config.inject.ConfigProperty;\n" +
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n" +
                "\n" +
                "@ApplicationScoped\n" +
                "public class " + nombre + "LifeCycle {\n" +
                "\n" +
                "    private static final Logger LOGGER = LoggerFactory.getLogger(" + nombre + "LifeCycle.class);\n" +
                "\n" +
                "    private String instanceId;\n" +
                "\n" +
                "    @Inject\n" +
                "    Consul consulClient;\n" +
                "    @ConfigProperty(name = \"quarkus.application.name\")\n" +
                "    String appName;\n" +
                "    @ConfigProperty(name = \"quarkus.application.version\")\n" +
                "    String appVersion;\n" +
                "\n" +
                "\n" +
                "    void onStart(@Observes StartupEvent ev) {\n" +
                "        ScheduledExecutorService executorService = Executors\n" +
                "                .newSingleThreadScheduledExecutor();\n" +
                "        executorService.schedule(() -> {\n" +
                "            HealthClient healthClient = consulClient.healthClient();\n" +
                "            List<ServiceHealth> instances = healthClient\n" +
                "                    .getHealthyServiceInstances(appName).getResponse();\n" +
                "            instanceId = appName + \"-\" + instances.size();\n" +
                "            ImmutableRegistration registration = ImmutableRegistration.builder()\n" +
                "                    .id(instanceId)\n" +
                "                    .name(appName)\n" +
                "                    .address(\"127.0.0.1\")\n" +
                "                    .port(Integer.parseInt(System.getProperty(\"quarkus.http.port\")))\n" +
                "                    .putMeta(\"version\", appVersion)\n" +
                "                    .build();\n" +
                "            consulClient.agentClient().register(registration);\n" +
                "            LOGGER.info(\"Instance registered: id={}\", registration.getId());\n" +
                "        }, 5000, TimeUnit.MILLISECONDS);\n" +
                "    }\n" +
                "\n" +
                "    void onStop(@Observes ShutdownEvent ev) {\n" +
                "        consulClient.agentClient().deregister(instanceId);\n" +
                "        LOGGER.info(\"Instance de-registered: id={}\", instanceId);\n" +
                "    }\n" +
                "\n" +
                "}\n";

        try {
            File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/MicroServiceConsul/LifeCycle/" + nombre + "LifeCycle.java");
            if (myObj.createNewFile()) {
                // System.out.println("File created: " + myObj.getName());
            } else {
                //  System.out.println("Archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/MicroServiceConsul/LifeCycle/" + nombre + "LifeCycle.java");
            myWriter.write(clase_micro);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }


        String custApp =
                "package org.proyecto;\n" +
                        "\n" +
                        "import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;\n" +
                        "import org.eclipse.microprofile.openapi.annotations.info.Contact;\n" +
                        "import org.eclipse.microprofile.openapi.annotations.info.Info;\n" +
                        "import org.eclipse.microprofile.openapi.annotations.info.License;\n" +
                        "import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;\n" +
                        "import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;\n" +
                        "import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;" +
                        "\n" +
                        "import javax.ws.rs.core.Application;\n" +
                        "\n" +
                        "\n" +
                        "@OpenAPIDefinition( \n" +
                        "        info = @Info(\n" +
                        "                title=\"Java Framework\",\n" +
                        "                version = \"1.0.0 (Test)\",\n" +
                        "                contact = @Contact(\n" +
                        "                        name = \"API Explorer\",\n" +
                        "                        url = \"http://pucmm.edu.do/\",\n" +
                        "                        email = \"pucmmisc@example.com\"),\n" +
                        "                license = @License(\n" +
                        "                        name = \"Proyecto Final 1.0\",\n" +
                        "                        url = \"http://www.apache.org/licenses/LICENSE-2.0.html\")))\n" +
                        "@SecuritySchemes(value = {\n" +
                        "        @SecurityScheme(securitySchemeName = \"apiKey\",\n" +
                        "                type = SecuritySchemeType.HTTP,\n" +
                        "                scheme = \"Bearer\")}\n" +
                        ")" +
                        "public class CustomApplication extends Application {\n" +
                        "}";


        try {
            File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/CustomApplication.java");
            if (myObj.createNewFile()) {
                //   System.out.println("Archivo Creado: " + myObj.getName());
            } else {
                // System.out.println("Archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }


        try {
            FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/CustomApplication.java");
            myWriter.write(custApp
            );
            myWriter.close();
            //  System.out.println("ApiSwagger Inyectado");
        } catch (IOException e) {
            System.out.println("Se produjo un error.");
            e.printStackTrace();
        }

        if (seguridad == 1) {
            String UserApi = "package org.proyecto.Api;\n" +
                    "\n" +
                    "import org.eclipse.microprofile.config.inject.ConfigProperty;\n" +
                    "import org.eclipse.microprofile.openapi.annotations.tags.Tag;\n" +
                    "\n" +
                    "import javax.ws.rs.*;\n" +
                    "import javax.ws.rs.core.MediaType;\n" +
                    "import java.io.BufferedReader;\n" +
                    "import java.io.DataOutputStream;\n" +
                    "import java.io.IOException;\n" +
                    "import java.io.InputStreamReader;\n" +
                    "import java.net.HttpURLConnection;\n" +
                    "import java.net.MalformedURLException;\n" +
                    "import java.net.ProtocolException;\n" +
                    "import java.net.URL;\n" +
                    "import java.nio.charset.StandardCharsets;\n" +
                    "\n" +
                    "\n" +
                    "@Path(\"/UserToken\")\n" +
                    "@Tag(name = \"UserToken\", description = \"Here is all the information about User and Access Token.\")\n" +
                    "public class UserToken {\n" +
                    "\n" +
                    "    @ConfigProperty(name = \"grant_type\")\n" +
                    "    private String grant_type;\n" +
                    "    @ConfigProperty(name = \"quarkus.oidc.client-id\")\n" +
                    "    private String client_id;\n" +
                    "    @ConfigProperty(name = \"quarkus.oidc.credentials.secret\")\n" +
                    "    private String client_secret;\n" +
                    "    @ConfigProperty(name=\"urltoken_request\")\n" +
                    "    private String request;\n" +
                    "\n" +
                    "\n" +
                    "    @POST\n" +
                    "    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)\n" +
                    "    @Produces(MediaType.APPLICATION_JSON)\n" +
                    "    public String ReturnToken(@FormParam(\"username\") String username, @FormParam(\"password\") String password) {\n" +
                    "\n" +
                    "        String urlParameters = \"username=\" + username + \"&password=\" + password + \"&grant_type=\" + grant_type + \"&client_id=\" + client_id + \"&client_secret=\" + client_secret;\n" +
                    "\n" +
                    "        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);\n" +
                    "        int postDataLength = postData.length;\n" +
                    "        URL url = null;\n" +
                    "        try {\n" +
                    "            url = new URL(request);\n" +
                    "            HttpURLConnection conn = (HttpURLConnection) url.openConnection();\n" +
                    "            conn.setDoOutput(true);\n" +
                    "            conn.setInstanceFollowRedirects(false);\n" +
                    "            conn.setRequestMethod(\"POST\");\n" +
                    "            conn.setRequestProperty(\"Content-Type\", \"application/x-www-form-urlencoded\");\n" +
                    "            conn.setRequestProperty(\"charset\", \"utf-8\");\n" +
                    "            conn.setRequestProperty(\"Content-Length\", Integer.toString(postDataLength));\n" +
                    "            conn.setUseCaches(false);\n" +
                    "            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {\n" +
                    "                wr.write(postData);\n" +
                    "            }\n" +
                    "            BufferedReader in = new BufferedReader(\n" +
                    "                    new InputStreamReader(\n" +
                    "                            conn.getInputStream()));\n" +
                    "\n" +
                    "            StringBuilder response = new StringBuilder();\n" +
                    "            String currentLine;\n" +
                    "\n" +
                    "            while ((currentLine = in.readLine()) != null)\n" +
                    "                response.append(currentLine);\n" +
                    "\n" +
                    "            in.close();\n" +
                    "            return response.toString();\n" +
                    "        } catch (MalformedURLException e) {\n" +
                    "            e.printStackTrace();\n" +
                    "        } catch (ProtocolException e) {\n" +
                    "            e.printStackTrace();\n" +
                    "        } catch (IOException e) {\n" +
                    "            e.printStackTrace();\n" +
                    "        }\n" +
                    "        return \"\";\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "}\n";

            try {
                File myObj = new File(path + "/" + nombre + "/src/main/java/org/proyecto/Api/UserToken.java");
                if (myObj.createNewFile()) {
                    // System.out.println("File created: " + myObj.getName());
                } else {
                    //  System.out.println("Archivo ya existe.");
                }
            } catch (IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }

            try {
                FileWriter myWriter = new FileWriter(path + "/" + nombre + "/src/main/java/org/proyecto/Api/UserToken.java");
                myWriter.write(UserApi);
                myWriter.close();
                //   System.out.println("Clase api generado");
            } catch (IOException e) {
                System.out.println("Se produjo un error.");
                e.printStackTrace();
            }
        }

        //////////////////////////////////////////////////////////

        java.nio.file.Path path2 = Paths.get(path + "/" + nombre + "/pom.xml");
        Charset charset = StandardCharsets.UTF_8;

        String content = null;
        try {
            content = new String(Files.readAllBytes(path2), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        content = content.replaceAll("1.9.0.CR1", "1.8.2.Final");
        try {
            Files.write(path2, content.getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //////////////////////////////////////////////////////////

        theDir = new File(RutaCapertaMadre + "/" + nombre);
        if (!theDir.exists()) theDir.mkdirs();
        File from = new File(path + "/" + nombre);
        File to = new File(RutaCapertaMadre + "/" + nombre);
        try {
            Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Aplicacion creada con exito en carpeta madre." + RutaCapertaMadre);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public String getAppProperties() {
        return appProperties;
    }
}
