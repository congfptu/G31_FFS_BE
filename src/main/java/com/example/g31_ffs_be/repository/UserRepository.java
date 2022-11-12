package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = " SELECT u.* FROM `user` u WHERE u.verification_code like :code", nativeQuery = true)
    User findByVerificationCode(String code);

    @Modifying
    @Transactional
    @Query(value = " insert into user_service (user_id,service_id,date_buy,service_price) values (:user_id,:service_id,:date_buy,:service_price)"
            , nativeQuery = true)
    Integer insertUserService(String user_id, int service_id, LocalDateTime date_buy, double service_price);

    @Modifying
    @Transactional
    @Query(value = "  update user set is_member_ship=false" +
                 " where  (user_id=:userId)"
            , nativeQuery = true)
    Integer updateIsMemberShip(String userId);


    @Query(value = " SELECT  u FROM User u " +
            "where u.id like :id")
    User findByUserId(String id);

 /*   select a.user_id, SUM( c.duration-TIMESTAMPDIFF(Day,  a.date_buy,now())) as day_remain from user_service a
    left join user b on a.user_id=b.user_id
    left join service c on a.service_id=c.id
    where (b.is_member_ship=true)
    group by a.user_id*/
    @Modifying
   @Transactional
    @Query(value = "update `user` set is_member_ship=false where " +
            "is_member_ship=true and user_id not in" +
            "(" +
            "select  a.user_id from `user_service` a " +
            "left join `service` c on a.service_id=c.id "+
            "where (c.duration-TIMESTAMPDIFF(Day, a.date_buy,now())>0) " +
            "group by  a.user_id " +
            "having SUM( c.duration-TIMESTAMPDIFF(Day, a.date_buy,now()))>0 " +
            ")", nativeQuery = true

    )
    Integer updateMemberShipExpired();
    @Modifying
    @Transactional
    @Query(value = "update `user` set is_banned=false " +
            "where user_id in " +
            "(" +
            "select distinct a.user_id from `ban` a " +
            "left join `type_ban` b on a.type_ban=b.id " +
            "where b.num_day-TIMESTAMPDIFF(minute,(select max(date) from ban where user_id=a.user_id),now())<=0 " +
            ")", nativeQuery = true

    )
    Integer updateBanUserExpired();


}
