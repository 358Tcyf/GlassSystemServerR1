package project.ys.glass_system.model.p.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.AlarmTag;
import project.ys.glass_system.model.p.entity.PushSet;

import java.util.List;

public interface AlarmTagDao extends JpaRepository<AlarmTag, Integer> {

    AlarmTag findByContent(String content);

    AlarmTag findByContentAndMinAndMax(String content,float min,float max);

    List<AlarmTag> findBySet(PushSet set);
}
