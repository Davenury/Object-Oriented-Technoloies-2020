package pl.edu.agh.school.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.name.Names;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.persistence.IPersistenceManager;
import pl.edu.agh.school.persistence.SerializableIPersistenceManager;

import javax.inject.Named;

public class SchoolModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IPersistenceManager.class).to(SerializableIPersistenceManager.class);

        bind(String.class).annotatedWith(Names.named("teachersFile"))
                .toInstance("guice-teachers.dat");
        bind(String.class).annotatedWith(Names.named("classFile"))
                .toInstance("guice-classes.dat");
        bind(String.class).annotatedWith(Names.named("loggerFile"))
                .toInstance("persistence.log");
    }
}
