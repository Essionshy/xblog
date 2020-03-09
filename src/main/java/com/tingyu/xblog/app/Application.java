package com.tingyu.xblog.app;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.tingyu.xblog.app.repository.base.BaseRepositoryImpl;

/**
 * Halo main class.
 *
 * @author ryanwang
 * @date 2017-11-14
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
@EnableJpaRepositories(basePackages = "com.tingyu.xblog.app.repository", repositoryBaseClass = BaseRepositoryImpl.class)
public class Application extends SpringBootServletInitializer {

    private static ConfigurableApplicationContext CONTEXT;

    public static void main(String[] args) {
        // Customize the spring config location
        System.setProperty("spring.config.additional-location", "file:${user.home}/.xblog/,file:${user.home}/xblog-dev/");

        // Run application
        CONTEXT = SpringApplication.run(Application.class, args);

    }

    /**
     * Restart Application.
     */
    public static void restart() {
        ApplicationArguments args = CONTEXT.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            CONTEXT.close();
            CONTEXT = SpringApplication.run(Application.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        System.setProperty("spring.config.additional-location", "file:${user.home}/.xblog/,file:${user.home}/xblog-dev/");
        return application.sources(Application.class);
    }
}
