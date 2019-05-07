package project.ys.glass_system.service.t;

import java.time.LocalDate;

public interface AutoProduceService {

    void produceStart(LocalDate date, boolean ignore);

    void produce(boolean ignore);

    void store(boolean ignore);

    void testStart(LocalDate date, boolean ignore);


    void test(boolean ignore);
}
