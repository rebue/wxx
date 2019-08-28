/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/8/28 15:13:56                           */
/*==============================================================*/


drop table if exists WXX_APP;

drop table if exists WXX_MCH;

/*==============================================================*/
/* Table: WXX_APP                                               */
/*==============================================================*/
create table WXX_APP
(
   APP_ID               varchar(30) not null comment 'APP_ID',
   APP_NAME             varchar(30) not null comment 'APP名称',
   MCH_ID               varchar(20) not null comment '商户号',
   APP_SECRET           varchar(50) not null comment 'APP_SECRET',
   TOKEN                varchar(50) not null comment 'TOKEN',
   ENCODEING_AES_KEY    varchar(50) comment 'ENCODEING_AES_KEY',
   SUBSCRIBE_AUTO_REPLY varchar(100) comment '用户关注后自动回复的文本',
   MENU                 varchar(1500) comment '自定义菜单',
   WXPAY_NOTIFY_URL     varchar(250),
   primary key (APP_ID)
);

alter table WXX_APP comment 'App信息';

/*==============================================================*/
/* Table: WXX_MCH                                               */
/*==============================================================*/
create table WXX_MCH
(
   MCH_ID               varchar(20) not null comment '商户号',
   MCH_NAME             varchar(30) not null comment '商户名称',
   API_KEY              varchar(50) not null comment 'API密钥，签名用的key，在商户平台设置（微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置',
   primary key (MCH_ID)
);

alter table WXX_MCH comment '商户信息，也就是微信支付账户信息';

alter table WXX_APP add constraint FK_RELATIONSHIP_1 foreign key (MCH_ID)
      references WXX_MCH (MCH_ID) on delete restrict on update restrict;

