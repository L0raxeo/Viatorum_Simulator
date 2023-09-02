package l0raxeo.arki.engine.mainApp;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AppConfig
{

    String windowTitle();
    int windowWidth();
    int windowHeight();
    boolean resizeable();
    int framesPerSecondCap() default 60;
    int updatesPerSecond() default 60;

}
