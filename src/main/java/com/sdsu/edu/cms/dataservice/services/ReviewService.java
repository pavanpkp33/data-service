package com.sdsu.edu.cms.dataservice.services;

import com.google.gson.Gson;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.common.models.review.Review;
import com.sdsu.edu.cms.dataservice.repository.ReviewServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    @Autowired
    ReviewServiceRepo reviewServiceRepo;

    public ServiceResponse getAllReviewers(String cid){
        return new ServiceResponse(reviewServiceRepo.findAll(Query.GET_REVIEWERS_CONF, cid),
                "Reviewers queried successfully.");
    }

    public ServiceResponse addReview(Review review){
        review.setReviewId(UUID.randomUUID().toString());
        try {
            reviewServiceRepo.save(Query.ADD_REVIEW, review.getParams());
            return new ServiceResponse(Arrays.asList(true), "Review added successful");
        }catch (Exception e){
            e.printStackTrace();
            return new ServiceResponse(Arrays.asList(false), "Failed to add review");
        }

    }

    public ServiceResponse getReviewBySubmissionID(String sid){
        List<Review> reviews = reviewServiceRepo.findReviewBySubmisison(Query.GET_REVIEW_SUBID, sid);
        String serializedReviews = new Gson().toJson(reviews).toString();
       return new ServiceResponse(Arrays.asList(serializedReviews), "Reviews queried successfully");

    }
}
