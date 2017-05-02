#Ark-BDADP
中软国际大数据应用开发平台

## 开发环境搭建说明 ##

 **1. GitLab账户注册**
注册后将账户名告知项目管理员，管理员会将账户加入团队中。

 **2. 使用Eclipse EGit插件导入工程**
      选择File->Import，选择Git->Project from Git, 点击下一步，输入Git仓库的URI.
      输入GitLab的用户名和密码，勾选Store in secure store(用户名和密码会以keystore的形式保存，以后不用再输入用户名和密码)
      选择分支，目前只有一个master分支，一直下一步直到结束，整个工程的下载就完成了，此时工程中的所有目录及文件都会被增加一个小数据库的图标。

 **3. 使用IDEA导入工程**
      选择VCS->Check out from Version Control->Git，输入Git Repository URL，点击Clone.

 **4. 安装配置Java8作为Eclipse工程执行环境**
       下载Java8 windows 64位并且安装，安装后配置JAVA_HOME及Path环境变量。
       在Eclipse的工程上右键点击并且选择Properties->Java Build Path, 在右边选择Libraries，此时JRE System Libaray会报错，选中JRE System Libarary后点击Edit，设置Installed JRE即可。

 **5. 安装配置Tomcat8.0.33作为开发用服务器**
       下载Tomcat 8.0.33并且解压至某一目录。
       由于工程是Eclipse Web工程并且默认使用Tomcat 8.0作为调试环境，所以需要再配置Tomcat 8.0 server。
       选择Window->Preference->Server->Runtime Environment，点击Add，选择Tomcat 8.0模板，输入Tomcat 8.0解压目录，选择JRE即可完成Tomcat 8.0 server的全局配置。
       右键点击工程选择Properties，选择Target Runtimes，检查是否有Tomcat 8.0。

完成上述步骤后右键点击bdadp-web工程并且选择Run As->Run on Server,然后选择Tomcat并且一直下一步直到结束，Eclipse会启动Tomcat并且加载工程代码，服务器启动后会自动打开欢迎页面index.html.

## 开发框架说明 ##

框架集成Spring(4.2.4.RELEASE)+SpringDataJPA(1.10.2.RELEASE)+Hibernate(5.0.9.Final)

Entity由Spring EntityManagerFactory管理，由HibernateJPA提供持久化，使用JPA注解
Dao由Spring Data提供持久化接口，使用方法请查看Spring Data官方文档说明
Service由Spring AOP提供声明的事务管理，方法声明必须符合声明规范，否则事务失效
Control由Spring Context完成ServletMapping，提供REST API，请求接受text/plain;charset=UTF-8，application/json;charset=UTF-8类型

静态资源映射为/**

开发注意事项规范，请参看示例

 **1. Entity**

   类使用@Entity注解，类名与数据表名一致，要符合JPA规范

    @Entity
    public class Entity {
    }

   主键用@Id标识，主键为字符串的使用UUID(32个长度的唯一字符串并去除'-'，util包有已修改的UUID)

     @Id
     private String xxxId;

   字段名与设计文档不相符的使用@Column、@JsonProperty转换属性名，

    @Column(name = "xxx_xxx")
    @JsonProperty("xxx_xxx")
    private String xxxXxx;

   日期类型字段使用@JsonFormat转换格式，如

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date xxxXxx;

 **2. Dao**

   声明接口继承Repository接口，通常情况只需声明方法不需实现

   当需要以属性xxx为条件查询时，那么方法名称可以声明为findByXxx，该属性可以是表中对应entity的任意属性名，首字母大写，方法参数与该属性类型、名称保持一致
   当需要多个属性条件查询，方法名中可以使用And连接，如声明为findByXxx1AndXxx2，更详细内容请参看Spring Data官方文档

    public interface EntityDao extends Repository<Entity, String> {
        public Iterable<Entity> findByXxx(String xxx);
    }

   CrudRepository接口已经声明常用增删查改的方法，继承后可以直接使用

 **3. Service**

   声明接口类并在Impl包下实现接口方法，实现类使用@Component注解

    @Component
    public class EntityServiceImpl implements EntityService {
    }

   Service所有方法默认都会被添加事务，请按照事务方法配置声明接口方法，否则事务失效

   数据库写操作，方法名称要以save、add、create、insert、update、merge、del、remove、put或use开头
   数据库读操作，方法名称要以get、count、find或list开头，这时事务为read-only

    public interface EntityService {
        public Entity createEntity(Entity s);
        public Iterable<Entity> getAllEntity();
        public void delEntity(String id);
    }

   其他命名方法的又发生了数据访问，默认均为read-only

   dao接口使用@Autowired注解，如

    @Autowired private EntityDao dao;

 **4. Controller**

   类使用@Controller和@RequestMapping注解，如

     @Controller
     @RequestMapping(value = "/service/v1")
     public class EntityController {
     }

   方法使用@RequestMapping、@ResponseBody注解
   方法中@RequestMapping注解参数如(value = "接口文档中该接口URL的后缀", method = 接口文档该接口method, produces = "接口文档该接口contentType")
   方法参数需要将请求数据装入javaBean的，在参数前加@RequestBody
   所有方法均返回ResultBody, body中装入返回内容(result)，捕获到异常后装入返回错误码(resultCode)和错误信息(resultMessage)

    @RequestMapping(value = "/entity", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResultBody createEntity(@RequestBody Entity s) {
        ResultBody<Entity> body = new ResultBody<>();
        try {
          body.setResult(service.createEntity(s));
        } catch (Exception e) {
          logger.error("/entity", e);
          body.setResultCode(1);
          body.setResultMessage(e.getMessage());
        }
        return body;
    }

   service接口使用@Autowired注解，如

    @Autowired private EntityService service;

 **5. Logger**

   框架集成slf4j(1.7.12)+slf4j-log4j12(1.7.12)+log4j(1.2.17)，通过slf4j-log4j12桥接log4j绑定logger

   LoggerFactory请使用org.slf4j.LoggerFactory

    private Logger logger = LoggerFactory.getLogger(this.getClass());

## 工程打包 ##

**1. 使用Maven打包**

   standalone：在工程目录下使用

     mvn package -P standalone

   war：在工程目录下使用

     mvn package -P war

**2. 使用IDEA打包**

   standalone：点击右侧工具栏的Maven Projects，勾选Profiles为standalone，选择root目录->Lifecycle->package

   war：点击右侧工具栏的Maven Projects，勾选Profiles为war，选择root目录->Lifecycle->package


----

# 多环境配置文件管理


### 开发人员
eclipse 中bdadp-web项目右键 run as -- run  configuration--Arguments-- VM arguments中添加。local配置文件不必上传git追踪管理

```
-Dspring.profiles.active="local"
```

### 部署人员

tomcat 中 catalina.bat（.sh） 添加JAVA_OPS。通过设置active选择不同配置文件
```
set JAVA_OPTS="-Dspring.profiles.active=test"
```



