package kakao.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Choen-hee Park
 * User : chpark
 * Date : 2020/11/20
 * Time : 12:11 AM
 */

public interface MoneyThrowingRepository extends JpaRepository<MoneyThrowing, String> {
}
