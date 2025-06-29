package io.github.sandeeplakka.mockify.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

//TODO: WIP
@ConfigurationProperties(prefix = "mockify")
public class JoinPolicyProperties {
    private Mode joinPolicy = Mode.SHALLOW;

    public Mode getJoinPolicy() {
        return joinPolicy;
    }

    public void setJoinPolicy(Mode mode) {
        this.joinPolicy = mode;
    }

    public enum Mode {FKS, SHALLOW, DEEP}
}
