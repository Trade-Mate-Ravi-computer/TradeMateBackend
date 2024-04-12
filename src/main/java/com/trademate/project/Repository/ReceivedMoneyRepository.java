package com.trademate.project.Repository;

import com.trademate.project.Model.ReceivedMoneyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.lang.Long;
public interface ReceivedMoneyRepository extends JpaRepository<ReceivedMoneyModel, Long> {
}
