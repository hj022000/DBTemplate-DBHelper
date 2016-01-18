#DBTemplate-1.x
###最近整理的DbTemplate是java的jdbc模板，它具有小巧，使用灵活、方便支持spring和操作对象进行CRUD。 
###概况： 
    1、目前DQL和分页暂时支持mysql、hsql和oracle；其他数据库的功能后续实现；
    2、无缝接入spring。
        <!-- dbHelper jdbcTemplate --> 
        <bean id="dbtemplate" class="com.dwl.dbtemplate.DbTemplate" init-method="InitDialect"> 
            <property name="dataSource" ref="dataSource"></property> 
        </bean> 
    3、DBTemplate大大减少了开发人员对dao层的编写和维护，节约了开发人员的时间。
    4、使用时候需要配合log4j使用。 
    5、缓存功能后续提供。

###如果你不喜欢用Hibernate、Mybaits这类ORM框架，不想维护和编写大量的配置文件，而且还要面向对象，那就使用DbTemplate吧！


###---使用示例--
        DriverDataSource ds = new DriverDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8", "root", "root");
        DbTemplate db = new DbTemplate();
        db.setDataSource(ds);
        db.InitDialect();
		
        /***
         * 实现地方
         */
 ##QQ交流群：497876380  欢迎志同道合者一起完善。
