package com.example.g31_ffs_be.repository;
import com.example.g31_ffs_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


public interface UserRepository extends JpaRepository<User,String> {
    @Query(value = " SELECT u.* FROM `user` u WHERE u.verification_code like :code",nativeQuery = true)
     User findByVerificationCode(String code);
    @Modifying
    @Transactional
    @Query(value = " insert into user_service (user_id,service_id,date_buy,service_price) values (:user_id,:service_id,:date_buy,:service_price)"
            , nativeQuery = true)
    Integer insertUserService(String user_id, int service_id, LocalDateTime date_buy, double service_price);

    @Query(value = " SELECT  u FROM User u " +
            "where u.id like :id")
    User findByUserId(String id);

 /*   select a.user_id, SUM( c.duration-TIMESTAMPDIFF(Day,  a.date_buy,now())) as day_remain from user_service a
    left join user b on a.user_id=b.user_id
    left join service c on a.service_id=c.id
    where (b.is_member_ship=true)
    group by a.user_id*/

    @Query(value = "select  b.*  from `user_service` a " +
            "left join `user` b on a.user_id=b.user_id " +
            "left join `service` c on a.service_id=c.id " +
            "where (b.is_member_ship=true) " +
            "group by a.user_id " +
            "having SUM( c.duration-TIMESTAMPDIFF(Day,  a.date_buy,now()))>0 ",nativeQuery = true

    )
    List<User> getAllUserExpiredMembership();

}
