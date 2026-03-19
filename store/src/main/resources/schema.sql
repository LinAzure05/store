CREATE DATABASE IF NOT EXISTS craft_trade_platform DEFAULT CHARACTER SET utf8mb4;
USE craft_trade_platform;

CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(255) COMMENT '头像路径',
    role VARCHAR(20) NOT NULL COMMENT '角色：consumer/merchant/admin',
    status TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='用户表';

CREATE TABLE IF NOT EXISTS merchant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商家信息ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    shop_name VARCHAR(100) NOT NULL COMMENT '店铺名称',
    shop_desc TEXT COMMENT '店铺简介',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(255) COMMENT '商家地址',
    business_scope VARCHAR(255) COMMENT '经营范围',
    audit_status TINYINT DEFAULT 1 COMMENT '审核状态：0待审核 1通过 2拒绝',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_merchant_user FOREIGN KEY (user_id) REFERENCES `user`(id)
) COMMENT='商家扩展信息表';

CREATE TABLE IF NOT EXISTS category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    sort_num INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用 0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT='商品分类表';

CREATE TABLE IF NOT EXISTS product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    cover_image VARCHAR(255) COMMENT '封面图',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT DEFAULT 0 COMMENT '库存',
    sales INT DEFAULT 0 COMMENT '销量',
    description TEXT COMMENT '商品描述',
    support_custom TINYINT DEFAULT 0 COMMENT '是否支持定制：1是 0否',
    status TINYINT DEFAULT 1 COMMENT '状态：1上架 0下架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_merchant FOREIGN KEY (merchant_id) REFERENCES merchant(id),
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id)
) COMMENT='商品表';

CREATE TABLE IF NOT EXISTS cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车ID',
    user_id BIGINT NOT NULL COMMENT '消费者ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    checked TINYINT DEFAULT 1 COMMENT '是否选中结算',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES `user`(id),
    CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES product(id)
) COMMENT='购物车表';

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '消费者ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    order_status TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态：0待付款 1待发货 2已发货 3已完成 4已取消',
    pay_time DATETIME COMMENT '支付时间',
    delivery_time DATETIME COMMENT '发货时间',
    finish_time DATETIME COMMENT '完成时间',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货电话',
    receiver_address VARCHAR(255) NOT NULL COMMENT '收货地址',
    logistics_no VARCHAR(100) COMMENT '物流单号',
    remark VARCHAR(255) COMMENT '订单备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES `user`(id)
) COMMENT='订单主表';

CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(255) COMMENT '商品图片',
    price DECIMAL(10,2) NOT NULL COMMENT '购买单价',
    quantity INT NOT NULL COMMENT '购买数量',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计',
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT fk_order_item_merchant FOREIGN KEY (merchant_id) REFERENCES merchant(id)
) COMMENT='订单明细表';

CREATE TABLE IF NOT EXISTS custom_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '定制订单ID',
    custom_no VARCHAR(50) NOT NULL UNIQUE COMMENT '定制单编号',
    user_id BIGINT NOT NULL COMMENT '消费者ID',
    merchant_id BIGINT NOT NULL COMMENT '商家ID',
    category_id BIGINT COMMENT '工艺品分类ID',
    title VARCHAR(200) NOT NULL COMMENT '定制标题',
    requirement_desc TEXT NOT NULL COMMENT '需求描述',
    reference_image VARCHAR(255) COMMENT '参考图片',
    budget DECIMAL(10,2) COMMENT '预算金额',
    expected_date DATE COMMENT '期望完成日期',
    status TINYINT DEFAULT 0 COMMENT '状态：0待查看 1已沟通 2已确认 3已拒绝 4制作中 5已完成',
    merchant_reply TEXT COMMENT '商家回复',
    contact_info VARCHAR(100) COMMENT '联系方式',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_custom_order_user FOREIGN KEY (user_id) REFERENCES `user`(id),
    CONSTRAINT fk_custom_order_merchant FOREIGN KEY (merchant_id) REFERENCES merchant(id),
    CONSTRAINT fk_custom_order_category FOREIGN KEY (category_id) REFERENCES category(id)
) COMMENT='定制订单表';

CREATE TABLE IF NOT EXISTS notice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
    title VARCHAR(200) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶',
    status TINYINT DEFAULT 1 COMMENT '状态：1发布 0下线',
    create_by BIGINT COMMENT '发布人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='公告表';

CREATE TABLE IF NOT EXISTS advertisement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '广告ID',
    title VARCHAR(200) NOT NULL COMMENT '广告标题',
    image_url VARCHAR(255) NOT NULL COMMENT '广告图片',
    link_url VARCHAR(255) COMMENT '跳转链接',
    sort_num INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用 0停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT='广告表';

CREATE TABLE IF NOT EXISTS recommend_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '推荐ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    sort_num INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用 0停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recommend_product FOREIGN KEY (product_id) REFERENCES product(id)
) COMMENT='推荐商品表';


-- 默认管理员账号：admin / Admin@123（BCrypt 加密）
INSERT INTO `user` (username, password, phone, email, role, status)
SELECT 'admin', '$2a$10$5B2mh9RzGQjQJfQn8hljIu2Sh4fMcf49M/5xULixYzA7fY4Hi9f.e', '13800000000', 'admin@crafts.com', 'admin', 1
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE username = 'admin');
