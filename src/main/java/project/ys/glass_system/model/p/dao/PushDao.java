package project.ys.glass_system.model.p.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.ys.glass_system.model.p.entity.Push;
import project.ys.glass_system.model.p.entity.Role;
import project.ys.glass_system.model.p.entity.User;

import java.util.Collection;
import java.util.List;

public interface PushDao extends JpaRepository<Push, Integer> {

    List<Push> findPushesByReceiver(String receiver);

    Push findByPushUuid(String pushUuid);

    Page<Push> queryPushesByTitleLikeAndCreateTimeBetweenAndReceiverLike(String title, long createTime, long createTime2, String receiver, Pageable pageable);

    Page<Push> queryPushesByTitleLikeAndCreateTimeBetweenAndReceiverLikeAndHaveRead(String title, long createTime, long createTime2, String receiver, boolean haveRead, Pageable pageable);

    Page<Push> queryPushesByTitleLikeAndCreateTimeBetweenAndReceiverLikeAndReceiverIn(String title, long createTime, long createTime2, String receiver, Collection<String> receiver2, Pageable pageable);

    Page<Push> queryPushesByTitleLikeAndCreateTimeBetweenAndReceiverLikeAndReceiverInAndHaveRead(String title, long createTime, long createTime2, String receiver, Collection<String> receiver2, boolean haveRead, Pageable pageable);
}
