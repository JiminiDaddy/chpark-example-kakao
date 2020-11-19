package kakao.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/19
 * Time : 7:22 AM
 */

@Getter
@Entity
@Table(name = "Rooms")
public class Room {
    @Id
    private String id;

    @OneToMany(mappedBy = "room")
    private Set<Member> members;
}
