package com.example.action;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Index action for the home page
 */
public class IndexAction extends ActionSupport {
    private static final long serialVersionUID = 1L;
    
    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }
}