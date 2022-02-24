### spring aop:
    1. 创建aop异常拦截注解(ErrorAnnotation/LoginfoAnnotation)
    2. 创建切面类
        1. 添加@Aspect注解和@Component注解
        2. OperationLogAspect
    3. 定义切点:
        1. 注解方式:
        OperationLogAspect.errorAspect         @Pointcut("@annotation(com.example.demo.aop.config.ErrorAnnotation)")
        OperationLogAspect.loginfoAspec        @Pointcut("@annotation(com.example.demo.aop.config.LoginfoAnnotation)"
        2. 表达式方式:
        OperationLogAspect.point    @Pointcut(value = "execution(* com.example.demo..*.*(..))")
    4. 定义切点触发业务逻辑:
        OperationLogAspect.afterThrowing(@AfterThrowing:发生异常时业务处理)
        1. @After:后置切入
        2. @Before:前置切入
        3. @Around:环绕切入
    5. 定义切入点方法
        MqSendController.aop方法添加需要处理的拦截业务逻辑注解:@ErrorAnnotation(value = "默认值.......................")
***

### rocket mq:
    - 添加mq配置属性:
        - 控制台属性配置
            suning.rocketmq.producerGroup=user-group
            suning.rocketmq.conumerGroup=user-group
            suning.rocketmq.namesrvaddr=localhost:9876
        - mq属性配置:
            rocketmq.name-server:127.0.0.1:9876
            rocketmq.producer.group:testgroup
            rocketmq.producer.send-msg-timeout:5000
            rocketmq.producer.retry-times-when-send-failed:2
            rocketmq.producer.max-message-size:4194304
    - 注入mq:
            @Autowired private RocketMQTemplate rocketMQTemplate;
    - 发送消息:
            MqSendController.sendMessage
    - 定义消息监听:
            创建ConsumerListener监听类,添加@RocketMQMessageListener(topic = "topic-test", selectorExpression = "nice", consumerGroup = "testgroup",consumeMode = ConsumeMode.ORDERLY)注解
        