package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.utilities.UntrustworthyData;
import sun.security.x509.X500Name;

import java.util.Set;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class EchoInitiator extends FlowLogic<Void> {
    private String message;
    private String counterParty;

    private final ProgressTracker progressTracker = new ProgressTracker();

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }


    public EchoInitiator(String message, String counterParty) {
        this.message = message;
        this.counterParty = counterParty;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        // Initiator flow logic goes here.

       Set<Party> parties = getServiceHub().getIdentityService().partiesFromName(this.counterParty,true);

        for (Party p : parties) {
         FlowSession session = initiateFlow(p);
         session.send(message);

         UntrustworthyData<String> receivedData = session.receive(String.class);
         String receivedStr = receivedData.unwrap(data -> {return data;});
         System.out.println(receivedStr);
        }

        return null;
    }

}
