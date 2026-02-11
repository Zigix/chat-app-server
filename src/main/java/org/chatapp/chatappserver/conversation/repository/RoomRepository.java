package org.chatapp.chatappserver.conversation.repository;

import org.chatapp.chatappserver.conversation.entity.Room;
import org.chatapp.chatappserver.conversation.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("""
            select r
            from Room r
            where exists (
                select 1
                from RoomMember m
                where m.roomId = r.roomId
                  and m.userId = :userId
            )
            """)
    List<Room> findByMemberUserId(@Param("userId") Long userId);

    @Query("""
            select r
            from Room r, RoomMember rm
            where r.roomId = rm.roomId
              and r.type = :type
            group by r
            having count(distinct rm.userId) = 2
               and sum(case when rm.userId in (:userId1, :userId2) then 1 else 0 end) = 2
            """)
    Optional<Room> findDirectMessageRoomForTwoUsers(
            @Param("type") RoomType type,
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2
    );
}
