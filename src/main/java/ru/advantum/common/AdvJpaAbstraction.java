package ru.advantum.common;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"ru.advantum.common.config", "ru.advantum.common.abstraction"})
public class AdvJpaAbstraction {
}
