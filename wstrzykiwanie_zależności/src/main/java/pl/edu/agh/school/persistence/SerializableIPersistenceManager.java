package pl.edu.agh.school.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import jdk.jfr.Name;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.SchoolClass;
import pl.edu.agh.school.Teacher;

import javax.inject.Named;

public final class SerializableIPersistenceManager implements IPersistenceManager {

    //private static final Logger log = Logger.getInstance();

    private String teachersStorageFileName;

    private String classStorageFileName;

    @Inject
    private Logger logger;

    @Inject
    public SerializableIPersistenceManager() {
        teachersStorageFileName = "teachers.dat";
        classStorageFileName = "classes.dat";
    }

    public SerializableIPersistenceManager(String teachersStorageFileName, String classStorageFileName){
        this.teachersStorageFileName = teachersStorageFileName;
        this.classStorageFileName = classStorageFileName;
    }

    public void saveTeachers(List<Teacher> teachers) {
        if (teachers == null) {
            throw new IllegalArgumentException();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(teachersStorageFileName))) {
            oos.writeObject(teachers);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            logger.log("There was an error while saving the teachers data", e);
        }
        logger.log("Teachers Saved");
    }

    @SuppressWarnings("unchecked")
    public List<Teacher> loadTeachers() {
        ArrayList<Teacher> res = null;
        try (ObjectInputStream ios = new ObjectInputStream(new FileInputStream(teachersStorageFileName))) {

            res = (ArrayList<Teacher>) ios.readObject();
        } catch (FileNotFoundException e) {
            res = new ArrayList<>();
        } catch (IOException e) {
            logger.log("There was an error while loading the teachers data", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        logger.log("Teachers Loaded");
        return res;
    }

    public void saveClasses(List<SchoolClass> classes) {
        if (classes == null) {
            throw new IllegalArgumentException();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(classStorageFileName))) {
            oos.writeObject(classes);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            logger.log("There was an error while saving the classes data", e);
        }
        logger.log("Classes Saved");
    }

    @SuppressWarnings("unchecked")
    public List<SchoolClass> loadClasses() {
        ArrayList<SchoolClass> res = null;
        try (ObjectInputStream ios = new ObjectInputStream(new FileInputStream(classStorageFileName))) {
            res = (ArrayList<SchoolClass>) ios.readObject();
        } catch (FileNotFoundException e) {
            res = new ArrayList<>();
        } catch (IOException e) {
            logger.log("There was an error while loading the classes data", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        logger.log("Classes Loaded");
        return res;
    }

    @Inject
    public void setClassStorageFileName(@Named("classFile") String classStorageFileName) {
        this.classStorageFileName = classStorageFileName;
    }

    @Inject
    public void setTeachersStorageFileName(@Named("teachersFile") String teachersStorageFileName) {
        this.teachersStorageFileName = teachersStorageFileName;
    }
}
