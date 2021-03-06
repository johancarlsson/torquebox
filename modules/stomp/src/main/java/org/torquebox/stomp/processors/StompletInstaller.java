package org.torquebox.stomp.processors;

import java.util.List;

import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController.Mode;
import org.projectodd.stilts.stomplet.container.SimpleStompletContainer;
import org.torquebox.core.as.CoreServices;
import org.torquebox.core.component.ComponentResolver;
import org.torquebox.core.runtime.RubyRuntimePool;
import org.torquebox.stomp.RubyStompletMetaData;
import org.torquebox.stomp.StompletService;
import org.torquebox.stomp.as.StompServices;

public class StompletInstaller implements DeploymentUnitProcessor {

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        DeploymentUnit unit = phaseContext.getDeploymentUnit();
        
        List<RubyStompletMetaData> allMetaData = unit.getAttachmentList( RubyStompletMetaData.ATTACHMENTS_KEY );
        
        for ( RubyStompletMetaData each : allMetaData ) {
            deploy( phaseContext, each );
        }
    }

    protected void deploy(DeploymentPhaseContext phaseContext, RubyStompletMetaData stompletMetaData) {
        DeploymentUnit unit = phaseContext.getDeploymentUnit();
        
        StompletService service = new StompletService();
        service.setConfig( stompletMetaData.getStompletConfig() );
        service.setDestinationPattern( stompletMetaData.getDestinationPattern() );
        
        phaseContext.getServiceTarget().addService( StompServices.stomplet( unit, stompletMetaData.getName() ), service )
            .addDependency( StompServices.container( unit ), SimpleStompletContainer.class, service.getStompletContainerInjector() )
            .addDependency( StompServices.stompletComponentResolver( unit, stompletMetaData.getName() ), ComponentResolver.class, service.getComponentResolverInjector() )
            .addDependency( CoreServices.runtimePoolName( unit, "stomplets" ), RubyRuntimePool.class, service.getRuntimePoolInjector() )
            .setInitialMode( Mode.ACTIVE )
            .install();
        
    }

    @Override
    public void undeploy(DeploymentUnit context) {
        
    }
    
    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger( "org.torquebox.stomp" );

}
