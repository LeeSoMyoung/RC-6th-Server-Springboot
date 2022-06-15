package com.example.demo.src.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewProvider {
    final Logger    logger = LoggerFactory.getLogger(this.getClass());

    private final ViewDao   viewDao;

    @Autowired
    public ViewProvider(ViewDao viewDao){
        this.viewDao = viewDao;
    }

}