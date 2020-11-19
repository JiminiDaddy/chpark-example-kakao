package kakao.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/20
 * Time : 12:33 AM
 */

public interface MemberRepository extends JpaRepository<Member, Long> {

}
