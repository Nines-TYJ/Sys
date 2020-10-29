DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user
(
  id VARCHAR(32) NOT NULL COMMENT '主键ID',
  nick_name VARCHAR(32) NULL DEFAULT NULL COMMENT '昵称',
  user_name VARCHAR(32) NOT NULL COMMENT '用户名',
  pass_word VARCHAR(64) NOT NULL COMMENT '密码',
  salt VARCHAR(50) NOT NULL COMMENT '加密盐',
  phone_number VARCHAR(11) DEFAULT NULL COMMENT '手机号',
  email VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
  status TINYINT(1) NOT NULL DEFAULT '0' COMMENT '0是启用,1是禁用',
  create_time TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY(id),
  UNIQUE KEY username(user_name)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT='用户';

INSERT INTO sys_user(id, nick_name, user_name, pass_word, salt, create_time, update_time) VALUES('1319171516998582274', 'test', 'test', 'dd3927221f5f01f0fb338c5c714cebfc', 'pezYZ35YUyCU5OVr64YF4g==', current_timestamp(), current_timestamp());

DROP TABLE IF EXISTS sys_role;

CREATE TABLE sys_role
(
  id VARCHAR(32) NOT NULL COMMENT '主键ID',
  name VARCHAR(50) NOT NULL COMMENT '角色名称',
  remark VARCHAR(100) DEFAULT NULL COMMENT '备注',
  status TINYINT(1) NOT NULL DEFAULT '0' COMMENT '0是启用,1是禁用',
  create_time TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY(id),
  UNIQUE KEY name(name)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT '角色';


DROP TABLE IF EXISTS sys_user_role;

CREATE TABLE sys_user_role
(
  id VARCHAR(32) NOT NULL COMMENT '主键ID',
  user_id VARCHAR(32) NOT NULL COMMENT '用户ID',
  role_id VARCHAR(32) NOT NULL COMMENT '角色ID',
  status TINYINT(1) NOT NULL DEFAULT '0' COMMENT '0是启用,1是禁用',
  create_time TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT '用户与角色关系表';


DROP TABLE IF EXISTS sys_permission;

CREATE TABLE sys_permission
(
  id VARCHAR(32) NOT NULL COMMENT '主键ID',
  menu_id VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '所属菜单ID',
  name VARCHAR(50) NOT NULL COMMENT '权限名称',
  remark VARCHAR(100) DEFAULT NULL COMMENT '备注',
  sort smallint(5) DEFAULT 100 COMMENT '排序',
  perm_code varchar(50) NOT NULL COMMENT '权限编码',
  status TINYINT(1) NOT NULL DEFAULT '0' COMMENT '0是启用,1是禁用',
  create_time TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY(id),
  UNIQUE KEY perm_code(perm_code)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT '权限表';


DROP TABLE IF EXISTS sys_role_permission;

CREATE TABLE sys_role_permission
(
  id VARCHAR(32) NOT NULL COMMENT '主键ID',
  role_id VARCHAR(32) NOT NULL COMMENT '角色ID',
  permission_id VARCHAR(32) NOT NULL COMMENT '权限ID',
  status TINYINT(1) NOT NULL DEFAULT '0' COMMENT '0是启用,1是禁用',
  create_time TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT '角色与权限关系表';

DROP TABLE IF EXISTS sys_menu;

CREATE TABLE sys_menu
(
  id VARCHAR(32) NOT NULL COMMENT '主键ID',
  parent_id VARCHAR(32) NOT NULL DEFAULT 0 COMMENT '父级ID',
  name VARCHAR(50) NOT NULL COMMENT '菜单名称',
  remark VARCHAR(100) DEFAULT NULL COMMENT '备注',
  url VARCHAR(100) DEFAULT NULL COMMENT '链接地址',
  sort smallint(5) DEFAULT 100 COMMENT '排序',
  status TINYINT(1) NOT NULL DEFAULT '0' COMMENT '0是启用,1是禁用',
  create_time TIMESTAMP COMMENT '创建时间',
  update_time TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT '菜单表';