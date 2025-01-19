## 简介
<font style="color:rgb(51, 51, 51);">统一登录中心（MoYu-Auth）是基于 CAS 思想的的企业级 SSO 单点登录系统，设计时主要包含以下特性：</font>

+ 支持 SSO 单点登录
+ 基于 OAuth2 + OIDC 协议进行身份认证
+ 支持多租户，支持 SAAS 化开租
+ 统一账户管理
+ 统一权限管理
+ 支持分布式部署

<font style="color:rgb(51, 51, 51);">CAS ，即是 Central Authentication Service，包含三部分</font>

+ <font style="color:rgb(51, 51, 51);">Client: 用户或浏览器</font>
+ <font style="color:rgb(51, 51, 51);">Server: 中心服务器，负责单点登录的服务器，即 MoYu SSO 认证中心 </font>
+ <font style="color:rgb(51, 51, 51);">Service: 需要使用单点登录的各个应用系统。负责处理对客户端受保护资源的访问请求，对请求方进行身份认证时，重定向到 CAS Server 进行认证</font>

基于中心登录态，可以通过几次浏览器跳转来实现不同应用之间的 SSO 免登

## 1 概念设计
统一登录中心（MoYu-Auth）设计了3个概念，即应用、租户、登录中心。

### 1.1 应用
每个三方系统作为单个应用接入统一登录中心，也可以多个三方系统作为同一个应用接入，应用接入登录中心后，登录中心会下发应用的 AppId 和 AppSecret，作为登录中心与三方应用通信的鉴权密钥。

+ 一方应用：指 MoYu-Auth 应用本身
+ 二方应用：指 MoYu-Group 组织下的内部应用
+ 三方应用：指 MoYu-Group 组织外部应用

### 1.2 租户
每个应用系统下的用户集合即视为一个租户，

每个租户下可以拥有自己的账号体系和权限体系，

每个租户下的账号、权限数据隔离，

每个租户的管理员可管理自己租户下的所有账号、权限数据，

租户创建后，每个应用可以进行租户开通，每个租户可被多个应用同时开通，

应用开通租户后，该租户下的所有账号均可登录开通该租户的应用，

多个应用开通同一个租户，即实现了同一租户下账号可登录多个应用，实现账号、权限打通

### 1.3 登录中心
登录中心为接入的三方系统提供登录服务，三方系统接入统一登录中心后，未登录的用户请求将会重定向到登录中心进行登录认证，用户登录后先在登录中心建立全局登录态，再重定向回三方系统建立应用登录态。

## 2 系统模块设计
根据概念设计出4个系统模块：

+ 应用管理
+ 租户管理
+ 租户控制台
+ 登录中心

### 2.1 应用管理
https://moyu-login.ffis.me/app

面向 MoYu-Auth 登录中心的平台管理人员，三方用户（三方应用的开发人员）

权限控制：平台管理员可查看所有应用

三方应用开发人员只能查看自己创建的应用

进行平台应用的查询、创建、编辑、删除、租户的开通与删除等操作

### 2.2 租户管理
https://moyu-login.ffis.me/tenant

面向登录中心的平台管理人员；

进行平台所有租户的查询、创建、编辑、删除等操作

### 2.4 租户控制台
https://moyu-login.ffis.me/console

面向租户管理员使用，每个租户都有自己的租户管理员；

租户管理员可登录租户控制台，对租户下的人员、权限进行管理

### 2.3 登录中心
https://moyu-login.ffis.me

面向租户下的所有用户进行登录使用；

为接入的应用提供用户登录服务，同时为本系统中的应用管理系统、租户管理系统提供登录服务；

