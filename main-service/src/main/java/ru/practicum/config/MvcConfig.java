//package ru.practicum.config;
//
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//import org.springframework.web.servlet.view.JstlView;
//
//@Configuration
//@ComponentScan
//public class MvcConfig implements WebMvcConfigurer {
////
//////    // Для страниц, которые никак не обрабатываются сервером, а просто возвращают страницу,
//////    // маппинг можно настроить в конфигурации.
//////    // Страница login обрабатывается Spring Security контроллером по умолчанию,
//////    // поэтому для неё отдельный контроллер не требуется.
////////    @Override
////////    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
////////        configurer.enable();
////////    }
//////
////////    @Override
////////    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
////////        configurer.enable();
////////    }
//////
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/login").setViewName("login");
////        registry.addViewController("/news").setViewName("news");
//    }
////
////    @Override
////    public void configureViewResolvers(ViewResolverRegistry registry) {
////        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
////        viewResolver.setViewClass(JstlView.class);
////        viewResolver.setPrefix("/WEB-INF/jsp/");
////        viewResolver.setSuffix(".jsp");
////        registry.viewResolver(viewResolver);
////    }
//}
