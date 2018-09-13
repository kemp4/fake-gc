package pl.polsl.student.skrd.settings;

public interface GlobalSettings {
    boolean IS_SPACE_TUNER_ON = true;
    long HEAP_SIZE = 16*1024;
    int MAX_LOS_LIFESPAN = 3;
    int MAX_NON_LOS_LIFESPAN = 3;
    int NON_LOS_SIZE = 4;
    int LOS_SIZE = 100;
    int THRESHOLD = 20;
    int ITERATIONS_NUMBER = 10000;

    int MIN_LOS_PER_ITERATION = 1;
    int MAX_LOS_PER_ITERATION = 3;

    int MIN_NON_LOS_PER_ITERATION = 10;
    int MAX_NON_LOS_PER_ITERATION = 60;


}
