package pl.polsl.student.skrd.settings;

public interface GlobalSettings {

    boolean IS_SPACE_TUNER_ON = true;
    long HEAP_SIZE = 16*1024;
    int NON_LOS_SIZE = 4;
    int LOS_SIZE = 100;
    int ITERATIONS_NUMBER = 100000;

    int MAX_LOS_LIFESPAN = 300;
    int MAX_NON_LOS_LIFESPAN = 300;

    int MIN_LOS_PER_ITERATION = 3;
    int MAX_LOS_PER_ITERATION = 6;

    int MIN_NON_LOS_PER_ITERATION = 300;
    int MAX_NON_LOS_PER_ITERATION = 600;

    int THRESHOLD = 20;
}
