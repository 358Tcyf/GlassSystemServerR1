package project.ys.glass_system.model.p.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.Alarm;
import project.ys.glass_system.model.p.entity.Push;

import java.util.Collection;
import java.util.List;

public interface AlarmDao extends JpaRepository<Alarm, Integer> {

    List<Alarm> findAlarmsByReceiver(String receiver);

    Alarm findByAlarmUuid(String alarmUuid);

    Page<Alarm> queryAlarmsByTitleLikeAndCreateTimeBetweenAndReceiverLike(String title, long createTime, long createTime2, String receiver, Pageable pageable);

    Page<Alarm> queryAlarmsByTitleLikeAndCreateTimeBetweenAndReceiverLikeAndHaveRead(String title, long createTime, long createTime2, String receiver, boolean haveRead, Pageable pageable);

    Page<Alarm> queryAlarmsByTitleLikeAndCreateTimeBetweenAndReceiverLikeAndReceiverIn(String title, long createTime, long createTime2, String receiver, Collection<String> receiver2, Pageable pageable);

    Page<Alarm> queryAlarmsByTitleLikeAndCreateTimeBetweenAndReceiverLikeAndReceiverInAndHaveRead(String title, long createTime, long createTime2, String receiver, Collection<String> receiver2, boolean haveRead, Pageable pageable);
}
