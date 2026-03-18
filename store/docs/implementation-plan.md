# 基于 Spring Boot 的传统手工艺品交易服务平台设计与实现（实施稿）

## 1. 项目定位与目标
本平台服务三类用户：消费者、商家、管理员，围绕“商品交易 + 定制服务 + 平台运营”构建统一的电商系统。

- 消费者：浏览商品、下单、支付（可模拟）、查看订单、申请定制。
- 商家：发布商品、管理库存、处理订单、处理定制需求。
- 管理员：管理用户、公告、广告、推荐商品，维护平台运营。

## 2. 技术架构
- 后端：Spring Boot 3 + MyBatis + MySQL 8
- 前端：Thymeleaf + HTML/CSS/JavaScript
- 权限：Session + Interceptor（按角色拦截）
- 架构：Controller / Service / Mapper / Entity 分层

## 3. 分层设计
### 3.1 前端展示层
主要页面：
1. 首页（公告、轮播广告、热销推荐）
2. 登录/注册页
3. 商品列表页（分类/关键字/价格区间筛选）
4. 商品详情页（加入购物车、立即购买、申请定制）
5. 购物车页
6. 订单结算页
7. 个人中心页
8. 商家后台（商品与订单管理）
9. 管理员后台（用户/公告/广告/推荐管理）
10. 定制服务申请页

### 3.2 业务逻辑层
建议包结构：

```text
src/main/java/com/crafts/platform
├── controller
├── service
│   └── impl
├── mapper
├── entity
├── dto
├── vo
├── config
├── interceptor
├── utils
└── exception
```

核心控制器：
- UserController：注册、登录、用户信息管理
- ProductController：商品查询、搜索、详情
- CartController：购物车增删改查
- OrderController：下单、模拟支付、订单跟踪
- CustomOrderController：提交与处理定制单
- MerchantController：商家商品管理与发货
- AdminController：平台运营管理

### 3.3 数据持久层
采用 MyBatis + Mapper XML（或注解）完成数据访问，典型 Mapper：
- UserMapper、MerchantMapper、CategoryMapper、ProductMapper
- CartMapper、OrderMapper、OrderItemMapper
- CustomOrderMapper、NoticeMapper、AdvertisementMapper、RecommendProductMapper

## 4. 关键业务流程
### 4.1 用户注册登录
1. 注册提交基础信息（用户名、密码、联系方式）
2. 后端校验用户名唯一
3. 密码加密后入库
4. 登录成功写入 Session（userId、role、nickname）
5. 按角色跳转至对应页面

### 4.2 商品交易流程
1. 浏览商品并加入购物车
2. 勾选购物车项并结算
3. 校验库存、生成订单主表与明细
4. 扣减库存、清理已结算购物车项
5. 模拟支付后状态：待付款 -> 待发货
6. 商家发货填写物流单号
7. 消费者确认收货，订单完成

### 4.3 定制服务流程
1. 消费者提交定制需求（预算、描述、期望时间）
2. 商家查看并沟通（备注）
3. 商家确认接单或拒绝
4. 进入制作中、已完成等状态流转

### 4.4 管理员运营流程
1. 用户管理（启用/禁用）
2. 公告管理（置顶/发布）
3. 广告管理（轮播、排序、启用）
4. 推荐商品管理（按销量等指标维护）

## 5. 订单与定制状态建议
- 订单状态：0待付款 / 1待发货 / 2已发货 / 3已完成 / 4已取消
- 定制状态：0待查看 / 1已沟通 / 2已确认 / 3已拒绝 / 4制作中 / 5已完成

## 6. 非功能性要求
- 安全：密码加密存储，接口鉴权拦截
- 性能：常用查询字段加索引；商品列表分页
- 可维护：统一异常处理、统一返回结构、日志记录关键流程
- 可扩展：角色、状态字段预留扩展空间

## 7. 实施里程碑（建议）
1. 第1周：完成数据库建模与基础工程搭建
2. 第2周：完成用户/商品/购物车模块
3. 第3周：完成订单流程与商家后台
4. 第4周：完成定制与管理员运营模块，联调测试
