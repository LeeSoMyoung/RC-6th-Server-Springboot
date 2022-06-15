package com.example.demo.src.view;

import com.example.demo.src.videos.model.PostVideoReq;
import com.example.demo.src.view.model.PostVideoViewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ViewDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){this.jdbcTemplate = new JdbcTemplate(dataSource);}

    public  int createVideoView(PostVideoViewReq postVideoViewReq){
        String      createViewQuery = "INSERT INTO \n" +
                "Views(userId, videoId) VALUES (?, ?);";
        Object[]    createViewQueryParams = new Object[]{postVideoViewReq.getUserId(), postVideoViewReq.getVideoId()};
        return  this.jdbcTemplate.update(createViewQuery, createViewQueryParams);
    }
}