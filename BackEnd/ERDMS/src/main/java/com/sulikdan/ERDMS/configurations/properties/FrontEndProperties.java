package com.sulikdan.ERDMS.configurations.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Daniel Šulik on 24-Oct-20
 * <p>
 * Class FrontEndProperties is used to get properties of FrontEnd.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="fe")
public class FrontEndProperties {

    private String port;
    private String address;

}