## 3 流程设计
### 3.1 SSO登录流程
![画板](https://cdn.nlark.com/yuque/0/2024/jpeg/2304260/1711690874283-8c01c782-17e8-48ef-a424-23bc67a4fa20.jpeg)

### 3.2 SSO注销流程（待更新）
![画板](https://cdn.nlark.com/yuque/0/2024/jpeg/2304260/1712421422483-6e147b1f-e382-44d4-a26c-0195a58b9265.jpeg)

### 3.3 账密登录流程
![画板](https://cdn.nlark.com/yuque/0/2024/jpeg/2304260/1709830841856-bb4e483d-8013-463a-a0cd-d5c997261342.jpeg)

### 3.4 登录过滤器流程
1）MoYu-Auth 登录中心 登录过滤器

![画板](https://cdn.nlark.com/yuque/0/2023/jpeg/2304260/1702397853553-6b17cfa8-34e6-46c4-a657-d37dda69a456.jpeg)

2）MoYu-APP 应用登录过滤器

![画板](https://cdn.nlark.com/yuque/0/2024/jpeg/2304260/1711688641438-b4ea58e5-3874-40a8-b672-1f98897e359b.jpeg)

### 3.4 登录重定向流程设计
![画板](https://cdn.nlark.com/yuque/0/2024/jpeg/2304260/1711977284245-9e4395b5-b7be-498e-92a3-1e6a9b03394e.jpeg)

## 4 登录鉴权设计
用户登录成功后，会写入两个Cookie

SSO_TOKEN 和 USER_COOKIE

![画板](https://cdn.nlark.com/yuque/0/2023/jpeg/2304260/1702396844889-f0bf268b-8992-457f-94a4-50f5aec49b57.jpeg)

### 4.1 SSO_TOKEN
MoYu-Auth 登录中心在用户登录成功后颁发的 ssoToken，即登录中心长期登录态

有效期：登录成功后24h

Cookie key： App应用名+SSO_TOKEN+Cookie 版本号

Cookie value：32位的 UUID

例如：

moyu-auth_SSO_TOKEN_V1=0b742446790c49b98d3f298155371d82

+ Cookie版本
    - V1 版本：32位的 UUID，只包含用户的登录状态，不包含其他信息

### 4.2 USER_COOKIE
通过 MoYu-Auth 登录中心 SSO 登录成功的三方应用向用户颁发的应用短期登录态

主要储存用户的登录信息，如用户ID，用户名，租户ID等，将用户登录信息加密后储存在 Cookie 中，每次请求应用时携带 Cookie 请求，Cookie 有效则视为用户已登录；

储存在 Cookie 中是为了避免应用去维护用户登录态而增加系统复杂性，所带来的问题就是已经登录的用户没法云端管理和注销，不过由于设计上应用建立的都是短期登录态，注销问题可以暂时不考虑；后续可以考虑在应用端储存用户中心登录态来解决注销问题；

有效期：30分钟

失效后，则尝试请求 MoYu-Auth 登录中心重新建立会话状态，如果存在登录中心的有效登录态 SSO_TOKEN，则重新回调到应用进行免登，重新生成USER_COOKIE，如果登录中心不存在有效登录态，则需要重新登陆

Cookie key：App应用名+SSO_TOKEN+Cookie 版本号

Cookie value：加密后的用户登录信息

V1 版本 Cookie：将用户登录信息，使用 AES-256 加密，加密模式 GCM，使用应用AppSecret生成SHA256的散列值，然后截取前256位（32字节）来作为AES加密秘钥，然后将加密的后的内容使用 Base64 编码

例如：

moyu-blog_USER_COOKIE_V1=Base64(IV) + ":" + Base64(EncryptedData)

```json
moyu-blog_USER_COOKIE_V1=ZTIyMjA1MTA5MmI0MDRhMDNhMWQ1NDE5:4iIFEJK0BKA6HVQZbsAPpCJyBcmy2fP8uo2Vcwaxv72IW85L3nGu7YUKDeTxqs34U91Dfoq+8ql036GZF7bSIWM2beLEYKmI1mVsDYm1bebjYscZf4fuZ0NTGA7pAiEoTs7o
```

其中 IV 为 AES 加密的随机向量值

EncryptedData 为通过 AES-256 加密后的数据

## 5 数据库模型设计
本应用使用 PostgreSQL 数据库

### 5.1 租户表
```sql
CREATE TABLE tenant (
    id SERIAL PRIMARY KEY,         -- 主键ID，自动增长
    creator VARCHAR(128),                 -- 创建人
    modifier VARCHAR(128),                -- 修改人
    create_time TIMESTAMP NOT NULL DEFAULT NOW(),-- 创建时间
    update_time TIMESTAMP NOT NULL DEFAULT NOW(),-- 修改时间
    is_deleted BOOLEAN NOT NULL DEFAULT false,  -- 是否删除
    tenant_id VARCHAR(32) NOT NULL, -- 租户ID
    tenant_name VARCHAR(128) NOT NULL, -- 租户名称
    tenant_description TEXT          -- 租户描述，使用TEXT类型以支持较长文本
);
```

### 5.2 用户表
```sql
CREATE TABLE "user" (
  "id" int8 NOT NULL,
  "creator" varchar(128) COLLATE "pg_catalog"."default",
  "modifier" varchar(128) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "update_time" timestamp(6) NOT NULL,
  "is_deleted" bool NOT NULL,
  "user_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "username" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "mobile" varchar(32) COLLATE "pg_catalog"."default",
  "email" varchar(48) COLLATE "pg_catalog"."default",
  "full_name" varchar(64) COLLATE "pg_catalog"."default",
  "last_login_time" timestamp(6),
  "user_status" varchar(255) COLLATE "pg_catalog"."default",
  CONSTRAINT "user_pkey" PRIMARY KEY ("id"),
  CONSTRAINT "user_mobile_key" UNIQUE ("mobile"),
  CONSTRAINT "user_user_id_key" UNIQUE ("user_id"),
  CONSTRAINT "user_username_key" UNIQUE ("username"),
  CONSTRAINT "user_email_key" UNIQUE ("email")
)
;
```

### 5.3 租户用户关联表
```sql
CREATE TABLE tenant_user (
    id SERIAL PRIMARY KEY,      -- 主键ID，自动增长
    tenant_id VARCHAR(32) NOT NULL,			-- 租户ID
    user_id VARCHAR(32) NOT NULL,				-- 用户ID
);
```

### 5.4 用户登录会话表
```sql
CREATE TABLE user_session (
    id SERIAL PRIMARY KEY,                      -- 主键ID, 自动增长
    session_id VARCHAR(255) NOT NULL UNIQUE,    -- 会话ID, 唯一
    user_id VARCHAR(32) NOT NULL,               -- 用户ID
    tenant_id VARCHAR(32),                      -- 租户ID
    created_time TIMESTAMP NOT NULL,            -- 会话创建时间, 默认为当前时间
    expire_time TIMESTAMP NOT NULL,             -- 会话过期时间
    user_ip VARCHAR(32),                        -- 用户IP地址
    user_agent varchar(255)                     -- 用户浏览器/客户端信息
);
```

### 5.5 SSO_TOKEN 令牌表
```sql
CREATE TABLE user_token (
    id SERIAL PRIMARY KEY,                   -- 主键ID, 自动增长
    session_id VARCHAR(255) NOT NULL,        -- 用户中心登录态会话ID
    access_token VARCHAR(255) NOT NULL,      -- 访问令牌
    refresh_token VARCHAR(255),              -- 刷新令牌
    token_expire_at TIMESTAMP NOT NULL,      -- 令牌过期时间
    token_type VARCHAR(50)                   -- 令牌类型
);
```

### 5.6 应用表
```sql
CREATE TABLE app (
    id SERIAL PRIMARY KEY,                    -- 主键ID, 自动增长
    creator VARCHAR(128),                 -- 创建人
    modifier VARCHAR(128),                -- 修改人
    create_time TIMESTAMP NOT NULL DEFAULT NOW(),-- 创建时间
    update_time TIMESTAMP NOT NULL DEFAULT NOW(),-- 修改时间
    is_deleted BOOLEAN NOT NULL DEFAULT false,  -- 是否删除
    app_id VARCHAR(32) NOT NULL UNIQUE,       -- 应用ID, 唯一
    app_name VARCHAR(255) NOT NULL,           -- 应用名称
    app_description TEXT,                     -- 应用描述
    app_secret VARCHAR(255) NOT NULL,         -- 应用密钥
    app_aes_key VARCHAR(255) NOT NULL,        -- 应用AES密钥
    redirect_uri VARCHAR(255),                -- 重定向地址
    enabled BOOLEAN NOT NULL   -- 是否启用
);
```

### 5.7 应用开通租户表
```sql
CREATE TABLE app_tenant (
    id SERIAL PRIMARY KEY,          -- 主键ID, 自动增长
    app_id VARCHAR(32) NOT NULL,    -- 应用ID
    tenant_id VARCHAR(32) NOT NULL, -- 租户ID
);
```

### 5.8 租户设置表
```sql
CREATE TABLE tenant_setting (
    id SERIAL PRIMARY KEY,                -- 主键ID, 自动增长
    tenant_id INT NOT NULL,               -- 租户ID
    setting_key VARCHAR(255) NOT NULL,    -- 设置的键（Key）
    setting_value TEXT NOT NULL,          -- 设置的值（Value）
);
```

### 5.9 审计日志
```sql
CREATE TABLE audit_log (
    id SERIAL PRIMARY KEY,               -- 主键ID, 自动增长
    action_type VARCHAR(50) NOT NULL,    -- 操作类型，如CREATE, UPDATE, DELETE等
    action_timestamp TIMESTAMP NOT NULL DEFAULT NOW(), -- 操作时间，默认为当前时间
    action_ip VARCHAR(50),               -- 操作IP地址
    action_details TEXT                  -- 操作信息，详细描述操作内容
);
```

## 6 接口设计
### 6.1 登录中心页面
#### 1 登录页面
URL：/ssoLogin.html

请求参数：

| <font style="color:#000000;">参数名称</font> | 类型 | 位置 | 必须 | 描述 |
| --- | --- | --- | --- | --- |
| <font style="color:#000000;">appId</font> | String | path | 否 | 登录的AppId |
| <font style="color:#000000;">backUrl</font> | String | path | 否 | 登录成功后回调地址 |


#### 2 切换租户页面
URL：/switchTenant.html

请求参数：

| <font style="color:#000000;">参数名称</font> | 类型 | 位置 | 必须 | 描述 |
| --- | --- | --- | --- | --- |
| <font style="color:#000000;">appId</font> | String | path | 否 | 登录的AppId |
| <font style="color:#000000;">backUrl</font> | String | path | 否 | 登录成功后回调地址 |


### 6.2 登录中心接口
#### 1 登录接口
单点登录接口

Path：/ssoLogin

Method：POST

请求参数：

| 参数名称 | 类型 | 位置 | 必须 | 描述 |
| --- | --- | --- | --- | --- |
| username | String | body | 是 | 用户名 |
| password | String | body | 是 | 密码 |
| loginType | String | body | 是 | 登录类型，账密登录、手机验证码登录等 |
| appId | String | path | 否 | 登录的AppId |
| <font style="color:rgb(51, 54, 57);">redirectUri</font> | String | path | 否 | 登录成功后回调地址 |
| <font style="color:rgb(51, 54, 57);">state</font> | String | body | 否 | 本次登陆 state，登陆成功会返回给客户端，用于客户端判断是否是自己发起的登陆 |
| <font style="color:rgb(13, 13, 13);">scope</font> | String | body | 否 | 登录时获取用户信息作用域，用于控制获取的用户信息，如openid <font style="color:rgb(13, 13, 13);">profile email等</font> |


**接口响应：**

```json
{
  "success": true,
  "code": "00000",
  "message": "",
  "content": {
    "redirectUrl": "/ssoLogin.html"
  },
  "traceId": "46742ce9c2cc4d2ebfc20ac007ba7e1c"
}
```

登录成功后由前端根据返回的地址进行302重定向

| 参数名称 | 类型 | 位置 | 必须 | 描述 |
| --- | --- | --- | --- | --- |
| redirectUrl | String | body | 是 | 重定向地址 |


#### 2 注销登录
Path：/ssoLogout

Method：POST，GET

请求参数：

| 参数名称 | 类型 | 位置 | 必须 | 描述 |
| --- | --- | --- | --- | --- |
| <font style="color:rgb(51, 54, 57);">redirectUri</font> | String | path | 否 | 注销成功后回调地址 |


响应信息：

注销成功后，302跳转到<font style="color:rgb(51, 54, 57);">redirectUri，重新进入登录流程</font>

#### 3 获取登录用户信息
验证ssoToken，验证成功会返回登录的用户信息

Path：/api/sso/getUser

Method：POST

**请求参数：**

| 参数名称 | 类型 | 位置 | 必须 | 描述 |
| --- | --- | --- | --- | --- |
| appId | String | body | 是 | 应用ID |
| appSecret | String | body | 是 | 应用密钥 |
| ssoToken | String | body | 是 | ssoToken |


**接口响应：**

```json
{
    "success": true,
    "code": "00000",
    "message": "",
    "content": {
        "appId": "demo-client",
        "user": {
            "tenantId": "10000",
            "tenantName": "摸鱼开发小组",
            "userId": "10001",
            "username": "admin",
            "nickname": "饭饭",
            "mobile": null,
            "email": null
        }
    },
    "traceId": "01a57c18634b488abddb90384b4529b9"
}
```

| 参数名称 | 类型 | 必须 | 描述 |
| --- | --- | --- | --- |
| appId | String | 是 | 登录的应用ID |
| <font style="color:#8A8F8D;">∟ </font><font style="color:#000000;">tenantId</font> | String | 是 | 租户ID |
| <font style="color:#8A8F8D;">∟ </font>tenantName | String | 是 | 租户名称 |
| <font style="color:#8A8F8D;">∟ </font><font style="color:rgb(51, 54, 57);">userId</font> | String | 是 | <font style="color:rgb(51, 54, 57);">用户ID</font> |
| <font style="color:#8A8F8D;">∟ </font>username | String | 是 | 用户名 |
| <font style="color:#8A8F8D;">∟ </font>nickname | String | 是 | 昵称 |
| <font style="color:#8A8F8D;">∟ </font>mobile | String | 否 | 手机号 |
| <font style="color:#8A8F8D;">∟ </font>email | String | 否 | 邮箱 |


#### 4 切换登录租户
用于切换当前用户登录的租户信息

Path：/api/switch/tenant/doSwitch

Method：POST

**请求参数：**

| 参数名称 | 类型 | 位置 | 必须 | 描述 |
| --- | --- | --- | --- | --- |
| tenantId | String | body | 是 | 切换的租户ID |
| backUrl | String | body | 否 | 切换成功后跳转的地址 |


**接口响应：**

```json
{
  "success": true,
  "code": "00000",
  "message": "",
  "content": {
    "redirectUrl": "/ssoLogin.html"
  },
  "traceId": "46742ce9c2cc4d2ebfc20ac007ba7e1c"
}
```

登录成功后由前端根据返回的地址进行302重定向

| 参数名称 | 类型 | 位置 | 必须 | 描述 |
| --- | --- | --- | --- | --- |
| redirectUrl | String | body | 是 | 重定向地址 |


#### 5 获取应用信息
用于应用启动时拉取应用的信息



### 6.3 应用端接口
#### 1 处理SSO登陆成功后的回调
用户登录后，登录中心携带ssoToken回调到应用侧，建立应用登录态

Path：/verifySSOToken

Method：GET / POST

**请求参数：**

| 参数名称 | 类型 | 位置 | 必须 | 描述 |
| --- | --- | --- | --- | --- |
| ssoToken | String | path | 是 | ssoToken |
| <font style="color:rgb(51, 54, 57);">backUrl</font> | String  | path | 是 | 跳转地址 |
| <font style="color:rgb(51, 54, 57);">state</font> | String | path | 否 | 本次登陆 state，登陆成功会返回给客户端，用于客户端判断是否是自己发起的登陆 |


**响应参数：**

写入用户登录Cookie，302跳转到回调地址

#### 2 应用端注销接口
应用端发起注销

Path：/ssoLogout

Method：GET / POST

**请求参数：**

| 参数名称 | 类型 | 位置 | 必须 | 描述 |
| --- | --- | --- | --- | --- |
| <font style="color:rgb(51, 54, 57);">backUrl</font> | String  | path | 否 | 跳转地址 |


**响应参数：**

删除用户登录Cookie，如果传入<font style="color:rgb(51, 54, 57);">backUrl则302重定向到登录注销注销中心登录态</font>

### 6.4 账户管理接口
todo

### 6.5 租户管理接口
todo

### 6.6 对外开放接口
todo

## 产品原型设计


## 6 技术选型
+ Moyu-Framework
+ Spring-Security
+ SpringData-JPA
+ PostgreSQL
+ Vuetify  
 

