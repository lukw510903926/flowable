set
foreign_key_checks=0;

-- ----------------------------
-- table structure for act_evt_log
-- ----------------------------
drop table if exists `act_evt_log`;
create table `act_evt_log`
(
    `log_nr_`       bigint(20) not null auto_increment,
    `type_`         varchar(64) collate utf8_bin  default null,
    `proc_def_id_`  varchar(64) collate utf8_bin  default null,
    `proc_inst_id_` varchar(64) collate utf8_bin  default null,
    `execution_id_` varchar(64) collate utf8_bin  default null,
    `task_id_`      varchar(64) collate utf8_bin  default null,
    `time_stamp_`   timestamp(3) not null         default current_timestamp(3) on update current_timestamp (3),
    `user_id_`      varchar(255) collate utf8_bin default null,
    `data_`         longblob,
    `lock_owner_`   varchar(255) collate utf8_bin default null,
    `lock_time_`    timestamp(3) null default null,
    `is_processed_` tinyint(4) default '0',
    primary key (`log_nr_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_evt_log
-- ----------------------------

-- ----------------------------
-- table structure for act_ge_bytearray
-- ----------------------------
drop table if exists `act_ge_bytearray`;
create table `act_ge_bytearray`
(
    `id_`            varchar(64) collate utf8_bin not null,
    `rev_`           int(11) default null,
    `name_`          varchar(255) collate utf8_bin default null,
    `deployment_id_` varchar(64) collate utf8_bin  default null,
    `bytes_`         longblob,
    `generated_`     tinyint(4) default null,
    primary key (`id_`),
    key              `act_fk_bytearr_depl` (`deployment_id_`),
    constraint `act_fk_bytearr_depl` foreign key (`deployment_id_`) references `act_re_deployment` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;


-- ----------------------------
-- table structure for act_ge_property
-- ----------------------------
drop table if exists `act_ge_property`;
create table `act_ge_property`
(
    `name_`  varchar(64) collate utf8_bin not null,
    `value_` varchar(300) collate utf8_bin default null,
    `rev_`   int(11) default null,
    primary key (`name_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- table structure for act_hi_actinst
-- ----------------------------
drop table if exists `act_hi_actinst`;
create table `act_hi_actinst`
(
    `id_`                varchar(64) collate utf8_bin  not null,
    `rev_`               int(11) default '1',
    `proc_def_id_`       varchar(64) collate utf8_bin  not null,
    `proc_inst_id_`      varchar(64) collate utf8_bin  not null,
    `execution_id_`      varchar(64) collate utf8_bin  not null,
    `act_id_`            varchar(255) collate utf8_bin not null,
    `task_id_`           varchar(64) collate utf8_bin   default null,
    `call_proc_inst_id_` varchar(64) collate utf8_bin   default null,
    `act_name_`          varchar(255) collate utf8_bin  default null,
    `act_type_`          varchar(255) collate utf8_bin not null,
    `assignee_`          varchar(255) collate utf8_bin  default null,
    `start_time_`        datetime(3) not null,
    `end_time_`          datetime(3) default null,
    `duration_`          bigint(20) default null,
    `delete_reason_`     varchar(4000) collate utf8_bin default null,
    `tenant_id_`         varchar(255) collate utf8_bin  default '',
    primary key (`id_`),
    key                  `act_idx_hi_act_inst_start` (`start_time_`),
    key                  `act_idx_hi_act_inst_end` (`end_time_`),
    key                  `act_idx_hi_act_inst_procinst` (`proc_inst_id_`,`act_id_`),
    key                  `act_idx_hi_act_inst_exec` (`execution_id_`,`act_id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- table structure for act_hi_attachment
-- ----------------------------
drop table if exists `act_hi_attachment`;
create table `act_hi_attachment`
(
    `id_`           varchar(64) collate utf8_bin not null,
    `rev_`          int(11) default null,
    `user_id_`      varchar(255) collate utf8_bin  default null,
    `name_`         varchar(255) collate utf8_bin  default null,
    `description_`  varchar(4000) collate utf8_bin default null,
    `type_`         varchar(255) collate utf8_bin  default null,
    `task_id_`      varchar(64) collate utf8_bin   default null,
    `proc_inst_id_` varchar(64) collate utf8_bin   default null,
    `url_`          varchar(4000) collate utf8_bin default null,
    `content_id_`   varchar(64) collate utf8_bin   default null,
    `time_`         datetime(3) default null,
    primary key (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_hi_attachment
-- ----------------------------

-- ----------------------------
-- table structure for act_hi_comment
-- ----------------------------
drop table if exists `act_hi_comment`;
create table `act_hi_comment`
(
    `id_`           varchar(64) collate utf8_bin not null,
    `type_`         varchar(255) collate utf8_bin  default null,
    `time_`         datetime(3) not null,
    `user_id_`      varchar(255) collate utf8_bin  default null,
    `task_id_`      varchar(64) collate utf8_bin   default null,
    `proc_inst_id_` varchar(64) collate utf8_bin   default null,
    `action_`       varchar(255) collate utf8_bin  default null,
    `message_`      varchar(4000) collate utf8_bin default null,
    `full_msg_`     longblob,
    primary key (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- table structure for act_hi_detail
-- ----------------------------
drop table if exists `act_hi_detail`;
create table `act_hi_detail`
(
    `id_`           varchar(64) collate utf8_bin  not null,
    `type_`         varchar(255) collate utf8_bin not null,
    `proc_inst_id_` varchar(64) collate utf8_bin   default null,
    `execution_id_` varchar(64) collate utf8_bin   default null,
    `task_id_`      varchar(64) collate utf8_bin   default null,
    `act_inst_id_`  varchar(64) collate utf8_bin   default null,
    `name_`         varchar(255) collate utf8_bin not null,
    `var_type_`     varchar(255) collate utf8_bin  default null,
    `rev_`          int(11) default null,
    `time_`         datetime(3) not null,
    `bytearray_id_` varchar(64) collate utf8_bin   default null,
    `double_`       double                         default null,
    `long_`         bigint(20) default null,
    `text_`         varchar(4000) collate utf8_bin default null,
    `text2_`        varchar(4000) collate utf8_bin default null,
    primary key (`id_`),
    key             `act_idx_hi_detail_proc_inst` (`proc_inst_id_`),
    key             `act_idx_hi_detail_act_inst` (`act_inst_id_`),
    key             `act_idx_hi_detail_time` (`time_`),
    key             `act_idx_hi_detail_name` (`name_`),
    key             `act_idx_hi_detail_task_id` (`task_id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_hi_detail
-- ----------------------------

-- ----------------------------
-- table structure for act_hi_identitylink
-- ----------------------------
drop table if exists `act_hi_identitylink`;
create table `act_hi_identitylink`
(
    `id_`                  varchar(64) collate utf8_bin not null,
    `group_id_`            varchar(255) collate utf8_bin default null,
    `type_`                varchar(255) collate utf8_bin default null,
    `user_id_`             varchar(255) collate utf8_bin default null,
    `task_id_`             varchar(64) collate utf8_bin  default null,
    `create_time_`         datetime(3) default null,
    `proc_inst_id_`        varchar(64) collate utf8_bin  default null,
    `scope_id_`            varchar(255) collate utf8_bin default null,
    `scope_type_`          varchar(255) collate utf8_bin default null,
    `scope_definition_id_` varchar(255) collate utf8_bin default null,
    primary key (`id_`),
    key                    `act_idx_hi_ident_lnk_user` (`user_id_`),
    key                    `act_idx_hi_ident_lnk_scope` (`scope_id_`,`scope_type_`),
    key                    `act_idx_hi_ident_lnk_scope_def` (`scope_definition_id_`,`scope_type_`),
    key                    `act_idx_hi_ident_lnk_task` (`task_id_`),
    key                    `act_idx_hi_ident_lnk_procinst` (`proc_inst_id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- table structure for act_hi_procinst
-- ----------------------------
drop table if exists `act_hi_procinst`;
create table `act_hi_procinst`
(
    `id_`                        varchar(64) collate utf8_bin not null,
    `rev_`                       int(11) default '1',
    `proc_inst_id_`              varchar(64) collate utf8_bin not null,
    `business_key_`              varchar(255) collate utf8_bin  default null,
    `proc_def_id_`               varchar(64) collate utf8_bin not null,
    `start_time_`                datetime(3) not null,
    `end_time_`                  datetime(3) default null,
    `duration_`                  bigint(20) default null,
    `start_user_id_`             varchar(255) collate utf8_bin  default null,
    `start_act_id_`              varchar(255) collate utf8_bin  default null,
    `end_act_id_`                varchar(255) collate utf8_bin  default null,
    `super_process_instance_id_` varchar(64) collate utf8_bin   default null,
    `delete_reason_`             varchar(4000) collate utf8_bin default null,
    `tenant_id_`                 varchar(255) collate utf8_bin  default '',
    `name_`                      varchar(255) collate utf8_bin  default null,
    `callback_id_`               varchar(255) collate utf8_bin  default null,
    `callback_type_`             varchar(255) collate utf8_bin  default null,
    primary key (`id_`),
    unique key `proc_inst_id_` (`proc_inst_id_`),
    key                          `act_idx_hi_pro_inst_end` (`end_time_`),
    key                          `act_idx_hi_pro_i_buskey` (`business_key_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- table structure for act_hi_taskinst
-- ----------------------------
drop table if exists `act_hi_taskinst`;
create table `act_hi_taskinst`
(
    `id_`                  varchar(64) collate utf8_bin not null,
    `rev_`                 int(11) default '1',
    `proc_def_id_`         varchar(64) collate utf8_bin   default null,
    `task_def_id_`         varchar(64) collate utf8_bin   default null,
    `task_def_key_`        varchar(255) collate utf8_bin  default null,
    `proc_inst_id_`        varchar(64) collate utf8_bin   default null,
    `execution_id_`        varchar(64) collate utf8_bin   default null,
    `scope_id_`            varchar(255) collate utf8_bin  default null,
    `sub_scope_id_`        varchar(255) collate utf8_bin  default null,
    `scope_type_`          varchar(255) collate utf8_bin  default null,
    `scope_definition_id_` varchar(255) collate utf8_bin  default null,
    `name_`                varchar(255) collate utf8_bin  default null,
    `parent_task_id_`      varchar(64) collate utf8_bin   default null,
    `description_`         varchar(4000) collate utf8_bin default null,
    `owner_`               varchar(255) collate utf8_bin  default null,
    `assignee_`            varchar(255) collate utf8_bin  default null,
    `start_time_`          datetime(3) not null,
    `claim_time_`          datetime(3) default null,
    `end_time_`            datetime(3) default null,
    `duration_`            bigint(20) default null,
    `delete_reason_`       varchar(4000) collate utf8_bin default null,
    `priority_`            int(11) default null,
    `due_date_`            datetime(3) default null,
    `form_key_`            varchar(255) collate utf8_bin  default null,
    `category_`            varchar(255) collate utf8_bin  default null,
    `tenant_id_`           varchar(255) collate utf8_bin  default '',
    `last_updated_time_`   datetime(3) default null,
    primary key (`id_`),
    key                    `act_idx_hi_task_scope` (`scope_id_`,`scope_type_`),
    key                    `act_idx_hi_task_sub_scope` (`sub_scope_id_`,`scope_type_`),
    key                    `act_idx_hi_task_scope_def` (`scope_definition_id_`,`scope_type_`),
    key                    `act_idx_hi_task_inst_procinst` (`proc_inst_id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- table structure for act_hi_varinst
-- ----------------------------
drop table if exists `act_hi_varinst`;
create table `act_hi_varinst`
(
    `id_`                varchar(64) collate utf8_bin  not null,
    `rev_`               int(11) default '1',
    `proc_inst_id_`      varchar(64) collate utf8_bin   default null,
    `execution_id_`      varchar(64) collate utf8_bin   default null,
    `task_id_`           varchar(64) collate utf8_bin   default null,
    `name_`              varchar(255) collate utf8_bin not null,
    `var_type_`          varchar(100) collate utf8_bin  default null,
    `scope_id_`          varchar(255) collate utf8_bin  default null,
    `sub_scope_id_`      varchar(255) collate utf8_bin  default null,
    `scope_type_`        varchar(255) collate utf8_bin  default null,
    `bytearray_id_`      varchar(64) collate utf8_bin   default null,
    `double_`            double                         default null,
    `long_`              bigint(20) default null,
    `text_`              varchar(4000) collate utf8_bin default null,
    `text2_`             varchar(4000) collate utf8_bin default null,
    `create_time_`       datetime(3) default null,
    `last_updated_time_` datetime(3) default null,
    primary key (`id_`),
    key                  `act_idx_hi_procvar_name_type` (`name_`,`var_type_`),
    key                  `act_idx_hi_var_scope_id_type` (`scope_id_`,`scope_type_`),
    key                  `act_idx_hi_var_sub_id_type` (`sub_scope_id_`,`scope_type_`),
    key                  `act_idx_hi_procvar_proc_inst` (`proc_inst_id_`),
    key                  `act_idx_hi_procvar_task_id` (`task_id_`),
    key                  `act_idx_hi_procvar_exe` (`execution_id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- table structure for act_id_bytearray
-- ----------------------------
drop table if exists `act_id_bytearray`;
create table `act_id_bytearray`
(
    `id_`    varchar(64) collate utf8_bin not null,
    `rev_`   int(11) default null,
    `name_`  varchar(255) collate utf8_bin default null,
    `bytes_` longblob,
    primary key (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_id_bytearray
-- ----------------------------

-- ----------------------------
-- table structure for act_id_group
-- ----------------------------
drop table if exists `act_id_group`;
create table `act_id_group`
(
    `id_`   varchar(64) collate utf8_bin not null,
    `rev_`  int(11) default null,
    `name_` varchar(255) collate utf8_bin default null,
    `type_` varchar(255) collate utf8_bin default null,
    primary key (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_id_group
-- ----------------------------

-- ----------------------------
-- table structure for act_id_info
-- ----------------------------
drop table if exists `act_id_info`;
create table `act_id_info`
(
    `id_`        varchar(64) collate utf8_bin not null,
    `rev_`       int(11) default null,
    `user_id_`   varchar(64) collate utf8_bin  default null,
    `type_`      varchar(64) collate utf8_bin  default null,
    `key_`       varchar(255) collate utf8_bin default null,
    `value_`     varchar(255) collate utf8_bin default null,
    `password_`  longblob,
    `parent_id_` varchar(255) collate utf8_bin default null,
    primary key (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_id_info
-- ----------------------------

-- ----------------------------
-- table structure for act_id_membership
-- ----------------------------
drop table if exists `act_id_membership`;
create table `act_id_membership`
(
    `user_id_`  varchar(64) collate utf8_bin not null,
    `group_id_` varchar(64) collate utf8_bin not null,
    primary key (`user_id_`, `group_id_`),
    key         `act_fk_memb_group` (`group_id_`),
    constraint `act_fk_memb_group` foreign key (`group_id_`) references `act_id_group` (`id_`),
    constraint `act_fk_memb_user` foreign key (`user_id_`) references `act_id_user` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_id_membership
-- ----------------------------

-- ----------------------------
-- table structure for act_id_priv
-- ----------------------------
drop table if exists `act_id_priv`;
create table `act_id_priv`
(
    `id_`   varchar(64) collate utf8_bin  not null,
    `name_` varchar(255) collate utf8_bin not null,
    primary key (`id_`),
    unique key `act_uniq_priv_name` (`name_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_id_priv
-- ----------------------------

-- ----------------------------
-- table structure for act_id_priv_mapping
-- ----------------------------
drop table if exists `act_id_priv_mapping`;
create table `act_id_priv_mapping`
(
    `id_`       varchar(64) collate utf8_bin not null,
    `priv_id_`  varchar(64) collate utf8_bin not null,
    `user_id_`  varchar(255) collate utf8_bin default null,
    `group_id_` varchar(255) collate utf8_bin default null,
    primary key (`id_`),
    key         `act_fk_priv_mapping` (`priv_id_`),
    key         `act_idx_priv_user` (`user_id_`),
    key         `act_idx_priv_group` (`group_id_`),
    constraint `act_fk_priv_mapping` foreign key (`priv_id_`) references `act_id_priv` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_id_priv_mapping
-- ----------------------------

-- ----------------------------
-- table structure for act_id_property
-- ----------------------------
drop table if exists `act_id_property`;
create table `act_id_property`
(
    `name_`  varchar(64) collate utf8_bin not null,
    `value_` varchar(300) collate utf8_bin default null,
    `rev_`   int(11) default null,
    primary key (`name_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_id_property
-- ----------------------------
insert into `act_id_property`
values ('schema.version', '6.3.0.1', '1');

-- ----------------------------
-- table structure for act_id_token
-- ----------------------------
drop table if exists `act_id_token`;
create table `act_id_token`
(
    `id_`          varchar(64) collate utf8_bin not null,
    `rev_`         int(11) default null,
    `token_value_` varchar(255) collate utf8_bin         default null,
    `token_date_`  timestamp(3)                 not null default current_timestamp(3) on update current_timestamp (3),
    `ip_address_`  varchar(255) collate utf8_bin         default null,
    `user_agent_`  varchar(255) collate utf8_bin         default null,
    `user_id_`     varchar(255) collate utf8_bin         default null,
    `token_data_`  varchar(2000) collate utf8_bin        default null,
    primary key (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_id_token
-- ----------------------------

-- ----------------------------
-- table structure for act_id_user
-- ----------------------------
drop table if exists `act_id_user`;
create table `act_id_user`
(
    `id_`         varchar(64) collate utf8_bin not null,
    `rev_`        int(11) default null,
    `first_`      varchar(255) collate utf8_bin default null,
    `last_`       varchar(255) collate utf8_bin default null,
    `email_`      varchar(255) collate utf8_bin default null,
    `pwd_`        varchar(255) collate utf8_bin default null,
    `picture_id_` varchar(64) collate utf8_bin  default null,
    `tenant_id_`  varchar(255) collate utf8_bin default '',
    primary key (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_id_user
-- ----------------------------

-- ----------------------------
-- table structure for act_procdef_info
-- ----------------------------
drop table if exists `act_procdef_info`;
create table `act_procdef_info`
(
    `id_`           varchar(64) collate utf8_bin not null,
    `proc_def_id_`  varchar(64) collate utf8_bin not null,
    `rev_`          int(11) default null,
    `info_json_id_` varchar(64) collate utf8_bin default null,
    primary key (`id_`),
    unique key `act_uniq_info_procdef` (`proc_def_id_`),
    key             `act_idx_info_procdef` (`proc_def_id_`),
    key             `act_fk_info_json_ba` (`info_json_id_`),
    constraint `act_fk_info_json_ba` foreign key (`info_json_id_`) references `act_ge_bytearray` (`id_`),
    constraint `act_fk_info_procdef` foreign key (`proc_def_id_`) references `act_re_procdef` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_procdef_info
-- ----------------------------

-- ----------------------------
-- table structure for act_re_deployment
-- ----------------------------
drop table if exists `act_re_deployment`;
create table `act_re_deployment`
(
    `id_`                varchar(64) collate utf8_bin not null,
    `name_`              varchar(255) collate utf8_bin default null,
    `category_`          varchar(255) collate utf8_bin default null,
    `key_`               varchar(255) collate utf8_bin default null,
    `tenant_id_`         varchar(255) collate utf8_bin default '',
    `deploy_time_`       timestamp(3) null default null,
    `derived_from_`      varchar(64) collate utf8_bin  default null,
    `derived_from_root_` varchar(64) collate utf8_bin  default null,
    `engine_version_`    varchar(255) collate utf8_bin default null,
    primary key (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- table structure for act_re_model
-- ----------------------------
drop table if exists `act_re_model`;
create table `act_re_model`
(
    `id_`                           varchar(64) collate utf8_bin not null,
    `rev_`                          int(11) default null,
    `name_`                         varchar(255) collate utf8_bin  default null,
    `key_`                          varchar(255) collate utf8_bin  default null,
    `category_`                     varchar(255) collate utf8_bin  default null,
    `create_time_`                  timestamp(3) null default null,
    `last_update_time_`             timestamp(3) null default null,
    `version_`                      int(11) default null,
    `meta_info_`                    varchar(4000) collate utf8_bin default null,
    `deployment_id_`                varchar(64) collate utf8_bin   default null,
    `editor_source_value_id_`       varchar(64) collate utf8_bin   default null,
    `editor_source_extra_value_id_` varchar(64) collate utf8_bin   default null,
    `tenant_id_`                    varchar(255) collate utf8_bin  default '',
    primary key (`id_`),
    key                             `act_fk_model_source` (`editor_source_value_id_`),
    key                             `act_fk_model_source_extra` (`editor_source_extra_value_id_`),
    key                             `act_fk_model_deployment` (`deployment_id_`),
    constraint `act_fk_model_deployment` foreign key (`deployment_id_`) references `act_re_deployment` (`id_`),
    constraint `act_fk_model_source` foreign key (`editor_source_value_id_`) references `act_ge_bytearray` (`id_`),
    constraint `act_fk_model_source_extra` foreign key (`editor_source_extra_value_id_`) references `act_ge_bytearray` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_re_model
-- ----------------------------

-- ----------------------------
-- table structure for act_re_procdef
-- ----------------------------
drop table if exists `act_re_procdef`;
create table `act_re_procdef`
(
    `id_`                     varchar(64) collate utf8_bin  not null,
    `rev_`                    int(11) default null,
    `category_`               varchar(255) collate utf8_bin  default null,
    `name_`                   varchar(255) collate utf8_bin  default null,
    `key_`                    varchar(255) collate utf8_bin not null,
    `version_`                int(11) not null,
    `deployment_id_`          varchar(64) collate utf8_bin   default null,
    `resource_name_`          varchar(4000) collate utf8_bin default null,
    `dgrm_resource_name_`     varchar(4000) collate utf8_bin default null,
    `description_`            varchar(4000) collate utf8_bin default null,
    `has_start_form_key_`     tinyint(4) default null,
    `has_graphical_notation_` tinyint(4) default null,
    `suspension_state_`       int(11) default null,
    `tenant_id_`              varchar(255) collate utf8_bin  default '',
    `engine_version_`         varchar(255) collate utf8_bin  default null,
    `derived_from_`           varchar(64) collate utf8_bin   default null,
    `derived_from_root_`      varchar(64) collate utf8_bin   default null,
    `derived_version_`        int(11) not null default '0',
    primary key (`id_`),
    unique key `act_uniq_procdef` (`key_`,`version_`,`derived_version_`,`tenant_id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_re_procdef
-- ----------------------------
insert into `act_re_procdef`
values ('eventmanagement:1:4', '2', null, '事件管理', 'eventmanagement', '1', '1', 'eventmanagement.bpmn20.xml',
        'eventmanagement.eventmanagement.png', null, '0', '1', '1', '', null, null, null, '0');
insert into `act_re_procdef`
values ('eventmanagement:2:2504', '2', null, '事件管理', 'eventmanagement', '2', '2501', 'eventmanagement.bpmn20.xml',
        'eventmanagement.eventmanagement.png', null, '0', '1', '1', '', null, null, null, '0');
insert into `act_re_procdef`
values ('eventmanagement:3:2508', '2', null, '事件管理', 'eventmanagement', '3', '2505', 'eventmanagement.bpmn20.xml',
        'eventmanagement.eventmanagement.png', null, '0', '1', '1', '', null, null, null, '0');

-- ----------------------------
-- table structure for act_ru_deadletter_job
-- ----------------------------
drop table if exists `act_ru_deadletter_job`;
create table `act_ru_deadletter_job`
(
    `id_`                  varchar(64) collate utf8_bin  not null,
    `rev_`                 int(11) default null,
    `type_`                varchar(255) collate utf8_bin not null,
    `exclusive_`           tinyint(1) default null,
    `execution_id_`        varchar(64) collate utf8_bin   default null,
    `process_instance_id_` varchar(64) collate utf8_bin   default null,
    `proc_def_id_`         varchar(64) collate utf8_bin   default null,
    `scope_id_`            varchar(255) collate utf8_bin  default null,
    `sub_scope_id_`        varchar(255) collate utf8_bin  default null,
    `scope_type_`          varchar(255) collate utf8_bin  default null,
    `scope_definition_id_` varchar(255) collate utf8_bin  default null,
    `exception_stack_id_`  varchar(64) collate utf8_bin   default null,
    `exception_msg_`       varchar(4000) collate utf8_bin default null,
    `duedate_`             timestamp(3) null default null,
    `repeat_`              varchar(255) collate utf8_bin  default null,
    `handler_type_`        varchar(255) collate utf8_bin  default null,
    `handler_cfg_`         varchar(4000) collate utf8_bin default null,
    `custom_values_id_`    varchar(64) collate utf8_bin   default null,
    `create_time_`         timestamp(3) null default null,
    `tenant_id_`           varchar(255) collate utf8_bin  default '',
    primary key (`id_`),
    key                    `act_idx_deadletter_job_exception_stack_id` (`exception_stack_id_`),
    key                    `act_idx_deadletter_job_custom_values_id` (`custom_values_id_`),
    key                    `act_idx_djob_scope` (`scope_id_`,`scope_type_`),
    key                    `act_idx_djob_sub_scope` (`sub_scope_id_`,`scope_type_`),
    key                    `act_idx_djob_scope_def` (`scope_definition_id_`,`scope_type_`),
    key                    `act_fk_deadletter_job_execution` (`execution_id_`),
    key                    `act_fk_deadletter_job_process_instance` (`process_instance_id_`),
    key                    `act_fk_deadletter_job_proc_def` (`proc_def_id_`),
    constraint `act_fk_deadletter_job_custom_values` foreign key (`custom_values_id_`) references `act_ge_bytearray` (`id_`),
    constraint `act_fk_deadletter_job_exception` foreign key (`exception_stack_id_`) references `act_ge_bytearray` (`id_`),
    constraint `act_fk_deadletter_job_execution` foreign key (`execution_id_`) references `act_ru_execution` (`id_`),
    constraint `act_fk_deadletter_job_process_instance` foreign key (`process_instance_id_`) references `act_ru_execution` (`id_`),
    constraint `act_fk_deadletter_job_proc_def` foreign key (`proc_def_id_`) references `act_re_procdef` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_ru_deadletter_job
-- ----------------------------

-- ----------------------------
-- table structure for act_ru_event_subscr
-- ----------------------------
drop table if exists `act_ru_event_subscr`;
create table `act_ru_event_subscr`
(
    `id_`            varchar(64) collate utf8_bin  not null,
    `rev_`           int(11) default null,
    `event_type_`    varchar(255) collate utf8_bin not null,
    `event_name_`    varchar(255) collate utf8_bin          default null,
    `execution_id_`  varchar(64) collate utf8_bin           default null,
    `proc_inst_id_`  varchar(64) collate utf8_bin           default null,
    `activity_id_`   varchar(64) collate utf8_bin           default null,
    `configuration_` varchar(255) collate utf8_bin          default null,
    `created_`       timestamp(3)                  not null default current_timestamp(3),
    `proc_def_id_`   varchar(64) collate utf8_bin           default null,
    `tenant_id_`     varchar(255) collate utf8_bin          default '',
    primary key (`id_`),
    key              `act_idx_event_subscr_config_` (`configuration_`),
    key              `act_fk_event_exec` (`execution_id_`),
    constraint `act_fk_event_exec` foreign key (`execution_id_`) references `act_ru_execution` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_ru_event_subscr
-- ----------------------------

-- ----------------------------
-- table structure for act_ru_execution
-- ----------------------------
drop table if exists `act_ru_execution`;
create table `act_ru_execution`
(
    `id_`                   varchar(64) collate utf8_bin not null,
    `rev_`                  int(11) default null,
    `proc_inst_id_`         varchar(64) collate utf8_bin  default null,
    `business_key_`         varchar(255) collate utf8_bin default null,
    `parent_id_`            varchar(64) collate utf8_bin  default null,
    `proc_def_id_`          varchar(64) collate utf8_bin  default null,
    `super_exec_`           varchar(64) collate utf8_bin  default null,
    `root_proc_inst_id_`    varchar(64) collate utf8_bin  default null,
    `act_id_`               varchar(255) collate utf8_bin default null,
    `is_active_`            tinyint(4) default null,
    `is_concurrent_`        tinyint(4) default null,
    `is_scope_`             tinyint(4) default null,
    `is_event_scope_`       tinyint(4) default null,
    `is_mi_root_`           tinyint(4) default null,
    `suspension_state_`     int(11) default null,
    `cached_ent_state_`     int(11) default null,
    `tenant_id_`            varchar(255) collate utf8_bin default '',
    `name_`                 varchar(255) collate utf8_bin default null,
    `start_act_id_`         varchar(255) collate utf8_bin default null,
    `start_time_`           datetime(3) default null,
    `start_user_id_`        varchar(255) collate utf8_bin default null,
    `lock_time_`            timestamp(3) null default null,
    `is_count_enabled_`     tinyint(4) default null,
    `evt_subscr_count_`     int(11) default null,
    `task_count_`           int(11) default null,
    `job_count_`            int(11) default null,
    `timer_job_count_`      int(11) default null,
    `susp_job_count_`       int(11) default null,
    `deadletter_job_count_` int(11) default null,
    `var_count_`            int(11) default null,
    `id_link_count_`        int(11) default null,
    `callback_id_`          varchar(255) collate utf8_bin default null,
    `callback_type_`        varchar(255) collate utf8_bin default null,
    primary key (`id_`),
    key                     `act_idx_exec_buskey` (`business_key_`),
    key                     `act_idc_exec_root` (`root_proc_inst_id_`),
    key                     `act_fk_exe_procinst` (`proc_inst_id_`),
    key                     `act_fk_exe_parent` (`parent_id_`),
    key                     `act_fk_exe_super` (`super_exec_`),
    key                     `act_fk_exe_procdef` (`proc_def_id_`),
    constraint `act_fk_exe_parent` foreign key (`parent_id_`) references `act_ru_execution` (`id_`) on delete cascade,
    constraint `act_fk_exe_procdef` foreign key (`proc_def_id_`) references `act_re_procdef` (`id_`),
    constraint `act_fk_exe_procinst` foreign key (`proc_inst_id_`) references `act_ru_execution` (`id_`) on delete cascade on update cascade,
    constraint `act_fk_exe_super` foreign key (`super_exec_`) references `act_ru_execution` (`id_`) on delete cascade
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- table structure for act_ru_history_job
-- ----------------------------
drop table if exists `act_ru_history_job`;
create table `act_ru_history_job`
(
    `id_`                 varchar(64) collate utf8_bin not null,
    `rev_`                int(11) default null,
    `lock_exp_time_`      timestamp(3) null default null,
    `lock_owner_`         varchar(255) collate utf8_bin  default null,
    `retries_`            int(11) default null,
    `exception_stack_id_` varchar(64) collate utf8_bin   default null,
    `exception_msg_`      varchar(4000) collate utf8_bin default null,
    `handler_type_`       varchar(255) collate utf8_bin  default null,
    `handler_cfg_`        varchar(4000) collate utf8_bin default null,
    `custom_values_id_`   varchar(64) collate utf8_bin   default null,
    `adv_handler_cfg_id_` varchar(64) collate utf8_bin   default null,
    `create_time_`        timestamp(3) null default null,
    `tenant_id_`          varchar(255) collate utf8_bin  default '',
    primary key (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_ru_history_job
-- ----------------------------

-- ----------------------------
-- table structure for act_ru_identitylink
-- ----------------------------
drop table if exists `act_ru_identitylink`;
create table `act_ru_identitylink`
(
    `id_`                  varchar(64) collate utf8_bin not null,
    `rev_`                 int(11) default null,
    `group_id_`            varchar(255) collate utf8_bin default null,
    `type_`                varchar(255) collate utf8_bin default null,
    `user_id_`             varchar(255) collate utf8_bin default null,
    `task_id_`             varchar(64) collate utf8_bin  default null,
    `proc_inst_id_`        varchar(64) collate utf8_bin  default null,
    `proc_def_id_`         varchar(64) collate utf8_bin  default null,
    `scope_id_`            varchar(255) collate utf8_bin default null,
    `scope_type_`          varchar(255) collate utf8_bin default null,
    `scope_definition_id_` varchar(255) collate utf8_bin default null,
    primary key (`id_`),
    key                    `act_idx_ident_lnk_user` (`user_id_`),
    key                    `act_idx_ident_lnk_group` (`group_id_`),
    key                    `act_idx_ident_lnk_scope` (`scope_id_`,`scope_type_`),
    key                    `act_idx_ident_lnk_scope_def` (`scope_definition_id_`,`scope_type_`),
    key                    `act_idx_athrz_procedef` (`proc_def_id_`),
    key                    `act_fk_tskass_task` (`task_id_`),
    key                    `act_fk_idl_procinst` (`proc_inst_id_`),
    constraint `act_fk_athrz_procedef` foreign key (`proc_def_id_`) references `act_re_procdef` (`id_`),
    constraint `act_fk_idl_procinst` foreign key (`proc_inst_id_`) references `act_ru_execution` (`id_`),
    constraint `act_fk_tskass_task` foreign key (`task_id_`) references `act_ru_task` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- table structure for act_ru_job
-- ----------------------------
drop table if exists `act_ru_job`;
create table `act_ru_job`
(
    `id_`                  varchar(64) collate utf8_bin  not null,
    `rev_`                 int(11) default null,
    `type_`                varchar(255) collate utf8_bin not null,
    `lock_exp_time_`       timestamp(3) null default null,
    `lock_owner_`          varchar(255) collate utf8_bin  default null,
    `exclusive_`           tinyint(1) default null,
    `execution_id_`        varchar(64) collate utf8_bin   default null,
    `process_instance_id_` varchar(64) collate utf8_bin   default null,
    `proc_def_id_`         varchar(64) collate utf8_bin   default null,
    `scope_id_`            varchar(255) collate utf8_bin  default null,
    `sub_scope_id_`        varchar(255) collate utf8_bin  default null,
    `scope_type_`          varchar(255) collate utf8_bin  default null,
    `scope_definition_id_` varchar(255) collate utf8_bin  default null,
    `retries_`             int(11) default null,
    `exception_stack_id_`  varchar(64) collate utf8_bin   default null,
    `exception_msg_`       varchar(4000) collate utf8_bin default null,
    `duedate_`             timestamp(3) null default null,
    `repeat_`              varchar(255) collate utf8_bin  default null,
    `handler_type_`        varchar(255) collate utf8_bin  default null,
    `handler_cfg_`         varchar(4000) collate utf8_bin default null,
    `custom_values_id_`    varchar(64) collate utf8_bin   default null,
    `create_time_`         timestamp(3) null default null,
    `tenant_id_`           varchar(255) collate utf8_bin  default '',
    primary key (`id_`),
    key                    `act_idx_job_exception_stack_id` (`exception_stack_id_`),
    key                    `act_idx_job_custom_values_id` (`custom_values_id_`),
    key                    `act_idx_job_scope` (`scope_id_`,`scope_type_`),
    key                    `act_idx_job_sub_scope` (`sub_scope_id_`,`scope_type_`),
    key                    `act_idx_job_scope_def` (`scope_definition_id_`,`scope_type_`),
    key                    `act_fk_job_execution` (`execution_id_`),
    key                    `act_fk_job_process_instance` (`process_instance_id_`),
    key                    `act_fk_job_proc_def` (`proc_def_id_`),
    constraint `act_fk_job_custom_values` foreign key (`custom_values_id_`) references `act_ge_bytearray` (`id_`),
    constraint `act_fk_job_exception` foreign key (`exception_stack_id_`) references `act_ge_bytearray` (`id_`),
    constraint `act_fk_job_execution` foreign key (`execution_id_`) references `act_ru_execution` (`id_`),
    constraint `act_fk_job_process_instance` foreign key (`process_instance_id_`) references `act_ru_execution` (`id_`),
    constraint `act_fk_job_proc_def` foreign key (`proc_def_id_`) references `act_re_procdef` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_ru_job
-- ----------------------------

-- ----------------------------
-- table structure for act_ru_suspended_job
-- ----------------------------
drop table if exists `act_ru_suspended_job`;
create table `act_ru_suspended_job`
(
    `id_`                  varchar(64) collate utf8_bin  not null,
    `rev_`                 int(11) default null,
    `type_`                varchar(255) collate utf8_bin not null,
    `exclusive_`           tinyint(1) default null,
    `execution_id_`        varchar(64) collate utf8_bin   default null,
    `process_instance_id_` varchar(64) collate utf8_bin   default null,
    `proc_def_id_`         varchar(64) collate utf8_bin   default null,
    `scope_id_`            varchar(255) collate utf8_bin  default null,
    `sub_scope_id_`        varchar(255) collate utf8_bin  default null,
    `scope_type_`          varchar(255) collate utf8_bin  default null,
    `scope_definition_id_` varchar(255) collate utf8_bin  default null,
    `retries_`             int(11) default null,
    `exception_stack_id_`  varchar(64) collate utf8_bin   default null,
    `exception_msg_`       varchar(4000) collate utf8_bin default null,
    `duedate_`             timestamp(3) null default null,
    `repeat_`              varchar(255) collate utf8_bin  default null,
    `handler_type_`        varchar(255) collate utf8_bin  default null,
    `handler_cfg_`         varchar(4000) collate utf8_bin default null,
    `custom_values_id_`    varchar(64) collate utf8_bin   default null,
    `create_time_`         timestamp(3) null default null,
    `tenant_id_`           varchar(255) collate utf8_bin  default '',
    primary key (`id_`),
    key                    `act_idx_suspended_job_exception_stack_id` (`exception_stack_id_`),
    key                    `act_idx_suspended_job_custom_values_id` (`custom_values_id_`),
    key                    `act_idx_sjob_scope` (`scope_id_`,`scope_type_`),
    key                    `act_idx_sjob_sub_scope` (`sub_scope_id_`,`scope_type_`),
    key                    `act_idx_sjob_scope_def` (`scope_definition_id_`,`scope_type_`),
    key                    `act_fk_suspended_job_execution` (`execution_id_`),
    key                    `act_fk_suspended_job_process_instance` (`process_instance_id_`),
    key                    `act_fk_suspended_job_proc_def` (`proc_def_id_`),
    constraint `act_fk_suspended_job_custom_values` foreign key (`custom_values_id_`) references `act_ge_bytearray` (`id_`),
    constraint `act_fk_suspended_job_exception` foreign key (`exception_stack_id_`) references `act_ge_bytearray` (`id_`),
    constraint `act_fk_suspended_job_execution` foreign key (`execution_id_`) references `act_ru_execution` (`id_`),
    constraint `act_fk_suspended_job_process_instance` foreign key (`process_instance_id_`) references `act_ru_execution` (`id_`),
    constraint `act_fk_suspended_job_proc_def` foreign key (`proc_def_id_`) references `act_re_procdef` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_ru_suspended_job
-- ----------------------------

-- ----------------------------
-- table structure for act_ru_task
-- ----------------------------
drop table if exists `act_ru_task`;
create table `act_ru_task`
(
    `id_`                  varchar(64) collate utf8_bin not null,
    `rev_`                 int(11) default null,
    `execution_id_`        varchar(64) collate utf8_bin   default null,
    `proc_inst_id_`        varchar(64) collate utf8_bin   default null,
    `proc_def_id_`         varchar(64) collate utf8_bin   default null,
    `task_def_id_`         varchar(64) collate utf8_bin   default null,
    `scope_id_`            varchar(255) collate utf8_bin  default null,
    `sub_scope_id_`        varchar(255) collate utf8_bin  default null,
    `scope_type_`          varchar(255) collate utf8_bin  default null,
    `scope_definition_id_` varchar(255) collate utf8_bin  default null,
    `name_`                varchar(255) collate utf8_bin  default null,
    `parent_task_id_`      varchar(64) collate utf8_bin   default null,
    `description_`         varchar(4000) collate utf8_bin default null,
    `task_def_key_`        varchar(255) collate utf8_bin  default null,
    `owner_`               varchar(255) collate utf8_bin  default null,
    `assignee_`            varchar(255) collate utf8_bin  default null,
    `delegation_`          varchar(64) collate utf8_bin   default null,
    `priority_`            int(11) default null,
    `create_time_`         timestamp(3) null default null,
    `due_date_`            datetime(3) default null,
    `category_`            varchar(255) collate utf8_bin  default null,
    `suspension_state_`    int(11) default null,
    `tenant_id_`           varchar(255) collate utf8_bin  default '',
    `form_key_`            varchar(255) collate utf8_bin  default null,
    `claim_time_`          datetime(3) default null,
    `is_count_enabled_`    tinyint(4) default null,
    `var_count_`           int(11) default null,
    `id_link_count_`       int(11) default null,
    `sub_task_count_`      int(11) default null,
    primary key (`id_`),
    key                    `act_idx_task_create` (`create_time_`),
    key                    `act_idx_task_scope` (`scope_id_`,`scope_type_`),
    key                    `act_idx_task_sub_scope` (`sub_scope_id_`,`scope_type_`),
    key                    `act_idx_task_scope_def` (`scope_definition_id_`,`scope_type_`),
    key                    `act_fk_task_exe` (`execution_id_`),
    key                    `act_fk_task_procinst` (`proc_inst_id_`),
    key                    `act_fk_task_procdef` (`proc_def_id_`),
    constraint `act_fk_task_exe` foreign key (`execution_id_`) references `act_ru_execution` (`id_`),
    constraint `act_fk_task_procdef` foreign key (`proc_def_id_`) references `act_re_procdef` (`id_`),
    constraint `act_fk_task_procinst` foreign key (`proc_inst_id_`) references `act_ru_execution` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_ru_task
-- ----------------------------
insert into `act_ru_task`
values ('20003', '2', '7510', '7501', 'eventmanagement:3:2508', null, null, null, null, null, '服务台处理', null, null,
        'servicehandle', null, 'admin', null, '50', '2019-02-22 05:35:53.051', null, null, '1', '', null,
        '2019-02-22 05:35:53.777', '1', '0', '0', '0');
insert into `act_ru_task`
values ('22513', '1', '22510', '22501', 'eventmanagement:3:2508', null, null, null, null, null, '服务台处理', null, null,
        'servicehandle', null, null, null, '50', '2019-02-22 08:37:41.290', null, null, '1', '', null, null, '1', '0',
        '0', '0');
insert into `act_ru_task`
values ('22526', '2', '22523', '22514', 'eventmanagement:3:2508', null, null, null, null, null, '服务台处理', null, null,
        'servicehandle', null, 'admin', null, '50', '2019-02-22 08:51:50.826', null, null, '1', '', null,
        '2019-02-22 08:52:00.058', '1', '0', '0', '0');
insert into `act_ru_task`
values ('22542', '1', '22539', '22530', 'eventmanagement:3:2508', null, null, null, null, null, '服务台处理', null, null,
        'servicehandle', null, null, null, '50', '2019-02-22 08:53:49.194', null, null, '1', '', null, null, '1', '0',
        '0', '0');
insert into `act_ru_task`
values ('22555', '1', '22552', '22543', 'eventmanagement:3:2508', null, null, null, null, null, '服务台处理', null, null,
        'servicehandle', null, null, null, '50', '2019-02-22 09:13:15.756', null, null, '1', '', null, null, '1', '0',
        '0', '0');
insert into `act_ru_task`
values ('5013', '1', '5010', '5001', 'eventmanagement:3:2508', null, null, null, null, null, '服务台处理', null, null,
        'servicehandle', null, null, null, '50', '2019-02-18 03:21:55.791', null, null, '1', '', null, null, '1', '0',
        '0', '0');
insert into `act_ru_task`
values ('5026', '1', '5023', '5014', 'eventmanagement:3:2508', null, null, null, null, null, '服务台处理', null, null,
        'servicehandle', null, null, null, '50', '2019-02-18 03:24:27.588', null, null, '1', '', null, null, '1', '0',
        '0', '0');
insert into `act_ru_task`
values ('5039', '1', '5036', '5027', 'eventmanagement:3:2508', null, null, null, null, null, '服务台处理', null, null,
        'servicehandle', null, null, null, '50', '2019-02-18 03:24:52.095', null, null, '1', '', null, null, '1', '0',
        '0', '0');
insert into `act_ru_task`
values ('5052', '1', '5049', '5040', 'eventmanagement:3:2508', null, null, null, null, null, '服务台处理', null, null,
        'servicehandle', null, null, null, '50', '2019-02-18 03:25:35.627', null, null, '1', '', null, null, '1', '0',
        '0', '0');
insert into `act_ru_task`
values ('5075', '2', '5062', '5053', 'eventmanagement:3:2508', null, null, null, null, null, '服务台处理', null, null,
        'servicehandle', null, 'admin', null, '50', '2019-02-18 03:26:52.004', null, null, '1', '', null,
        '2019-02-18 03:26:52.047', '1', '0', '0', '0');
insert into `act_ru_task`
values ('5091', '1', '5088', '5079', 'eventmanagement:3:2508', null, null, null, null, null, '服务台处理', null, null,
        'servicehandle', null, null, null, '50', '2019-02-18 03:32:48.060', null, null, '1', '', null, null, '1', '0',
        '0', '0');

-- ----------------------------
-- table structure for act_ru_timer_job
-- ----------------------------
drop table if exists `act_ru_timer_job`;
create table `act_ru_timer_job`
(
    `id_`                  varchar(64) collate utf8_bin  not null,
    `rev_`                 int(11) default null,
    `type_`                varchar(255) collate utf8_bin not null,
    `lock_exp_time_`       timestamp(3) null default null,
    `lock_owner_`          varchar(255) collate utf8_bin  default null,
    `exclusive_`           tinyint(1) default null,
    `execution_id_`        varchar(64) collate utf8_bin   default null,
    `process_instance_id_` varchar(64) collate utf8_bin   default null,
    `proc_def_id_`         varchar(64) collate utf8_bin   default null,
    `scope_id_`            varchar(255) collate utf8_bin  default null,
    `sub_scope_id_`        varchar(255) collate utf8_bin  default null,
    `scope_type_`          varchar(255) collate utf8_bin  default null,
    `scope_definition_id_` varchar(255) collate utf8_bin  default null,
    `retries_`             int(11) default null,
    `exception_stack_id_`  varchar(64) collate utf8_bin   default null,
    `exception_msg_`       varchar(4000) collate utf8_bin default null,
    `duedate_`             timestamp(3) null default null,
    `repeat_`              varchar(255) collate utf8_bin  default null,
    `handler_type_`        varchar(255) collate utf8_bin  default null,
    `handler_cfg_`         varchar(4000) collate utf8_bin default null,
    `custom_values_id_`    varchar(64) collate utf8_bin   default null,
    `create_time_`         timestamp(3) null default null,
    `tenant_id_`           varchar(255) collate utf8_bin  default '',
    primary key (`id_`),
    key                    `act_idx_timer_job_exception_stack_id` (`exception_stack_id_`),
    key                    `act_idx_timer_job_custom_values_id` (`custom_values_id_`),
    key                    `act_idx_tjob_scope` (`scope_id_`,`scope_type_`),
    key                    `act_idx_tjob_sub_scope` (`sub_scope_id_`,`scope_type_`),
    key                    `act_idx_tjob_scope_def` (`scope_definition_id_`,`scope_type_`),
    key                    `act_fk_timer_job_execution` (`execution_id_`),
    key                    `act_fk_timer_job_process_instance` (`process_instance_id_`),
    key                    `act_fk_timer_job_proc_def` (`proc_def_id_`),
    constraint `act_fk_timer_job_custom_values` foreign key (`custom_values_id_`) references `act_ge_bytearray` (`id_`),
    constraint `act_fk_timer_job_exception` foreign key (`exception_stack_id_`) references `act_ge_bytearray` (`id_`),
    constraint `act_fk_timer_job_execution` foreign key (`execution_id_`) references `act_ru_execution` (`id_`),
    constraint `act_fk_timer_job_process_instance` foreign key (`process_instance_id_`) references `act_ru_execution` (`id_`),
    constraint `act_fk_timer_job_proc_def` foreign key (`proc_def_id_`) references `act_re_procdef` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_ru_timer_job
-- ----------------------------

-- ----------------------------
-- table structure for act_ru_variable
-- ----------------------------
drop table if exists `act_ru_variable`;
create table `act_ru_variable`
(
    `id_`           varchar(64) collate utf8_bin  not null,
    `rev_`          int(11) default null,
    `type_`         varchar(255) collate utf8_bin not null,
    `name_`         varchar(255) collate utf8_bin not null,
    `execution_id_` varchar(64) collate utf8_bin   default null,
    `proc_inst_id_` varchar(64) collate utf8_bin   default null,
    `task_id_`      varchar(64) collate utf8_bin   default null,
    `scope_id_`     varchar(255) collate utf8_bin  default null,
    `sub_scope_id_` varchar(255) collate utf8_bin  default null,
    `scope_type_`   varchar(255) collate utf8_bin  default null,
    `bytearray_id_` varchar(64) collate utf8_bin   default null,
    `double_`       double                         default null,
    `long_`         bigint(20) default null,
    `text_`         varchar(4000) collate utf8_bin default null,
    `text2_`        varchar(4000) collate utf8_bin default null,
    primary key (`id_`),
    key             `act_idx_ru_var_scope_id_type` (`scope_id_`,`scope_type_`),
    key             `act_idx_ru_var_sub_id_type` (`sub_scope_id_`,`scope_type_`),
    key             `act_fk_var_bytearray` (`bytearray_id_`),
    key             `act_idx_variable_task_id` (`task_id_`),
    key             `act_fk_var_exe` (`execution_id_`),
    key             `act_fk_var_procinst` (`proc_inst_id_`),
    constraint `act_fk_var_bytearray` foreign key (`bytearray_id_`) references `act_ge_bytearray` (`id_`),
    constraint `act_fk_var_exe` foreign key (`execution_id_`) references `act_ru_execution` (`id_`),
    constraint `act_fk_var_procinst` foreign key (`proc_inst_id_`) references `act_ru_execution` (`id_`)
) engine=innodb default charset=utf8mb4 collate=utf8_bin;

-- ----------------------------
-- records of act_ru_variable
-- ----------------------------
insert into `act_ru_variable`
values ('15001', '1', 'string', 'sys_current_pid', '7501', '7501', null, null, null, null, null, null, null, '7501',
        null);
insert into `act_ru_variable`
values ('15002', '1', 'string', 'sys_current_workid', '7501', '7501', null, null, null, null, null, null, null, '16',
        null);
insert into `act_ru_variable`
values ('15003', '1', 'string', 'sys_current_worknumber', '7501', '7501', null, null, null, null, null, null, null,
        'other190219-38955', null);
insert into `act_ru_variable`
values ('15004', '3', 'string', 'sys_current_taskid', '7501', '7501', null, null, null, null, null, null, null, '17503',
        null);
insert into `act_ru_variable`
values ('22502', '1', 'string', 'sys_biz_createuser', '22501', '22501', null, null, null, null, null, null, null,
        'admin', null);
insert into `act_ru_variable`
values ('22503', '1', 'null', 'sys_formtype', '22501', '22501', null, null, null, null, null, null, null, null, null);
insert into `act_ru_variable`
values ('22504', '1', 'string', 'sys_biz_id', '22501', '22501', null, null, null, null, null, null, null, '18', null);
insert into `act_ru_variable`
values ('22505', '1', 'string', 'handleuser', '22501', '22501', null, null, null, null, null, null, null, '', null);
insert into `act_ru_variable`
values ('22507', '1', 'serializable', '_members', '22501', '22501', null, null, null, null, '22506', null, null, null,
        null);
insert into `act_ru_variable`
values ('22509', '1', 'string', 'sys_button_value', '22501', '22501', null, null, null, null, null, null, null,
        'submit', null);
insert into `act_ru_variable`
values ('22515', '1', 'string', 'sys_biz_createuser', '22514', '22514', null, null, null, null, null, null, null,
        'admin', null);
insert into `act_ru_variable`
values ('22516', '1', 'null', 'sys_formtype', '22514', '22514', null, null, null, null, null, null, null, null, null);
insert into `act_ru_variable`
values ('22517', '1', 'string', 'sys_biz_id', '22514', '22514', null, null, null, null, null, null, null, '19', null);
insert into `act_ru_variable`
values ('22518', '1', 'string', 'handleuser', '22514', '22514', null, null, null, null, null, null, null, '', null);
insert into `act_ru_variable`
values ('22520', '1', 'serializable', '_members', '22514', '22514', null, null, null, null, '22519', null, null, null,
        null);
insert into `act_ru_variable`
values ('22522', '1', 'string', 'sys_button_value', '22514', '22514', null, null, null, null, null, null, null,
        'submit', null);
insert into `act_ru_variable`
values ('22531', '1', 'string', 'sys_biz_createuser', '22530', '22530', null, null, null, null, null, null, null,
        'admin', null);
insert into `act_ru_variable`
values ('22532', '1', 'null', 'sys_formtype', '22530', '22530', null, null, null, null, null, null, null, null, null);
insert into `act_ru_variable`
values ('22533', '1', 'string', 'sys_biz_id', '22530', '22530', null, null, null, null, null, null, null, '20', null);
insert into `act_ru_variable`
values ('22534', '1', 'string', 'handleuser', '22530', '22530', null, null, null, null, null, null, null, '', null);
insert into `act_ru_variable`
values ('22536', '1', 'serializable', '_members', '22530', '22530', null, null, null, null, '22535', null, null, null,
        null);
insert into `act_ru_variable`
values ('22538', '1', 'string', 'sys_button_value', '22530', '22530', null, null, null, null, null, null, null,
        'submit', null);
insert into `act_ru_variable`
values ('22544', '1', 'string', 'sys_biz_createuser', '22543', '22543', null, null, null, null, null, null, null,
        'admin', null);
insert into `act_ru_variable`
values ('22545', '1', 'null', 'sys_formtype', '22543', '22543', null, null, null, null, null, null, null, null, null);
insert into `act_ru_variable`
values ('22546', '1', 'string', 'sys_biz_id', '22543', '22543', null, null, null, null, null, null, null, '21', null);
insert into `act_ru_variable`
values ('22547', '1', 'string', 'handleuser', '22543', '22543', null, null, null, null, null, null, null, '', null);
insert into `act_ru_variable`
values ('22549', '1', 'serializable', '_members', '22543', '22543', null, null, null, null, '22548', null, null, null,
        null);
insert into `act_ru_variable`
values ('22551', '1', 'string', 'sys_button_value', '22543', '22543', null, null, null, null, null, null, null,
        'submit', null);
insert into `act_ru_variable`
values ('5002', '1', 'string', 'sys_biz_createuser', '5001', '5001', null, null, null, null, null, null, null, 'admin',
        null);
insert into `act_ru_variable`
values ('5003', '1', 'null', 'sys_formtype', '5001', '5001', null, null, null, null, null, null, null, null, null);
insert into `act_ru_variable`
values ('5004', '1', 'string', 'sys_biz_id', '5001', '5001', null, null, null, null, null, null, null, '10', null);
insert into `act_ru_variable`
values ('5005', '1', 'string', 'handleuser', '5001', '5001', null, null, null, null, null, null, null, '东单详情', null);
insert into `act_ru_variable`
values ('5007', '1', 'serializable', '_members', '5001', '5001', null, null, null, null, '5006', null, null, null,
        null);
insert into `act_ru_variable`
values ('5009', '1', 'string', 'sys_button_value', '5001', '5001', null, null, null, null, null, null, null, 'submit',
        null);
insert into `act_ru_variable`
values ('5015', '1', 'string', 'sys_biz_createuser', '5014', '5014', null, null, null, null, null, null, null, 'admin',
        null);
insert into `act_ru_variable`
values ('5016', '1', 'null', 'sys_formtype', '5014', '5014', null, null, null, null, null, null, null, null, null);
insert into `act_ru_variable`
values ('5017', '1', 'string', 'sys_biz_id', '5014', '5014', null, null, null, null, null, null, null, '11', null);
insert into `act_ru_variable`
values ('5018', '1', 'string', 'handleuser', '5014', '5014', null, null, null, null, null, null, null, '', null);
insert into `act_ru_variable`
values ('5020', '1', 'serializable', '_members', '5014', '5014', null, null, null, null, '5019', null, null, null,
        null);
insert into `act_ru_variable`
values ('5022', '1', 'string', 'sys_button_value', '5014', '5014', null, null, null, null, null, null, null, 'submit',
        null);
insert into `act_ru_variable`
values ('5028', '1', 'string', 'sys_biz_createuser', '5027', '5027', null, null, null, null, null, null, null, 'admin',
        null);
insert into `act_ru_variable`
values ('5029', '1', 'null', 'sys_formtype', '5027', '5027', null, null, null, null, null, null, null, null, null);
insert into `act_ru_variable`
values ('5030', '1', 'string', 'sys_biz_id', '5027', '5027', null, null, null, null, null, null, null, '12', null);
insert into `act_ru_variable`
values ('5031', '1', 'string', 'handleuser', '5027', '5027', null, null, null, null, null, null, null, '', null);
insert into `act_ru_variable`
values ('5033', '1', 'serializable', '_members', '5027', '5027', null, null, null, null, '5032', null, null, null,
        null);
insert into `act_ru_variable`
values ('5035', '1', 'string', 'sys_button_value', '5027', '5027', null, null, null, null, null, null, null, 'submit',
        null);
insert into `act_ru_variable`
values ('5041', '1', 'string', 'sys_biz_createuser', '5040', '5040', null, null, null, null, null, null, null, 'admin',
        null);
insert into `act_ru_variable`
values ('5042', '1', 'null', 'sys_formtype', '5040', '5040', null, null, null, null, null, null, null, null, null);
insert into `act_ru_variable`
values ('5043', '1', 'string', 'sys_biz_id', '5040', '5040', null, null, null, null, null, null, null, '13', null);
insert into `act_ru_variable`
values ('5044', '1', 'string', 'handleuser', '5040', '5040', null, null, null, null, null, null, null, '', null);
insert into `act_ru_variable`
values ('5046', '1', 'serializable', '_members', '5040', '5040', null, null, null, null, '5045', null, null, null,
        null);
insert into `act_ru_variable`
values ('5048', '1', 'string', 'sys_button_value', '5040', '5040', null, null, null, null, null, null, null, 'submit',
        null);
insert into `act_ru_variable`
values ('5054', '1', 'string', 'sys_biz_createuser', '5053', '5053', null, null, null, null, null, null, null, 'admin',
        null);
insert into `act_ru_variable`
values ('5055', '1', 'null', 'sys_formtype', '5053', '5053', null, null, null, null, null, null, null, null, null);
insert into `act_ru_variable`
values ('5056', '1', 'string', 'sys_biz_id', '5053', '5053', null, null, null, null, null, null, null, '14', null);
insert into `act_ru_variable`
values ('5057', '1', 'string', 'handleuser', '5053', '5053', null, null, null, null, null, null, null, '', null);
insert into `act_ru_variable`
values ('5059', '1', 'serializable', '_members', '5053', '5053', null, null, null, null, '5058', null, null, null,
        null);
insert into `act_ru_variable`
values ('5061', '2', 'string', 'sys_button_value', '5053', '5053', null, null, null, null, null, null, null,
        'servicerecovery', null);
insert into `act_ru_variable`
values ('5069', '1', 'string', 'sys_current_pid', '5053', '5053', null, null, null, null, null, null, null, '5053',
        null);
insert into `act_ru_variable`
values ('5070', '1', 'string', 'sys_current_workid', '5053', '5053', null, null, null, null, null, null, null, '14',
        null);
insert into `act_ru_variable`
values ('5071', '1', 'string', 'sys_current_worknumber', '5053', '5053', null, null, null, null, null, null, null,
        'other190218-89862', null);
insert into `act_ru_variable`
values ('5072', '1', 'string', 'sys_current_taskid', '5053', '5053', null, null, null, null, null, null, null, '5065',
        null);
insert into `act_ru_variable`
values ('5080', '1', 'string', 'sys_biz_createuser', '5079', '5079', null, null, null, null, null, null, null, 'admin',
        null);
insert into `act_ru_variable`
values ('5081', '1', 'null', 'sys_formtype', '5079', '5079', null, null, null, null, null, null, null, null, null);
insert into `act_ru_variable`
values ('5082', '1', 'string', 'sys_biz_id', '5079', '5079', null, null, null, null, null, null, null, '15', null);
insert into `act_ru_variable`
values ('5083', '1', 'string', 'handleuser', '5079', '5079', null, null, null, null, null, null, null, '14', null);
insert into `act_ru_variable`
values ('5085', '1', 'serializable', '_members', '5079', '5079', null, null, null, null, '5084', null, null, null,
        null);
insert into `act_ru_variable`
values ('5087', '1', 'string', 'sys_button_value', '5079', '5079', null, null, null, null, null, null, null, 'submit',
        null);
insert into `act_ru_variable`
values ('7502', '1', 'string', 'sys_biz_createuser', '7501', '7501', null, null, null, null, null, null, null, 'admin',
        null);
insert into `act_ru_variable`
values ('7503', '1', 'null', 'sys_formtype', '7501', '7501', null, null, null, null, null, null, null, null, null);
insert into `act_ru_variable`
values ('7504', '1', 'string', 'sys_biz_id', '7501', '7501', null, null, null, null, null, null, null, '16', null);
insert into `act_ru_variable`
values ('7505', '1', 'string', 'handleuser', '7501', '7501', null, null, null, null, null, null, null, '', null);
insert into `act_ru_variable`
values ('7507', '1', 'serializable', '_members', '7501', '7501', null, null, null, null, '7506', null, null, null,
        null);
insert into `act_ru_variable`
values ('7509', '2', 'string', 'sys_button_value', '7501', '7501', null, null, null, null, null, null, null,
        'servicerecovery', null);

-- ----------------------------
-- table structure for t_biz_counter_user
-- ----------------------------
drop table if exists `t_biz_counter_user`;
create table `t_biz_counter_user`
(
    `id`          int(11) not null,
    `biz_id`      varchar(32) default null,
    `create_time` datetime    default null,
    `name`        varchar(32) default null,
    `task_id`     varchar(32) default null,
    `user_name`   varchar(64) default null,
    primary key (`id`)
) engine=innodb default charset=utf8mb4;

-- ----------------------------
-- records of t_biz_counter_user
-- ----------------------------

-- ----------------------------
-- table structure for t_biz_file
-- ----------------------------
drop table if exists `t_biz_file`;
create table `t_biz_file`
(
    `id`               int(11) not null auto_increment,
    `create_time`      datetime    default null,
    `create_user`      varchar(64)  not null,
    `description`      varchar(512),
    `file_catalog`     varchar(64)  not null,
    `filetype`         varchar(64)  not null,
    `name`             varchar(64)  not null,
    `path`             varchar(256) not null,
    `task_id`          varchar(64) default null,
    `task_instance_id` varchar(64) default null,
    `task_name`        varchar(64),
    `biz_id`           varchar(64) default null,
    primary key (`id`),
    key                `fk34bt5oe9xt1ovakwe0nymup5m` (`biz_id`)
) engine=innodb auto_increment=4 default charset=utf8mb4;

-- ----------------------------
-- ----------------------------
-- table structure for t_biz_info
-- ----------------------------
drop table if exists `t_biz_info`;
create table `t_biz_info`
(
    `id`                    int(11) not null auto_increment,
    `work_num`              varchar(64) default null,
    `biz_type`              varchar(64)  not null,
    `create_time`           datetime    default null,
    `create_user`           varchar(64)  not null,
    `limit_time`            datetime    default null,
    `parent_id`             varchar(64) default null,
    `process_definition_id` varchar(64)  not null,
    `process_instance_id`   varchar(64) default null,
    `source`                varchar(128) not null,
    `biz_status`            varchar(32)  not null,
    `task_assignee`         varchar(64),
    `task_def_key`          varchar(64) default null,
    `task_id`               varchar(64),
    `task_name`             varchar(64),
    `title`                 longtext,
    primary key (`id`)
) engine=innodb auto_increment=22 default charset=utf8mb4;

-- ----------------------------
drop table if exists `t_biz_info_conf`;
create table `t_biz_info_conf`
(
    `id`            int(11) not null auto_increment,
    `create_time`   datetime    default null,
    `task_assignee` varchar(64),
    `task_id`       varchar(64) default null,
    `biz_id`        varchar(64) default null,
    primary key (`id`),
    key             `fk4sit8xk90bn7gi75v5ag5aaoe` (`biz_id`)
) engine=innodb auto_increment=20 default charset=utf8mb4;

-- ----------------------------
-- table structure for t_biz_log
-- ----------------------------
drop table if exists `t_biz_log`;
create table `t_biz_log`
(
    `id`                 int(10) not null auto_increment,
    `create_time`        datetime    default null,
    `handle_description` varchar(512),
    `handle_name`        varchar(64) not null,
    `handle_result`      varchar(64) not null,
    `handle_user_name`   varchar(64) default null comment '操作人名称',
    `handle_user`        varchar(64) not null,
    `task_id`            varchar(64) not null,
    `task_name`          varchar(64),
    `user_dept`          varchar(64) default null,
    `user_phone`         varchar(64) default null,
    `biz_id`             varchar(64) default null,
    primary key (`id`),
    key                  `fkbsvvskjwl92mbq0o22sxin9xi` (`biz_id`)
) engine=innodb auto_increment=33 default charset=utf8mb4;

-- ----------------------------
-- table structure for t_biz_process_instance
-- ----------------------------
drop table if exists `t_biz_process_instance`;
create table `t_biz_process_instance`
(
    `id`                  int(10) not null auto_increment,
    `biz_id`              varchar(64)  not null,
    `create_time`         datetime    default null,
    `process_instance_id` varchar(64) default null,
    `value`               longtext     not null,
    `process_variable_id` varchar(64) default null,
    `task_id`             varchar(32)  not null,
    `handle_user`         varchar(128) not null,
    `variable_alias`      varchar(32)  not null,
    `variable_name`       varchar(32)  not null,
    `view_component`      varchar(32) default null,
    primary key (`id`),
    key                   `fkm1ungk6wgwapfog91iscp9sod` (`process_variable_id`)
) engine=innodb auto_increment=47 default charset=utf8mb4;

-- ----------------------------
-- table structure for t_biz_process_variable
-- ----------------------------
drop table if exists `t_biz_process_variable`;
create table `t_biz_process_variable`
(
    `id`                    int(11) not null auto_increment,
    `alias`                 longtext    not null,
    `group_name`            longtext,
    `group_order`           int(11) default null,
    `name`                  longtext    not null,
    `name_order`            int(11) default null,
    `process_definition_id` varchar(64) not null,
    `is_process_variable`   bit(1)      default null,
    `ref_param`             longtext,
    `ref_variable`          varchar(64) default null,
    `is_required`           bit(1)      not null,
    `version`               int(11) not null,
    `view_component`        longtext,
    `view_datas`            longtext,
    `view_params`           longtext,
    `task_id`               varchar(32) default null,
    `view_url`              varchar(128),
    primary key (`id`)
) engine=innodb auto_increment=24 default charset=utf8mb4;

-- ----------------------------
-- records of t_biz_process_variable
-- ----------------------------
insert into `t_biz_process_variable`
values ('3', '处理方式', '', '1', 'handletype', '1', 'eventmanagement:3:2508', '\0', '', '', '\0', '3', 'treatment', '', '',
        'vendorhandle', null);
insert into `t_biz_process_variable`
values ('6', '处理意见', '', '1', 'handlemessage', '1', 'eventmanagement:3:2508', '\0', '', '', '\0', '3', 'textarea', '',
        '', 'start', null);
insert into `t_biz_process_variable`
values ('7', '处理方式', '', '1', 'handletype', '1', 'eventmanagement:3:2508', '\0', '', '7', '\0', '3', 'treatment', '',
        '', 'servicehandle', null);
insert into `t_biz_process_variable`
values ('8', '下拉', '', '1', 'ddd', '1', 'eventmanagement:3:2508', '\0', '', '', '\0', '3', 'dictcombobox', '1,2', '1',
        'start', null);
insert into `t_biz_process_variable`
values ('9', '处理方式', '', '1', 'handletype', '1', 'eventmanagement:3:2508', '\0', '', '', '\0', '3', 'treatment', '', '',
        'vendorhandle', null);
insert into `t_biz_process_variable`
values ('11', '工时', '', '1', 'worktime', '2', 'eventmanagement:3:2508', '\0', '', '', '', '3', 'number', '', '',
        'servicehandle', null);
insert into `t_biz_process_variable`
values ('12', '处理人', '', '1', 'handleuser', '2', 'eventmanagement:3:2508', '', '', '', '', '3', 'text', '', '',
        'start', null);
insert into `t_biz_process_variable`
values ('23', '事件级别', '', '1', 'level', '1', 'eventmanagement:3:2508', '\0', '', '', '\0', '3', 'text', '', '', 'start',
        null);

-- ----------------------------
-- table structure for t_biz_template_file
-- ----------------------------
drop table if exists `t_biz_template_file`;
create table `t_biz_template_file`
(
    `id`          int(10) not null auto_increment,
    `create_time` datetime    not null,
    `create_user` varchar(64) default null,
    `file_name`   varchar(64) default null,
    `flow_name`   varchar(32) not null,
    `full_name`   varchar(64) default null,
    primary key (`id`)
) engine=innodb auto_increment=6 default charset=utf8mb4;

-- ----------------------------
-- table structure for t_counter_sign
-- ----------------------------
drop table if exists `t_counter_sign`;
create table `t_counter_sign`
(
    `id`                    int(10) not null auto_increment,
    `create_time`           datetime default null,
    `is_complete`           int(11) not null,
    `process_definition_id` varchar(128) not null,
    `process_instance_id`   varchar(128) not null,
    `result_type`           int(11) not null,
    `task_assignee`         varchar(32)  not null,
    `task_id`               varchar(32)  not null,
    `biz_id`                varchar(32)  not null,
    primary key (`id`)
) engine=innodb default charset=utf8mb4;

-- ----------------------------
-- records of t_counter_sign
-- ----------------------------

-- ----------------------------
-- table structure for t_counter_user
-- ----------------------------
drop table if exists `t_counter_user`;
create table `t_counter_user`
(
    `id`          int(10) not null,
    `biz_id`      varchar(32) default null,
    `create_time` datetime    default null,
    `name`        varchar(32) default null,
    `task_id`     varchar(32) default null,
    `user_name`   varchar(32) default null,
    primary key (`id`)
) engine=innodb default charset=utf8mb4;

-- ----------------------------
-- records of t_counter_user
-- ----------------------------
-- ----------------------------
-- table structure for t_dict_type
-- ----------------------------
drop table if exists `t_dict_type`;
create table `t_dict_type`
(
    `id`          int(10) not null auto_increment,
    `create_time` datetime     default null,
    `creator`     varchar(255) default null,
    `modified`    datetime     default null,
    `modifier`    varchar(255) default null,
    `name`        varchar(255) default null,
    primary key (`id`)
) engine=innodb auto_increment=7 default charset=utf8mb4;

-- ----------------------------
-- records of t_dict_type
-- ----------------------------
insert into `t_dict_type`
values ('1', '2018-01-29 13:59:42', '1', '2018-01-29 13:59:48', '1', '性别');
insert into `t_dict_type`
values ('4', '2019-02-15 06:50:49', 'admin', '2019-02-15 06:50:49', 'admin', 'banmart');
insert into `t_dict_type`
values ('5', '2019-02-15 06:50:55', 'admin', '2019-02-15 06:50:55', 'admin', '亲橙里');
insert into `t_dict_type`
values ('6', '2019-02-15 06:51:00', 'admin', '2019-02-15 07:03:56', '1', '亲橙里编辑');

-- ----------------------------
-- table structure for t_dict_value
-- ----------------------------
drop table if exists `t_dict_value`;
create table `t_dict_value`
(
    `id`           int(10) not null auto_increment,
    `code`         varchar(255) default null,
    `create_time`  datetime     default null,
    `creator`      varchar(255) default null,
    `dict_type_id` varchar(255) default null,
    `modified`     datetime     default null,
    `modifier`     varchar(255) default null,
    `name`         varchar(255) default null,
    primary key (`id`)
) engine=innodb auto_increment=9 default charset=utf8mb4;

-- ----------------------------
-- records of t_dict_value
-- ----------------------------
insert into `t_dict_value`
values ('1', '1', null, null, '1', null, null, '男');
insert into `t_dict_value`
values ('2', '2', null, null, '1', '2019-02-15 08:28:46', 'admin', '女');
insert into `t_dict_value`
values ('8', '0', '2019-02-15 08:28:57', 'admin', '1', '2019-02-15 08:28:57', 'admin', '未知');

-- ----------------------------
-- table structure for t_sys_role
-- ----------------------------
drop table if exists `t_sys_role`;
create table `t_sys_role`
(
    `id`          int(10) not null auto_increment,
    `create_time` datetime    default null,
    `name_cn`     varchar(64) default null,
    `name_en`     varchar(64) default null,
    primary key (`id`)
) engine=innodb default charset=utf8mb4;

-- ----------------------------
-- records of t_sys_role
-- ----------------------------

-- ----------------------------
-- table structure for t_sys_user
-- ----------------------------
drop table if exists `t_sys_user`;
create table `t_sys_user`
(
    `id`              int(64) not null auto_increment,
    `create_time`     datetime    default null,
    `email`           varchar(64) default null,
    `last_login_time` datetime    default null,
    `name`            varchar(64) default null,
    `password`        varchar(64) default null,
    `status`          int(11) default null,
    `username`        varchar(64) default null,
    primary key (`id`)
) engine=innodb auto_increment=2 default charset=utf8mb4;

-- ----------------------------
-- records of t_sys_user
-- ----------------------------
insert into `t_sys_user`
values ('1', '2017-12-02 10:36:50', null, null, '超级管理员', 'admin', '1', 'admin');

-- ----------------------------
-- table structure for t_sys_user_role
-- ----------------------------
drop table if exists `t_sys_user_role`;
create table `t_sys_user_role`
(
    `role_id` int(64) not null,
    `user_id` int(64) not null,
    `id`      int(10) not null,
    primary key (`id`)
) engine=innodb default charset=utf8mb4;

-- ----------------------------
-- records of t_sys_user_role
-- ----------------------------

-- ----------------------------
-- table structure for t_timed_task
-- ----------------------------
drop table if exists `t_timed_task`;
create table `t_timed_task`
(
    `id`           int(10) not null,
    `biz_id`       int(10) default null,
    `button_id`    varchar(32)  default null,
    `create_time`  datetime     default null,
    `end_time`     varchar(255) default null,
    `task_def_key` varchar(64)  default null,
    `task_id`      varchar(64)  default null,
    `task_name`    varchar(64)  default null,
    primary key (`id`)
) engine=innodb default charset=utf8mb4;
