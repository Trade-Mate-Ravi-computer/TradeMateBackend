package com.trademate.project.Repository;

import com.trademate.project.Model.FeedbackModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<FeedbackModel,Long> {
    FeedbackModel findByName(String name);
}
