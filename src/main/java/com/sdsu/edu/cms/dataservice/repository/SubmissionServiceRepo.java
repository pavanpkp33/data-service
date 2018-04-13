package com.sdsu.edu.cms.dataservice.repository;


import com.sdsu.edu.cms.common.models.cms.Authors;
import com.sdsu.edu.cms.common.models.cms.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class SubmissionServiceRepo implements DataAccessRepository{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Object> findAll(String query, Object... params) {
       return null;
    }

    @Override
    public Object findOne(String query, Object... params) {
        HashMap<Integer, HashSet<String>> set =  jdbcTemplate.query(query, params, rs -> {
            HashMap<Integer, HashSet<String>> data = new HashMap<>();
            HashSet<String> set1;
            while(rs.next()){
                if(data.containsKey(rs.getInt(1))){
                    set1 =  data.get(rs.getInt(1));
                    set1.add(rs.getString(2));
                    data.put(rs.getInt(1), set1);
                }else{
                    set1 = new HashSet<>();
                    set1.add(rs.getString(2));
                    data.put(rs.getInt(1), set1);
                }
            }

            return data;
        });
        return set;
    }

    @Override
    public Object findById(String query, String id) {
        return null;
    }

    @Override
    public int save(String query, Object... params) {
        try{
            int i = jdbcTemplate.update(query, params);
            return i;
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public int update(String query, Object... params) {
        return 0;
    }

    public int delete(String query, Object... params){
        try{
            return jdbcTemplate.update(query, params);

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public List<Submission> findSubmisisons(String query, Object ...params){
        return jdbcTemplate.query(query, params, rs -> {
            Submission submission;
            List<Submission> subList = new ArrayList<>();
            while(rs.next()){
                submission = new Submission();
                submission.setSid(rs.getString(1));
                submission.setTitle(rs.getString(2));
                submission.setSubmission_date(rs.getTimestamp(3));
                submission.setSubmit_author_id(rs.getString(4));
                submission.setTrack_id(rs.getInt(5));
                submission.setAbstract_text(rs.getString(6));
                submission.setLast_updated(rs.getTimestamp(7));
                submission.setDecision_status(rs.getString(8));
                submission.setIs_paid(rs.getString(9));
                submission.setGroup_app(rs.getInt(10));
                submission.setSubmitAuthorName(rs.getString(11));
                submission.setSubmitAuthorEmail(rs.getString(12));

                subList.add(submission);
            }

            return subList;
        });
    }

    public String[] getKeywords(String query, Object ...params){
        return jdbcTemplate.query(query, params, rs ->{
           List<String> kws = new ArrayList<>();
           while(rs.next()){
               kws.add(rs.getString(1));
           }

           return kws.toArray(new String[0]);
        });
    }

    public List<Authors> getAuthors(String query, Object ...params){
        return jdbcTemplate.query(query, params, rs->{
            List<Authors> authorsList = new ArrayList<>();
            Authors authors;
            while(rs.next()){
                authors = new Authors();
                authors.setId(rs.getString(1));
                authors.setIs_corresponding(rs.getString(2));
                authors.setFirst_name(rs.getString(3));
                authors.setLast_name(rs.getString(4));
                authors.setEmail(rs.getString(5));
                authorsList.add(authors);
            }
           return authorsList;

        });
    }

    public Map getFilesData(String query, Object ...params){
        return jdbcTemplate.query(query, params, rs ->{
           Map<Integer, String> map = new HashMap<>();
           while (rs.next()){
               map.put(rs.getInt(1), rs.getString(2));
           }

           return map;
        });
    }




}
