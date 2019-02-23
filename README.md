## 介绍
这是一个事件驱动学习的demo，用于以下目的：
- 熟悉事件驱动的设计思想
- 会在项目中使用事件驱动(spring event和 eventbus)
- spring event和 eventbus 的异同点

通过博客看更清晰：

## QucikStart

### spring event
[spring event官方文档索引](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#context-functionality-events)

三要素：
-  `Event`(事件)
- `Publisher`(发布者)
- `Listener`(监听者)


#### 举例场景
用户下订单，推送发短信通知，发微信通知。

#### 创建一个 `Event`
直接继承`ApplicationEvent`即可。

示例代码:
```java
@Data
public class CreateOrderEvent extends ApplicationEvent {

    private Order order;

    public CreateOrderEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }
}
```

#### 创建一个 `Publisher`
使用`ApplicationEventPublisher`发布即可。

示例代码:
```java
@Slf4j
@Component
public class EventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishCreateOrderEvent(CreateOrderEvent createOrderEvent) {
        log.info("发布了一个[订单创建]事件：{}", createOrderEvent);
        applicationEventPublisher.publishEvent(createOrderEvent);
    }
}
```

#### 监听事件
监听者的创建可以使用两种方式：
- 实现`ApplicationListener`接口
- 基于注解式的监听(推荐，要求 Spring 4.2 + )

使用实现`ApplicationListener`接口的方式示例：
```java
@Slf4j
@Component
public class DuanXinNoticeListener implements ApplicationListener<CreateOrderEvent> {
    @Override
    public void onApplicationEvent(CreateOrderEvent event) {
        log.info("实现接口方式：收到[创建订单]事件。[短信通知]：亲爱的{}，您的订单[{}]已被创建", event.getOrder().getUserName(), event.getOrder().getOrderName());
    }
}
```
这种方式不太优雅，每次都要实现一次接口不太爽。那么推荐使用使用注解式监听器。

注解式监听只需要在监听方法逻辑上使用注解`@EventListener`即可，而且注解支持spel表达式。

注解式示例：
```java
@Slf4j
@Component
public class CreateOrderListener {

    @EventListener(condition = "#createOrderEvent.order.status == 1")
    public void processCreateOrderEvent(CreateOrderEvent createOrderEvent) {
        log.info("注解式 spring event 收到消息，订单名称:{} ; 订单状态为：{} ; 开始处理相应的事件。", createOrderEvent.getOrder().getOrderName(), createOrderEvent.getOrder().getStatus());
    }

    @EventListener(condition = "#createOrderEvent.order.status == 2")
    public void processCreateOrderEvent2(CreateOrderEvent createOrderEvent) {
        log.info("注解式 spring event 收到消息，订单名称:{} ; 订单状态为：{} ; 开始处理相应的事件。", createOrderEvent.getOrder().getOrderName(), createOrderEvent.getOrder().getStatus());
    }
}
```

#### 使用事件发布
事件在控制器里使用：
```java
@Slf4j
@RestController
@RequestMapping("/event")
public class EventDemoController {

    @Autowired
    private EventPublisher createOrderEventPublisher;

    @PostMapping("/spring/createOrder")
    public String createOrder(Order order) {
        createOrderEventPublisher.publishCreateOrderEvent(new CreateOrderEvent(this, order));
        return "ok";
    }

}
```

#### 测试创建订单事件
发送请求：
```
curl -X POST \
  http://localhost:9090/event/spring/createOrder \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -H 'postman-token: a632443a-b7b2-40df-6bc3-3881babd15e3' \
  -d 'id=1&orderName=53%E5%BA%A62018%E9%A3%9E%E5%A4%A9%E8%8C%85%E5%8F%B0&status=1&userName=%E5%BC%A0%E4%B8%89'
```

可以看到控制台打印：
```
2019-02-21 10:53:20.750  INFO 24168 --- [nio-9090-exec-3] c.gemantic.wealth.spring.EventPublisher  : 发布了一个[订单创建]事件：CreateOrderEvent(order=Order(id=1, orderName=53度2018飞天茅台, status=1, userName=张三))
2019-02-21 10:53:20.750  INFO 24168 --- [nio-9090-exec-3] c.g.wealth.spring.CreateOrderListener    : 注解式 spring event 收到消息，订单名称:53度2018飞天茅台 ; 订单状态为：1 ; 开始处理相应的事件。
2019-02-21 10:53:20.750  INFO 24168 --- [nio-9090-exec-3] c.g.wealth.spring.DuanXinNoticeListener  : 实现接口方式：收到[创建订单]事件。[短信通知]：亲爱的张三，您的订单[53度2018飞天茅台]已被创建
2019-02-21 10:53:20.750  INFO 24168 --- [nio-9090-exec-3] c.g.wealth.spring.WeiXinNoticeListener   : 实现接口方式：收到[创建订单]事件。[微信通知]：亲爱的张三，您的订单[53度2018飞天茅台]已被创建
```
事件已被执行，把`status`改为2：
```
2019-02-21 10:55:51.951  INFO 24168 --- [nio-9090-exec-5] c.gemantic.wealth.spring.EventPublisher  : 发布了一个[订单创建]事件：CreateOrderEvent(order=Order(id=1, orderName=53度2018飞天茅台, status=2, userName=张三))
2019-02-21 10:55:51.951  INFO 24168 --- [nio-9090-exec-5] c.g.wealth.spring.CreateOrderListener    : 注解式 spring event 收到消息，订单名称:53度2018飞天茅台 ; 订单状态为：2 ; 开始处理相应的事件。
2019-02-21 10:55:51.951  INFO 24168 --- [nio-9090-exec-5] c.g.wealth.spring.DuanXinNoticeListener  : 实现接口方式：收到[创建订单]事件。[短信通知]：亲爱的张三，您的订单[53度2018飞天茅台]已被创建
2019-02-21 10:55:51.951  INFO 24168 --- [nio-9090-exec-5] c.g.wealth.spring.WeiXinNoticeListener   : 实现接口方式：收到[创建订单]事件。[微信通知]：亲爱的张三，您的订单[53度2018飞天茅台]已被创建
```
可见可以很方便的根据条件执行监听。

#### 补充1: 使用异步事件处理
可以在监听方法上使用注解`@Async`，前提是需要spring应用启动异步功能：`@EnableAsync`


#### 补充2: TransactionalEventListener实现事务监控
我们知道，比如如上“发送短信”，“发送微信”等通知是需要在“订单入库”的基础上才能执行，也就是说“订单入库”完成，才能处理执行相应的监听器的逻辑。

使用`@TransactionalEventListener`即可完成如上控制，可参考：
[spring官方文档 Transaction-bound Events](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/data-access.html#transaction-event)

注意一点，官方文档中有提出：
> If no transaction is running, the listener is not invoked at all, since we cannot honor the required semantics. You can, however, override that behavior by setting the fallbackExecution attribute of the annotation to true.

也就是说，实现事务控制必须`开启事务`并且处于一个事务中，**否者该监听器不会被调用**。(不过可以设置`fallbackExecution=true`)

前提条件：
- 开启事务管理：`@EnableTransactionManagement`
- 配置事务管理器: `PlatformTransactionManager`(这一步`@EnableTransactionManagement`已默认实现，如须按需配置可手动配置)
- 使用`@Transactional`处理一个事务

示例代码如下（前提已开启事务）：

订单服务-“订单入库”：
```java
@Slf4j
@Service
public class OrderService {

    @Autowired
    private EventPublisher createOrderEventPublisher;

    /**
     * 插入订单表操作
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(Order order) {
        log.info("[订单入库] start");

        createOrderEventPublisher.publishCreateOrderEvent(new CreateOrderEvent(this, order));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("[订单入库] end");
    }

}
```

两个新的监听器，一个不是事务的，一个是事务的做对比：
```java
@EventListener(condition = "#createOrderEvent.order.status == 3")
public void processCreateOrderEvent3(CreateOrderEvent createOrderEvent) {
    log.info("[EventListener] 注解式 spring event 收到消息，订单名称:{} ; 订单状态为：{} ; 开始处理相应的事件。", createOrderEvent.getOrder().getOrderName(), createOrderEvent.getOrder().getStatus());
}

@TransactionalEventListener(condition = "#createOrderEvent.order.status == 3", phase = TransactionPhase.AFTER_COMMIT)
public void processCreateOrderEvent4(CreateOrderEvent createOrderEvent) {
    log.info("[TransactionalEventListener] 注解式 spring event 收到消息，订单名称:{} ; 订单状态为：{} ; 开始处理相应的事件。", createOrderEvent.getOrder().getOrderName(), createOrderEvent.getOrder().getStatus());
}
```

控制器里的事务调用请求处理：
```java
@PostMapping("/spring/createOrder_transaction")
public String createOrder_transaction(Order order) {
    orderService.insert(order);
    return "ok";
}
```

开启服务发请求测试:
```
curl -X POST \
  http://localhost:9090/event/spring/createOrder_transaction \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/x-www-form-urlencoded' \
  -H 'postman-token: 1e9d2357-ba51-6aa6-156b-6fe2559cdb06' \
  -d 'id=1&orderName=53%E5%BA%A62018%E9%A3%9E%E5%A4%A9%E8%8C%85%E5%8F%B0&status=3&userName=%E5%BC%A0%E4%B8%89'
```

结果如下：
```
2019-02-21 14:07:31.467  INFO 24496 --- [nio-9090-exec-1] c.gemantic.wealth.service.OrderService   : [订单入库] start
2019-02-21 14:07:31.468  INFO 24496 --- [nio-9090-exec-1] c.gemantic.wealth.spring.EventPublisher  : 发布了一个[订单创建]事件：CreateOrderEvent(order=Order(id=1, orderName=53度2018飞天茅台, status=3, userName=张三))
2019-02-21 14:07:31.482  INFO 24496 --- [nio-9090-exec-1] c.g.wealth.spring.CreateOrderListener    : [EventListener] 注解式 spring event 收到消息，订单名称:53度2018飞天茅台 ; 订单状态为：3 ; 开始处理相应的事件。
2019-02-21 14:07:31.483  INFO 24496 --- [nio-9090-exec-1] c.g.wealth.spring.DuanXinNoticeListener  : 实现接口方式：收到[创建订单]事件。[短信通知]：亲爱的张三，您的订单[53度2018飞天茅台]已被创建
2019-02-21 14:07:31.483  INFO 24496 --- [nio-9090-exec-1] c.g.wealth.spring.WeiXinNoticeListener   : 实现接口方式：收到[创建订单]事件。[微信通知]：亲爱的张三，您的订单[53度2018飞天茅台]已被创建
2019-02-21 14:07:36.483  INFO 24496 --- [nio-9090-exec-1] c.gemantic.wealth.service.OrderService   : [订单入库] end
2019-02-21 14:07:36.484  INFO 24496 --- [nio-9090-exec-1] c.g.wealth.spring.CreateOrderListener    : [TransactionalEventListener] 注解式 spring event 收到消息，订单名称:53度2018飞天茅台 ; 订单状态为：3 ; 开始处理相应的事件。
```
请求大约5秒后返回，可以看到控制台，`EventListener`是直接处理的，`TransactionalEventListener`是在订单`insert`方法调用后再处理。这是因为`TransactionalEventListener`默认的处理是事务`commit`之后处理的，这里可以改注解的`phase`属性。参考枚举类：`TransactionPhase`。
```java
public enum TransactionPhase {
	BEFORE_COMMIT,
	AFTER_COMMIT,
	AFTER_ROLLBACK,
	AFTER_COMPLETION
}
```
#### 补充3: 事件的父类监听
如果事件继承了某一父类，此父类也有监听，则每次发布事件，父类子类的监听器都会执行。

### Guava EventBus
Google出品的轻量级进程内事件框架。

可参考：https://github.com/google/guava/wiki/EventBusExplained

## Spring Event与 Guava EventBus的比较
--|Spring Event | Guava EventBus
---|---|---
Event| 任意对象 | 任意对象
Publisher| ApplicationEventPublisher | EventBus
Subscriber| @EventListener | @Subscribe 
发布方法 | ApplicationEventPublisher#publishEvent | EventBus#post
注册方法 | spring自动注册 | 手动注册：EventBus#register
是否支持异步 | 支持。@Async | 支持。AsyncEventBus
是否支持事务 | 支持。@TransactionalEventListener | 不支持。
是否支持条件过滤 | 支持。 | 不支持。
是否支持DeadEvent | 不支持。 | 支持。
是否支持事件继承 | 支持。 | 支持。
事件异常处理 | 异常上抛 | 捕获异常并不上抛
复杂度 | 复杂 | 轻量

### 事件处理异常对比
--|Spring Event | Guava EventBus
---|---|---
同步|会对其他监听器有影响|不影响其他监听器的处理
异步|不影响其他监听器的处理|不影响其他监听器的处理


- 异步情况下，都是多线程处理，无差异。
- 同步情况下，同一线程处理，由于Spring会抛出异常，适合事件有依赖的情况，而EventBus适合事件互不依赖的场景。

举个栗子：
- 下订单场景一。订单已经创建，那么会发送短信通知，微信通知，此二者之间无依赖，适合EventBus.
- 下订单场景二。如果创建订单也作为事件，那么微信短信通知需依赖订单事件已完成，如果创建订单异常失败则不需要发送通知，适合Spring Event

### 异步的实现对比
- EventBus的异步实现是依赖于线程池的，在创建的时候确定，实际上是在监听器调用方法的时候采用的异步，同一个`EventBus`都是同一种处理方式(同步or异步)。
- Spring是通过拦截器的机制，被`@Async`的方法都会被`AsyncExecutionInterceptor`拦截，然后采用多线程的方式调用

综上，Spring Event 会更加灵活一些。