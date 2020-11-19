package kakao.domain;

import lombok.Getter;

import javax.persistence.*;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/19
 * Time : 10:22 PM
 */

@Getter
@Entity
@Table(name = "Members")
public class Member {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Room room;

    public Member(Long id) {
        this.id = id;
    }
}
