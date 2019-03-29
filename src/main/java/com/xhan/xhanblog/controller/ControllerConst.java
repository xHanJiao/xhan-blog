package com.xhan.xhanblog.controller;

public interface ControllerConst {
    String SLASH = "/";
    String REDIRECT = "REDIRECT:";
    String ADMIN_LOGIN_PAGE = "adminlogin";
    String ADMIN_LOGIN_URL = SLASH + ADMIN_LOGIN_PAGE;
    String CONTROL_CENTER_PAGE = "center";
    String CONTROL_CENTER_URL = SLASH + CONTROL_CENTER_PAGE;
    String LOGOUT_URL = "logout";
    String ARTICLE_PAGE = "article";
    String UPDATE_USER_INFO = CONTROL_CENTER_URL + SLASH + "update";
    String PAGE_401 = "401";
    String ARTICLE_URL = SLASH + ARTICLE_PAGE;
    String LIST_ARTICLE_URL = ARTICLE_URL + SLASH + "list";
    String DELETE_ARTICLE_URL = ARTICLE_URL + SLASH + "delete";
    String UPLOAD_ARTICLE_URL = ARTICLE_URL + SLASH + "upload";
    String RECOVER_ARTICLE_URL = ARTICLE_URL + SLASH + "recover";
    String ARTICLE_SHOW_PAGE = "article";
    String INDEX_PAGE = "index";
    String CATEGORY_ARTICLE = "category";
    String CATEGORY_URL = SLASH + CATEGORY_ARTICLE;
    String COMMENT_URL = SLASH + "comment";
    String COMMENT_URL_DECENT = COMMENT_URL + SLASH + "decent";
}
