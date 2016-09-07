    create table sms_send_record (
        id int not null auto_increment,
        create_time datetime,
        state tinyint,
        content varchar(255),
        mobile_no varchar(255),
        result_code varchar(255),
        sender varchar(255),
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table sys_category (
        id int not null auto_increment,
        create_time datetime,
        state tinyint,
        category tinyint,
        name varchar(255),
        sort_num tinyint,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table sys_config (
        mkey varchar(255) not null,
        mvalue varchar(255),
        primary key (mkey)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table sys_module (
        id int not null auto_increment,
        create_time datetime,
        state tinyint,
        name varchar(255),
        sort_num tinyint,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table sys_permission (
        id int not null auto_increment,
        create_time datetime,
        state tinyint,
        code varchar(255),
        name varchar(255),
        sort_num tinyint,
        module_id int,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table sys_role (
        id int not null auto_increment,
        create_time datetime,
        state tinyint,
        name varchar(255),
        sort_num tinyint,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table sys_role_permission (
        role_id int not null,
        permission_id int not null
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table sys_user (
        id int not null auto_increment,
        true_name varchar(8),
        user_name varchar(32),
        nick_name varchar(32),
        gender tinyint,
        head_img varchar(255),
        mobile_no varchar(255),
        password varchar(255),
        salt varchar(255),
        is_virtual bit(1) not null default false,
        state tinyint,
        create_time datetime,
        creator varchar(255),
        edit_time datetime,
        editor varchar(255),
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table sys_user_role (
        user_id int not null,
        role_id int not null
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table tmp_file (
        id int not null auto_increment,
        create_time datetime,
        state tinyint,
        url varchar(255),
        user_id int,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    alter table sys_permission
        add constraint FK_8l7yu9swypl58fj3kiw7fcwkl
        foreign key (module_id)
        references sys_module (id);

    alter table sys_role_permission
        add constraint FK_8n7kjpptsuop8jvkeh4ls34ct
        foreign key (permission_id)
        references sys_permission (id);

    alter table sys_role_permission
        add constraint FK_6ov1c9l6b3e2o9d3q889c90l
        foreign key (role_id)
        references sys_role (id);

    alter table sys_user_role
        add constraint FK_fxu3td9m5o7qov1kbdvmn0g0x
        foreign key (role_id)
        references sys_role (id);

    alter table sys_user_role
        add constraint FK_fethvr269t6stivlddbo5pxry
        foreign key (user_id)
        references sys_user (id);


INSERT INTO sys_module (name, sort_num)
VALUES
	('系统管理', 99);

INSERT INTO sys_permission (name, code, sort_num, module_id)
VALUES
	('查看用户','sys:user:view',1,1),
	('编辑用户','sys:user:edit',2,1),
	('查看系统类别','sys:category:view',13,1),
	('编辑系统类别','sys:category:edit',14,1),
	('后台手动操作','sys:manual',50,1),
	('查看角色','sys:role:view',3,1),
	('编辑角色','sys:role:edit',4,1),
	('查看权限','sys:permission:view',5,1),
	('编辑权限','sys:permission:edit',6,1),
	('查看模块','sys:module:view',7,1),
	('编辑模块','sys:module:edit',8,1),
	('查看系统设置','sys:config:view',9,1),
	('编辑系统设置','sys:config:edit',10,1),
	('修改密码','sys:user:changePwd',2,1);

INSERT INTO sys_role (name, sort_num)
VALUES
	('系统管理员',1);

INSERT INTO sys_role_permission (role_id, permission_id)
VALUES
  (1,1),
  (1,2),
  (1,3),
  (1,4),
  (1,5),
  (1,6),
  (1,7),
  (1,8),
  (1,9),
  (1,10),
  (1,11),
  (1,12),
  (1,13),
  (1,14);

INSERT INTO sys_user (id, create_time, state, creator, edit_time, editor, gender, head_img, mobile_no, password, salt, true_name, user_name)
VALUES
	(1, now(), 1, NULL, NULL, NULL, 1, NULL, NULL, '8dbb0690ffdf77d5c0d20cdbf30505af57fed43b', '96438695e943022e', '管理员', 'admin');


INSERT INTO sys_user_role (user_id, role_id)
VALUES
	(1,1);


/**业务sql*/
create table goods_category (
        id int not null auto_increment,
        create_time datetime,
        state tinyint(2),
        img_url varchar(255),
        name varchar(255),
        sort_num tinyint(2),
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table goods_image (
        id int not null auto_increment,
        create_time datetime,
        state tinyint(2),
        category tinyint(2),
        edit_time datetime,
        url varchar(255),
        goods_id int,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


    create table goods_info (
        id int not null auto_increment,
        name varchar(255) DEFAULT NULL,
        tip varchar(64) DEFAULT NULL,
        price int(11) DEFAULT NULL,
        total_times int(11) DEFAULT NULL,
        unit_money int(11) DEFAULT NULL,
        is_ten_yuan bit(1) DEFAULT NULL,
        thumbnail varchar(255) DEFAULT NULL,
        goods_category_id int(11) DEFAULT NULL,
        details_html_url varchar(128),
        sales_amount int,
        create_time datetime DEFAULT NULL,
        creator varchar(255) DEFAULT NULL,
        edit_time datetime DEFAULT NULL,
        editor varchar(255) DEFAULT NULL,
        state tinyint(2) DEFAULT NULL,
        version int not null DEFAULT 0,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table goods_share (
        id int not null auto_increment,
        title varchar(255),
        content varchar(255),
        user_id int,
        user_nick_name varchar(255),
        user_head_img VARCHAR(128),
        goods_times_id int,
        goods_times_name int,
        goods_name varchar(255),
        create_time datetime,
        state tinyint(2),
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table goods_share_image (
        id int not null auto_increment,
        create_time datetime,
        state tinyint(2),
        url varchar(255),
        goods_share_id int,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table goods_times (
        id int not null auto_increment,
        times int(11) DEFAULT NULL,
        good_id int(11) DEFAULT NULL,
        goods_name varchar(255) DEFAULT NULL,
        goods_tip varchar(64) DEFAULT NULL,
        is_ten_yuan bit(1),
        total_times int(11) DEFAULT NULL,
        total_buy_times int(11) DEFAULT NULL,
        snatch_progress int(11) DEFAULT NULL,
        avalue bigint DEFAULT NULL,
        bvalue bigint DEFAULT NULL,
        luck_num int,
        cqssc_period_no varchar(16),
        full_time bigint,
        open_time datetime DEFAULT NULL,
        winng_user_id int(11) DEFAULT NULL,
        winng_user_identity varchar(32),
        winng_user_name varchar(255) DEFAULT NULL,
        winng_user_head_img varchar(128),
        buy_times int(11) DEFAULT NULL,
        logistics_name varchar(255) DEFAULT NULL,
        logistics_order_no varchar(255) DEFAULT NULL,
        recevice_address varchar(255) DEFAULT NULL,
        has_share_goods bit(1) default false,
        create_time datetime DEFAULT NULL,
        state tinyint(2) DEFAULT NULL,
        winng_state tinyint(2),
        is_first_open bit(1) not null default true,
        fail_deadline_date datetime,
        can_get_cqsscno bit(1) not null default true,
        compute_detail_uri varchar(128),
        version int not null DEFAULT 0,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table order_pay_record (
        id int not null auto_increment,
        money int,
        user_id int,
        out_trade_no varchar(32),
        sign varchar(32),
        thirdpart_pay_time datetime,
        result_descr varchar(255),
        to_balance int,
        create_time datetime,
        state tinyint(2),
        version int not null DEFAULT 0,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table order_pay_way (
        id int not null auto_increment,
        create_time datetime,
        state tinyint(2),
        money int,
        way tinyint(2),
        pay_record_id int,
        redpack_id int,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table order_snatch_list_item (
        id int not null auto_increment,
        create_time datetime,
        state tinyint(2),
        buy_times int,
        money int,
        goods_id int,
        goods_times_id int,
        user_id int,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table order_snatch_num (
        id int not null auto_increment,
        create_time datetime,
        state tinyint(2),
        address varchar(255),
        buy_time datetime,
        ip varchar(255),
        num int,
        goods_times_id int,
        snatch_record_id int,
        user_id int,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table order_snatch_record (
        id int not null auto_increment,
        create_time datetime,
        state tinyint(2),
        buy_time datetime,
        money int,
        total_times int,
        goods_times_id int,
        user_id int,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table order_snatch_record_detail (
        id int not null auto_increment,
        buy_times int,
        money int,
        user_id int,
        user_head_img varchar(128),
        user_nick_name varchar(32),
        address varchar(64),
        ip varchar(16),
        goods_id int,
        goods_times_id int,
        pay_record_id int,
        snatch_record_id int,
        snatch_time bigint,
        create_time datetime,
        state tinyint(2),
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table record_jifen (
        id int not null auto_increment,
        create_time datetime,
        state tinyint(2),
        amount int,
        descr varchar(255),
        member_id int,
        user_id int,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table record_recharge (
        id int not null auto_increment,
        money int,
        pay_way tinyint(2),
        user_id int,
        out_trade_no varchar(32),
        pay_time datetime,
        create_time datetime,
        state tinyint(2),
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table record_redpack (
        id int not null auto_increment,
        total int,
        balance int,
        source_type tinyint(2),
        expire_time datetime,
        user_id int,
        create_time datetime,
        state tinyint(2),
        version int not null DEFAULT 0,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table sys_image_setting (
        id int not null auto_increment,
        category tinyint(2),
        sort_num tinyint(2),
        url varchar(255),
        action_type tinyint(2),
        value varchar(128),
        create_time datetime,
        state tinyint(2),
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table sys_member (
        id int not null auto_increment,
        true_name varchar(255),
        birthday date,
        member_level_id int,
        balance int,
        can_snatch bit(1),
        can_speak bit(1),
        head_img varchar(255),
        jifen int,
        user_id int,
        edit_time datetime,
        create_time datetime,
        state tinyint(2),
        version int not null DEFAULT 0,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table sys_member_level (
        id int not null auto_increment,
        create_time datetime,
        state tinyint(2),
        max_value int,
        min_value int,
        name varchar(255),
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table sys_receive_address (
        id int not null auto_increment,
        create_time datetime,
        state tinyint(2),
        city varchar(255),
        detail_address varchar(255),
        district varchar(255),
        is_default bit(1),
        province varchar(255),
        receiver varchar(255),
        tel varchar(255),
        zip varchar(255),
        user_id int,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    alter table goods_image
        add constraint FK_c73gntlh7yxnuw6q357ujhg5j
        foreign key (goods_id)
        references goods_info (id);

    alter table goods_info
        add constraint FK_f0l1glitpsyyn3sidvkxvtuxj
        foreign key (goods_category_id)
        references goods_category (id);

    alter table goods_share
        add constraint FK_rp91k332614kcqxsxyi0rrf81
        foreign key (user_id)
        references sys_user (id);

    alter table goods_share
        add constraint FK_shrsjmive03fpe6mkui5uf26y
        foreign key (goods_times_id)
        references goods_times (id);

    alter table goods_share_image
        add constraint FK_a3alb9kyopc6gdpi0svl3x2u9
        foreign key (goods_share_id)
        references goods_share (id);

    alter table goods_times
        add constraint FK_8k1f1oi6uakdfrslrvy03wtgw
        foreign key (good_id)
        references goods_info (id);

        alter table goods_times
            add constraint FK_1h79y0be4llx0tsy452d3pf7m
            foreign key (winng_user_id)
            references sys_user (id);

    alter table order_pay_record
        add constraint FK_qut8ljvu3bdixfeupxl6049v6
        foreign key (user_id)
        references sys_user (id);

    alter table order_pay_way
        add constraint FK_34pwbtffqribivotl6usr997g
        foreign key (pay_record_id)
        references order_pay_record (id);

    alter table order_snatch_list_item
        add constraint FK_mlhh3iqltj6dt7rqqjrmwv4u2
        foreign key (goods_id)
        references goods_info (id);

    alter table order_snatch_list_item
        add constraint FK_oows05gndudhsl59mf66u1avf
        foreign key (goods_times_id)
        references goods_times (id);

    alter table order_snatch_list_item
        add constraint FK_hxm7hbxg1rtidbrvwetes6gjk
        foreign key (user_id)
        references sys_user (id);

    alter table order_snatch_num
        add constraint FK_2vc1hb2oblo2ay39osuwdgac7
        foreign key (goods_times_id)
        references goods_times (id);

    alter table order_snatch_num
        add constraint FK_f8ofu69b1ghk03m2e619pbmar
        foreign key (snatch_record_id)
        references order_snatch_record_detail (id);

    alter table order_snatch_num
        add constraint FK_8uv7k107y286podrn2t3f21h5
        foreign key (user_id)
        references sys_user (id);

    alter table order_snatch_record
        add constraint FK_6bp6x5qko1rpgodgtfo9mo8lw
        foreign key (goods_times_id)
        references goods_times (id);

    alter table order_snatch_record
        add constraint FK_a6voechastyj7b5k052agnsdx
        foreign key (user_id)
        references sys_user (id);

    alter table order_snatch_record_detail
        add constraint FK_bgq5fqxamhje3efjhad7q5wsc
        foreign key (goods_id)
        references goods_info (id);

    alter table order_snatch_record_detail
        add constraint FK_k9wmp25xx95eygvvq1qn1qli8
        foreign key (goods_times_id)
        references goods_times (id);

    alter table order_snatch_record_detail
        add constraint FK_pbj2y3ggn5eg2jt8niuf0audl
        foreign key (pay_record_id)
        references order_pay_record (id);

    alter table order_snatch_record_detail
        add constraint FK_7d9hw7q2jcrrnpkw784uwo6bh
        foreign key (snatch_record_id)
        references order_snatch_record (id);

        alter table order_snatch_record_detail
            add constraint FK_r0qdadw4dnjte2xc1hg9ehwa1
            foreign key (user_id)
            references sys_user (id);

    alter table record_jifen
        add constraint FK_ew23nqj2tv3py7fu7x5t2w3xb
        foreign key (member_id)
        references sys_member (id);

    alter table record_jifen
        add constraint FK_1wder5job4qjk20vo8xhrpuro
        foreign key (user_id)
        references sys_user (id);

    alter table record_recharge
        add constraint FK_fhtf3a1gb5ihq2v75vdrq2e48
        foreign key (user_id)
        references sys_user (id);

    alter table record_redpack
        add constraint FK_b9cmw50ss63071x472uscfoyu
        foreign key (user_id)
        references sys_user (id);

    alter table sys_member
        add constraint FK_ascva6y5cgv3xfpy9ddupoeuu
        foreign key (member_level_id)
        references sys_member_level (id);

    alter table sys_member
        add constraint FK_ldwq8ixn8swqfx6qnt44nj1np
        foreign key (user_id)
        references sys_user (id);

    alter table sys_receive_address
        add constraint FK_5jdao1uld6a2j1mmi5o5nc0dt
        foreign key (user_id)
        references sys_user (id);


    create table notice (
        id int not null auto_increment,
        create_time datetime,
        state tinyint,
        content varchar(255),
        sender_name varchar(255),
        title varchar(255),
        sender_id int,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    create table notice_send_record (
        id int not null auto_increment,
        create_time datetime,
        state tinyint,
        to_user_name varchar(255),
        notice_id int,
        to_user_id int,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    alter table notice
    add constraint FK_a6cfyv0iuxwd7p6gj82v89gng
    foreign key (sender_id)
    references sys_user (id);

    alter table notice_send_record
    add constraint FK_bnbtsaykuvrg8t42kumg4bv58
    foreign key (notice_id)
    references notice (id);

    alter table notice_send_record
    add constraint FK_k8dd2ht6ha0w7faxu0t4c8du5
    foreign key (to_user_id)
    references sys_user (id);


    alter table order_snatch_record add goods_times_name int;

    alter table order_snatch_record add buy_times int;

    alter table order_snatch_record drop buy_time;

    alter table order_snatch_record add goods_name varchar(255);

    create table goods_times_num (
        id int not null auto_increment,
        goods_times_id int,
        num int,
        create_time datetime,
        state tinyint,
        primary key (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    alter table goods_times_num
    add constraint FK_k8dd2ht6ha0w7faxu0t4c8du6
    foreign key (goods_times_id)
    references goods_times(id);

    alter table order_pay_way add constraint foreign key
        (redpack_id) references record_redpack(id);

    create table record_buy_record (
        id int not null auto_increment primary key,
        goods_times_id int not null,
        snatch_record_detail_id int not null,
        user_id int,
        user_nick_name varchar(32),
        snatch_time	bigint,
        time_value int,
        create_time datetime,
        state tinyint(2),
        unique key (goods_times_id, snatch_record_detail_id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

    alter table record_buy_record add constraint
    foreign key (goods_times_id) references goods_times(id);

    alter table record_buy_record add constraint
    foreign key (snatch_record_detail_id) references order_snatch_record_detail(id);
