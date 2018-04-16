package com.sdsu.edu.cms.dataservice.services;

import com.google.gson.Gson;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.common.models.review.Review;
import com.sdsu.edu.cms.dataservice.repository.ReviewServiceRepo;
import com.sdsu.edu.cms.dataservice.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

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
        List<Review> reviews = reviewServiceRepo.findReview(Query.GET_REVIEW_SUBID, sid);
        String serializedReviews = new Gson().toJson(reviews).toString();
       return new ServiceResponse(Arrays.asList(serializedReviews), "Reviews queried successfully");

    }

    public ServiceResponse getReviewByReviewId(String rid){
        List<Review> review = reviewServiceRepo.findReview(Query.GET_REVIEW_BYID, rid);
        String serializedReview = new Gson().toJson(review).toString();
        return new ServiceResponse(Arrays.asList(serializedReview), "Review queried successfully");
    }

    public ServiceResponse updateReview(Review review){
        String query = buildQuery(review);
        reviewServiceRepo.save(query, null);
        return new ServiceResponse(Arrays.asList(true), "Review updated successfully");
    }

    private String buildQuery(Review review){
        Map<String, String> columns = new HashMap<>();
        columns.put("reviewId","rid" );
        columns.put("review", "review");
        columns.put("score", "Score");
        columns.put("messageChair", "message_to_chair");
        columns.put("confidenceScore", "confidence_score");
        columns.put("publish", "publish");


        String query = "UPDATE REVIEWS SET ";
        boolean flag = true;
        for (Field field : review.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(review);
                if(value != null){

                    if(flag){
                        if(field.getName().equals("score") || field.getName().equals("confidenceScore")){
                            query += columns.get(field.getName()) +" = "+value; continue;
                        }
                        query += columns.get(field.getName()) +" = \""+value.toString()+"\"";
                        flag=false;
                    }else{
                        if(field.getName().equals("score") || field.getName().equals("confidenceScore")){
                            query += ", "+ columns.get(field.getName()) +" = "+value; continue;
                        }
                        query += ", "+ columns.get(field.getName()) +" = \""+value.toString()+ "\" ";
                    }

                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        query += ", last_updated = now() WHERE rid = \""+review.getReviewId()+"\"";
        return query;
    }

    public ServiceResponse publishReviews(String sid){
        reviewServiceRepo.save(Query.PUBLISH_REVIEWS, sid);
        return new ServiceResponse(Arrays.asList(true), "Reviews published successfully.");

    }

    public ServiceResponse deleteReview(String rid) {
        reviewServiceRepo.save(Query.DELETE_REVIEW, rid);
        return new ServiceResponse(Arrays.asList(true), "Review deleted successfully.");
    }
}
