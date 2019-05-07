package project.ys.glass_system.service.t;

import java.time.LocalDate;
import java.util.Map;

public interface AutoProduceService {

    void produceStart(LocalDate date, boolean ignore);

    void produce(boolean ignore);

    void store(boolean ignore);

    void testStart(LocalDate date, boolean ignore);

    void test(boolean ignore);


    Map<String, Object> factoryQuery(int page, int limit);
    Map<String, Object> produceQuery(int page, int limit);
    Map<String, Object> glassQuery(int page, int limit);
    Map<String, Object> wareHouseQuery(int page, int limit);
    Map<String, Object> testQuery(int page, int limit);
}
