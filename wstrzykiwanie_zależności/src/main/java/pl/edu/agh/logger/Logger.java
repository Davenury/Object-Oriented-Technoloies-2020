package pl.edu.agh.logger;

import com.google.inject.Inject;
import jdk.jfr.Name;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class Logger {
    protected static Logger logger;

    protected DateFormat dateFormat;

    protected Set<IMessageSerializer> registeredSerializers;

    private String fileName;

    @Inject
    public void setFileName(@Named("loggerFile") String fileName){
        this.fileName = fileName;
    }

    public Logger() {
        init();
        this.registeredSerializers = new HashSet<IMessageSerializer>();
    }

    public Logger(Set<IMessageSerializer> registeredSerializers) {
        init();
        if (registeredSerializers == null) {
            throw new IllegalArgumentException("null argument");
        }
        this.registeredSerializers = registeredSerializers;
    }

    public static Logger getInstance() {
        if (logger == null)
            logger = new Logger();
        return logger;
    }

    public void registerSerializer(IMessageSerializer messageSerializer) {
        registeredSerializers.add(messageSerializer);
    }

    public void log(String message) {
        //log(message, null);
        /**Tu wpisz do pliku persistence.log*/
        System.out.println("File name: " + this.fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.fileName))) {
            oos.writeObject(message);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            this.log("There was an error while saving the classes data", e);
        }
    }

    public void log(String message, Throwable error) {
        for (IMessageSerializer messageSerializer : registeredSerializers) {
            String formattedMessage = dateFormat.format(new Date())
                    + ": " + message + (error != null ? error.toString() : "");
            messageSerializer.serializeMessage(formattedMessage);
        }
    }

    private void init() {
        dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    }

}
