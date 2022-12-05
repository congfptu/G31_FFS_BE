package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.dto.CountDto;
import com.example.g31_ffs_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


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
            "left join `service` c on a.service_id=c.id " +
            "where (c.duration-TIMESTAMPDIFF(Day, a.date_buy,now())>0) " +
            "group by  a.user_id " +
            "having SUM( c.duration-TIMESTAMPDIFF(Day, a.date_buy,now()))>0 " +
            ")", nativeQuery = true

    )
    Integer updateMemberShipExpired();

    @Modifying
    @Transactional
    @Query(value = "SET SQL_SAFE_UPDATES = 0", nativeQuery = true)
    Integer updateNotSafe();
    @Modifying
    @Transactional
    @Query(value = "SET SQL_SAFE_UPDATES = 1", nativeQuery = true)
    Integer updateSafe();

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

    @Query(value = "select  b.num_day*60*24-TIMESTAMPDIFF(minute,a.date,now()) from ban a " +
            "left join type_ban b on a.type_ban=b.id " +
            "where user_id=:userId and a.date= (select max(date) from ban where user_id=:userId)", nativeQuery = true

    )
    Integer countTimeBanRemain(String userId);


    @Query(value = "select CONCAT(MONTHNAME(STR_TO_DATE(month(a.created_date), '%m')),' ',year(a.created_date)) AS 'month',count(*) as countNumber from account a\n" +
            "inner join freelancer b on a.id=b.freelancer_id\n" +
            "where  a.created_date>=DATE_SUB(NOW(), INTERVAL 7 MONTH) and a.created_date<=NOW()\n" +
            "group by month(a.created_date)\n" +
            "order by month(a.created_date) asc ", nativeQuery = true

    )
    List<Object[]> countFreelancer();

    @Query(value = "select CONCAT(MONTHNAME(STR_TO_DATE(month(a.created_date), '%m')),' ',year(a.created_date)) AS 'month',count(*) as countNumber from account a\n" +
            "inner join recruiter b on a.id=b.recruiter_id\n" +
            "where  a.created_date>=DATE_SUB(NOW(), INTERVAL 7 MONTH) and a.created_date<=NOW()\n" +
            "group by month(a.created_date)\n" +
            "order by month(a.created_date) asc ", nativeQuery = true

    )
    List<Object[]> countRecruiter();

    @Query(value = "select MONTHNAME(STR_TO_DATE(month(a.time), '%m')) AS 'month',IFNULL(count(*), 0) as countNumber from jobs a " +
            "where year(a.time)=year(now()) " +
            "group by month(a.time) ", nativeQuery = true

    )
    List<Object[]> countPosted();

    @Query(value = "select CONCAT(MONTHNAME(STR_TO_DATE(month(a.apply_date), '%m')),' ',year(a.apply_date)) AS 'month',count(*) as countNumber from job_request a\n" +
            "where a.apply_date>=DATE_SUB(NOW(), INTERVAL 7 MONTH) and a.apply_date<=NOW()\n" +
            "group by month(a.apply_date);", nativeQuery = true

    )
    List<Object[]> countApplies();

    @Query(value = "select month,SUM(totalAmount) as total from (\n" +
            "select  CONCAT(MONTHNAME(STR_TO_DATE(month(a.time), '%m')),' ',year(a.time)) AS 'month',IFNULL(SUM(a.fee), 0)  totalAmount from jobs a\n" +
            "where  a.time>=DATE_SUB(NOW(), INTERVAL 7 MONTH) and a.time<=NOW()\n" +
            "group by month(a.time)\n" +
            "union all\n" +
            "select  CONCAT(MONTHNAME(STR_TO_DATE(month(b.apply_date), '%m')),' ',year(b.apply_date)) AS 'month',  IFNULL(SUM(b.fee), 0) as totalAmount from job_request b\n" +
            "where  b.apply_date>=DATE_SUB(NOW(), INTERVAL 7 MONTH) and b.apply_date<=NOW()\n" +
            "group by month(b.apply_date)\n" +
            "union all\n" +
            "select  CONCAT(MONTHNAME(STR_TO_DATE(month(c.date_buy), '%m')),' ',year(c.date_buy)) AS 'month',IFNULL(SUM(c.service_price), 0)  totalAmount from user_service c\n" +
            "where  c.date_buy>=DATE_SUB(NOW(), INTERVAL 7 MONTH) and c.date_buy<=NOW()\n" +
            "group by month(c.date_buy)\n" +
            "union all\n" +
            "select  CONCAT(MONTHNAME(STR_TO_DATE(month(d.date_buy), '%m')),' ',year(d.date_buy)) AS 'month',IFNULL(SUM(d.fee), 0) as totalAmount  from view_profile_history d\n" +
            "where  d.date_buy>=DATE_SUB(NOW(), INTERVAL 7 MONTH) and d.date_buy<=NOW()\n" +
            "group by month(d.date_buy)\n" +
            "union all\n" +
            "select  CONCAT(MONTHNAME(STR_TO_DATE(month(e.date_push), '%m')),' ',year(e.date_push)) AS 'month',IFNULL(SUM(e.fee), 0) as totalAmount  from push_top_history e\n" +
            "where  e.date_push>=DATE_SUB(NOW(), INTERVAL 7 MONTH) and e.date_push<=NOW()\n" +
            "group by month(e.date_push)\n" +
            ")f\n" +
            "group by f.month;", nativeQuery = true

    )
    List<Object[]> countRevenues();
    @Query(value = "select IFNULL(count(*), 0) from career", nativeQuery = true)
    Integer countCareer();
    @Query(value = "select IFNULL(count(*), 0) from subcareer", nativeQuery = true)
    Integer countSubCareer();
    @Query(value = "select IFNULL(count(*), 0) from user a where a.is_member_ship=1", nativeQuery = true)
    Integer countMemberShip();
    @Query(value = "select IFNULL(count(*), 0)from report", nativeQuery = true)
    Integer countReport();

    @Query(value = "select f.freelancer_id,acc.email,u.fullname,IFNULL(sum(totalAmount),0) as TotalMoneyUsed  from(\n" +
            "select a.freelancer_id, IFNULL(sum(c.fee),0) as totalAmount from freelancer a \n" +
            "left join job_request c on c.freelancer_id=a.freelancer_id\n" +
            "group by a.freelancer_id\n" +
            "union all \n" +
            "select a.freelancer_id, IFNULL(sum(d.service_price),0) as totalAmount from freelancer a \n" +
            "left join user_service d on d.user_id=a.freelancer_id\n" +
            "group by a.freelancer_id\n" +
            "union all \n" +
            "select a.freelancer_id, IFNULL(sum(e.fee),0) as totalAmount from freelancer a \n" +
            "left join view_profile_history e on e.user_id=a.freelancer_id\n" +
            "group by a.freelancer_id\n" +
            ")f \n" +
            "inner join user u on u.user_id=f.freelancer_id\n" +
            "inner join account acc on f.freelancer_id=acc.id\n" +
            "group by f.freelancer_id\n" +
            "order by TotalMoneyUsed desc "+
            "limit 10; ", nativeQuery = true

    )
    List<Object[]> topFreelancerHot();
    @Query(value = "select f.recruiter_id,acc.email,u.fullname,IFNULL(sum(totalAmount),0) as TotalMoneyUsed  from(\n" +
            "select a.recruiter_id, IFNULL(sum(b.fee),0) as totalAmount from recruiter a \n" +
            "left join push_top_history b on b.recruiter_id=a.recruiter_id\n" +
            "group by a.recruiter_id\n" +
            "union all\n" +
            "select a.recruiter_id, IFNULL(sum(c.fee),0) as totalAmount from recruiter a \n" +
            "left join jobs c on c.create_by=a.recruiter_id\n" +
            "group by a.recruiter_id\n" +
            "union all \n" +
            "select a.recruiter_id, IFNULL(sum(d.service_price),0) as totalAmount from recruiter a \n" +
            "left join user_service d on d.user_id=a.recruiter_id\n" +
            "group by a.recruiter_id\n" +
            "union all \n" +
            "select a.recruiter_id, IFNULL(sum(e.fee),0) as totalAmount from recruiter a \n" +
            "left join view_profile_history e on e.user_id=a.recruiter_id\n" +
            "group by a.recruiter_id\n" +
            ")f \n" +
            "inner join user u on u.user_id=f.recruiter_id\n" +
            "inner join account acc on f.recruiter_id=acc.id\n" +
            "group by f.recruiter_id \n" +
            "order by TotalMoneyUsed desc "+
            "limit 10;", nativeQuery = true
    )
    List<Object[]> topRecruiterHot();
}
