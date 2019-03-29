CREATE TABLE `user` (
  `user_id`          BIGINT PRIMARY KEY        AUTO_INCREMENT,
  `user_name`        VARCHAR(20) UNIQUE NOT NULL,
  `password`         VARCHAR(255)       NOT NULL,
  `last_register_ip` VARCHAR(64)               DEFAULT NULL,
  `email`            VARCHAR(64) UNIQUE        DEFAULT NULL,
  `home_url`         VARCHAR(200)              DEFAULT NULL,
  `group_name`       VARCHAR(16)               DEFAULT 'visitor'
);

CREATE TABLE `article` (
  `a_id`          BIGINT PRIMARY KEY AUTO_INCREMENT,
  `a_title`       VARCHAR(140) NOT NULL,
  `a_content`     TEXT         NOT NULL,
  `create_time`   TIMESTAMP    NOT NULL,
  `category`      VARCHAR(50),
  `category_id`   BIGINT,
  `tags`          VARCHAR(50),
  `hits`          MEDIUMINT    NOT NULL,
  `comment_nums`  SMALLINT,
  `allow_comment` TINYINT            DEFAULT 1,
  `status`        VARCHAR(20)        DEFAULT 'published'
);

CREATE TABLE `meta` (
  `m_id`        BIGINT PRIMARY KEY AUTO_INCREMENT,
  `m_type`      VARCHAR(10) NOT NULL,
  `m_content`   VARCHAR(20) NOT NULL,
  `description` VARCHAR(255),
  `parent`      BIGINT             DEFAULT '0'
);

CREATE TABLE `comment` (
  `co_id`      BIGINT PRIMARY KEY AUTO_INCREMENT,
  `co_content` VARCHAR(255) NOT NULL,
  `mail`       VARCHAR(200)       DEFAULT NULL,
  `url`        VARCHAR(200)       DEFAULT NULL,
  `ip`         VARCHAR(64)        DEFAULT NULL,
  `author`     VARCHAR(200)       DEFAULT NULL,
  `author_id`  BIGINT             DEFAULT '0',
  `status`     VARCHAR(16)        DEFAULT 'approved',
  `parent`     BIGINT             DEFAULT '0'
);

CREATE TABLE `t_logs` (
  `id`        BIGINT NOT NULL AUTO_INCREMENT,
  `action`    VARCHAR(100)    DEFAULT NULL,
  `data`      VARCHAR(2000)   DEFAULT NULL,
  `author_id` INT(10)         DEFAULT NULL,
  `ip`        VARCHAR(20)     DEFAULT NULL,
  `created`   INT(10)         DEFAULT NULL,
  PRIMARY KEY (`id`)
);
