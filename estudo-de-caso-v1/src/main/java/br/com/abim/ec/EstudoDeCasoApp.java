package br.com.abim.ec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class EstudoDeCasoApp {

    private static final Logger log = LoggerFactory.getLogger(EstudoDeCasoApp.class);



    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EstudoDeCasoApp.class);
        app.run(args);
    }
    
 

}
