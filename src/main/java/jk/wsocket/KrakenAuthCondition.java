package jk.wsocket;

import jk.rest.tools.LocalPropLoader;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class KrakenAuthCondition implements Condition {

    private static final LocalPropLoader props = new LocalPropLoader();

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return props.keysFound();
    }
}
